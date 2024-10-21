package task02.src;

import java.io.Console;
import java.util.*;

public class Main {

    public static Set<String> generate(String s) {
        Set<String> set = new HashSet<>();
        if (s.length() == 1) {
            set.add(s);
            return set;
        }
        for (int i = 0; i < s.length(); i++) {
            String root = s.subSequence(i, i+1).toString();
            String rest = s.replaceFirst(root, "");
            List<String> temp = generate(rest).stream()
                .map(str -> root + str)
                .toList();
            set.addAll(temp);
        }
        return set;
    }

    public static void main(String[] args) {
        Console cons = System.console();
        String input = cons.readLine("Enter a string of numbers: ");
        String temp = input.replaceAll(" ", "");
        Set<String> set = new HashSet<>();

        set.addAll(generate(temp));
        set.stream().forEach(System.out::println);
        System.out.println("Total number of permutations: " + set.size());
    }
}
