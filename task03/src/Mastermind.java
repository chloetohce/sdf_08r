
import java.io.*;
import java.security.SecureRandom;
import java.util.*;

public class Mastermind {
    private static final String VALID_INPUT = "123456";
    private static final int NUM_OPTIONS = 4;
    private final int[] answer = new int[NUM_OPTIONS];
    private int tries;
    private boolean isWon;
    private final boolean provideHelp;
    private final File db;
    
    public Mastermind(String db, boolean help) {
        for (int i = 0; i < NUM_OPTIONS; i++) {
            Random rand = new SecureRandom();
            answer[i] = rand.nextInt(1,VALID_INPUT.length() + 1);
        }
        tries = 0;
        isWon = false;
        try {
            File f = new File(db);
            f.delete();
            f.createNewFile();
        } catch (IOException e) {
            System.err.println("Error with file handling.");
            System.exit(-1);
        }
        this.db = new File(db);
        provideHelp = help;
    }


    private String checkPlacement(Integer[] guessArr) {
        List<String> result = new ArrayList<>();
        List<Integer> answerList = new ArrayList<>();
        for (int i : answer) {
            answerList.add(i);
        }
        for (int i = 0; i < NUM_OPTIONS; i++) {
            if (guessArr[i] == answer[i]) {
                result.add(guessArr[i].toString());
                answerList.set(i, 0); // set to 0 to mean that answer option has been matched
            } else {
                result.add("_");
            }
        }

        for (int i = 0; i < result.size(); i++) {
            if (answerList.contains(guessArr[i]) && result.get(i).equals("_")) {
                // if there is a num that is in the wrong place and if there is still the number left in the answer (list)
                result.set(i, "?");
            }
        }
        return result.stream().reduce("", (x, y) -> x + y + " ");
    }

    private boolean checkWin(String result) {
        return !(result.contains("_") || result.contains("?"));
    }

    private void writeToFile(String guess, String result) {
        try (FileWriter fw = new FileWriter(db,true); BufferedWriter bw = new BufferedWriter(fw)) {
            bw.append(guess + "," + result);
            bw.newLine();
            bw.flush();

        } catch (IOException e) {
            System.err.println("Error writing to file. Please restart.");

        }
    }

    public void play() {
        System.out.println("Enter your guess as a single input (e.g. 1234). You'll have 10 tries to guess the correct combination. ");
        System.out.println("Valid input includes any of the following numbers: " + VALID_INPUT);
        System.out.println("There are " + NUM_OPTIONS + " blanks to guess.");
        System.out.println();

        Intelligence ai = new Intelligence(db, VALID_INPUT, NUM_OPTIONS);

        Console cons = System.console();
        while (!isWon && tries <= 10) {
            String guess = cons.readLine("Enter a guess: ").trim();
            int filtered = Math.toIntExact(Arrays.stream(guess.split("")) //filters out any invalid input
                .filter(s -> VALID_INPUT.contains(s))
                .count());
            if (filtered != NUM_OPTIONS) { // ensures that there are exactly 4 valid inputs
                System.err.println("Invalid input. Please try again.");
                continue;
            }
            tries++;
            Integer[] guessArr = Arrays.stream(guess.split(""))
                .map(str -> Integer.valueOf(str))
                .toArray(s -> new Integer[s]);
            
            // Check which position is correct or wrongly placed
            String result = checkPlacement(guessArr);

            writeToFile(guess, result);
            
            // Check if user wants help before providing help
            if (provideHelp)
                System.out.println("Suggested guess: " + ai.suggestGuess());

            if (checkWin(result)) {
                isWon = true;
                continue;
            }

            System.out.println("Result: " + result);
            System.out.println();
        }

        String ans = "";
        for (int i : answer) {
            ans += i;
        }

        if (isWon) {
            System.out.println("Congratulations! You guess the correct combination: " + ans);
        } else {
            System.out.println("You ran out of tries...");
            System.out.println("The correct answer is: " + ans);
        }
    }

    public static void printMenu() {
        System.out.println("=================== MENU ===================");
        System.out.println("play: play game.");
        System.out.println("quit: quit program.");
        System.out.println("============================================");
        System.out.println();
    }

    public static void main(String[] args) throws IOException{
        Console cons = System.console();
        String input = "";
        while (!input.equals("quit")) {
            printMenu();
            input = cons.readLine("> ");
            if (input.equals("play")) {
                System.out.println("Please enter a file to save your data. \nPlease note that all data within the save file will be overwritten.");
                input = cons.readLine("Enter a file name: ");
                String f = "data" + File.separator + input;
                System.out.println("Would you like some help while playing? (Y/N)");
                input = cons.readLine("> ");
                System.out.println();
                Mastermind m = new Mastermind(f, true);
                if (input.equals("N"))
                    m = new Mastermind(f,false);
                m.play();
            } else if (!input.equals("quit")) {
                System.err.println("Unrecognised command. ");
            }
            System.out.println();
            System.out.println("Play again?");
        }
    }
}
