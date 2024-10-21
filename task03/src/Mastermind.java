
import java.io.Console;
import java.security.SecureRandom;
import java.util.*;

public class Mastermind {
    private static final String VALID_INPUT = "123456";
    private static final int NUM_OPTIONS = 4;
    private final int[] answer = new int[NUM_OPTIONS];
    private int tries;
    private boolean isWon;
    
    public Mastermind() {
        for (int i = 0; i < NUM_OPTIONS; i++) {
            Random rand = new SecureRandom();
            answer[i] = rand.nextInt(1,7);
        }
        tries = 0;
        isWon = false;
    }

    private int checkCorrectPlacement(Integer[] guessArr) {
        int num = 0;
        for (int i = 0; i < NUM_OPTIONS; i++) {
            if (guessArr[i] == answer[i]) {
                num++;
            }
        }
        return num;
    }

    private int checkWrongPlacement(Integer[] guessArr) {
        int num = 0;
        List<Integer> answerList = new ArrayList<>();
        for (int i : answer) {
            answerList.add(i);
        }

        // Check for correct positions first (should take priority)
        for (int i = 0; i < NUM_OPTIONS; i++) {
            int guess = guessArr[i];
            if (guess == answer[i]) {
                answerList.remove(answerList.indexOf(guess));
                guessArr[i] = 0;
            }
        }

        // Check if there are any mismatch in positions. While iterating, replace each correct guess in answerList
        for (int i = 0; i < NUM_OPTIONS; i++) {
            int guess = guessArr[i];
            if (answerList.contains(guess)) {
                num++;
                int index = answerList.indexOf(guess);
                answerList.set(index, 0);
            }
        }

        // for (int i = 0; i < NUM_OPTIONS; i++) {
        //     int guess = guessArr[i];
        //     if (guess == answerList.get(i)) {
        //         answerList.set(i, 0);
        //         continue;
        //     }
        //     if (answerList.contains(guess)) {
        //         num++;
        //         int index = answerList.indexOf(guess);
        //         answerList.set(index, 0);
        //     }
        // }
        return num;
    }

    public void play() {
        System.out.println("Enter your guess as a single input (e.g. 1234). You'll have 10 tries to guess the correct combination. ");
        System.out.println("Valid input: 1 - 6");

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
            
            //Check if there are any correct answers
            int correctPlace = checkCorrectPlacement(guessArr);
            int wrongPlacement = checkWrongPlacement(guessArr);

            if (correctPlace == NUM_OPTIONS) {
                isWon = true;
                continue;
            }

            System.out.println();
            System.out.println("No. of correct placements: " + correctPlace);
            System.out.println("No. of correct num but wrong position: " + wrongPlacement);
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

    public static void main(String[] args) {
        Console cons = System.console();
        String input = "";
        while (!input.equals("quit")) {
            printMenu();
            input = cons.readLine("> ");
            if (input.equals("play")) {
                Mastermind m = new Mastermind();
                m.play();
            } else if (!input.equals("quit")) {
                System.err.println("Unrecognised command. ");
            }
            System.out.println();
            System.out.println("Play again?");
        }
    }
}
