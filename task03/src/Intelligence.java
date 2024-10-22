import java.io.*;
import java.util.*;

public class Intelligence {
    private static final String UNGUESSED = "-";
    private static final String WRONG_POS = "?";
    private static final String WRONG_GUESS = "X";
    private static final String CORRECT = "O";
    private final File f;
    private final Map<Integer, Map<String, String>> map;
    private final List<String> inputs;

    public Intelligence(File f, String input, int numOptions) {
        this.f = f;
        this.inputs = Arrays.asList(input.split(""));
        this.map = new HashMap<>();
        for (int pos = 0; pos < numOptions; pos++) {
            Map<String, String> guesses = new HashMap<>();
            for (int guess = 0; guess < inputs.size(); guess++) {
                guesses.put(inputs.get(guess), UNGUESSED);
            }
            map.put(pos, guesses);
        }
    }

    private void addLastGuess() {
        String last = "";
        try (FileReader fr = new FileReader(f); BufferedReader br = new BufferedReader(fr);) {
            String line = "";
            while ((line = br.readLine()) != null) {
                last = line;
            }
        } catch (IOException e) {
            System.err.println("Error reading saved data. Game will proceed without providing any guess suggestions. ");
        }

        String[] temp = last.split(",");
        String guess = temp[0].trim();
        String[] result = temp[1].trim().split(" ");
        for (int i = 0; i < guess.length(); i++) {
            //iterate through the guess, and cross check with the map. Update map accordingly.
            // Check if pos is correct
            if (inputs.contains(result[i])) {
                handleCorrect(i, guess.substring(i, i+1));
            
            // Check if pos is wrong, and it is no longer found in other parts of the answer
            } else if (result[i].equals("_")) {
                handleWrong(guess.substring(i, i+1));

            // Check if the guess is a wrong pos.
            } else if (result[i].equals("?")) {
                handleWrongPos(i, guess.substring(i, i+1));
            }
        }
    }

    private void handleCorrect(int pos, String guess) {
        // Set all options to WRONG except the correct guess given.
        Map<String, String> guesses = map.get(pos);
        for (String k : guesses.keySet()) {
            guesses.put(k, "WRONG_GUESS");
        }
        guesses.put(guess, CORRECT);
    }

    private void handleWrong(String guess) {
        // update all pos for this guess option to X. If the value in map is O, skip it.
        for (Map.Entry<Integer, Map<String, String>> m : map.entrySet()) {
            int currPos = m.getKey();
            Map<String, String> innerMap = m.getValue();
            if (!innerMap.get(guess).equals(CORRECT)) {
                innerMap.put(guess, WRONG_GUESS);
            }
            map.put(currPos, innerMap);
        }
    }

    private void handleWrongPos(int pos, String guess) {
        for (int i = 0; i < map.size(); i++) {
            Map<String, String> guesses = map.get(i);
            if (guesses.get(guess).equals(CORRECT)) {continue;}
            else if (i == pos) {guesses.put(guess, WRONG_GUESS);}
            else {
                guesses.put(guess, WRONG_POS);
            }
        }
    }

    public String suggestGuess() {
        // Run through commands to suggest a guess based on previous data
        addLastGuess();
        List<String> suggestion = new ArrayList<>();

        // Getting correct guesses first
        for (int pos = 0; pos < map.size(); pos++) {
            suggestion.add("_");
            Map<String, String> options = map.get(pos);
            for (Map.Entry<String, String> m : options.entrySet()) {
                String option = m.getKey();
                String result = m.getValue();
                if (result.equals(CORRECT)) {
                    suggestion.set(pos, option);
                    break;
                }
            }
        }

        // Getting other guesses
        for (int pos = 0; pos < suggestion.size(); pos++) {
            if (suggestion.get(pos).equals("_")) {
                Map<String, String> options = map.get(pos);
                List<String> wrongPos = new ArrayList<>();
                List<String> unguessed = new ArrayList<>();
                for (Map.Entry<String, String> m : options.entrySet()) {
                    String option = m.getKey();
                    String result = m.getValue();
                    if (result.equals(UNGUESSED)) {
                        unguessed.add(option);
                    } else if (result.equals(WRONG_POS)) {
                        wrongPos.add(option);
                    }
                }
                if (!wrongPos.isEmpty()) {
                    suggestion.set(pos, wrongPos.getFirst());
                } else {
                    suggestion.set(pos, unguessed.getFirst());
                }
            }
        }

        return suggestion.stream().reduce("", (x, y) -> x + y);
    }
}
