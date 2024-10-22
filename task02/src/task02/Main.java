package task02;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Main {

    /* 
     * Iterative method to generate permutations
     * 
     */

    public static Set<String> generate(String s) {
        Set<String> set = new HashSet<>();
        if (s.length() == 1) {
            set.add(s);
            return set;
        }
        for (int i = 0; i < s.length(); i++) {
            String root = s.substring(i, i+1);
            String rest = s.replaceFirst(root, "");
            List<String> temp = generate(rest).stream()
                .map(str -> root + str)
                .toList();
            set.addAll(temp);
        }
        return set;
    }

    public static void main(String[] args) {
        // Console cons = System.console();
        // String input = cons.readLine("Enter a string of numbers: ");
        // String temp = input.replaceAll(" ", "");
        // Set<String> result = generate(temp);
        // result.stream().forEach(System.out::println);
        // System.out.println("Total number of permutations: " + result.size());

        // Threading
        // IterativeImpl impl1 = new IterativeImpl();
        // IterativeImpl impl2 = new IterativeImpl();
        // IterativeImpl impl3 = new IterativeImpl();

        // ExecutorService es = Executors.newSingleThreadExecutor(); //tasks will be run by the same thread
        // ExecutorService es = Executors.newFixedThreadPool(4);
        // es.execute(impl1);
        // es.execute(impl2);
        // es.execute(impl3);


        // MAPS
        Map<String, Integer> map = Map.of( //immutable map
            "H", 10,
            "C", 20,
            "B", 30,
            "M", 30
        );
        List<Entry<String, Integer>> mapList = new ArrayList<>();
        mapList.sort(Entry.comparingByKey());
        mapList.forEach(System.out::println);
        
        map.entrySet().stream()
            .sorted(Entry.comparingByKey())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
            .forEach((str, i) -> System.out.println(str + " " + i));
    }
}
