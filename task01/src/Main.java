
import java.io.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class Main {
    
    public static void main(String[] args) {

        String fName = "data/output.txt";
        if (args.length == 0) {
            System.err.println("Please use the format: java task01/data/<file name>");
            System.exit(-1);
        }
        File f = new File(fName);

        // Another method of getting date
        LocalDate ld = LocalDate.of(2024, 10, 21);
        Date d = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Product mouse = new Product(1, "mouse", "for clicking UI on screen", "Computer", Date.from(Instant.ofEpochSecond(1729474306)), 99.0f);
        Product keyboard = new Product(2, "keyboard", "device that allows alphanumerical inputs", "Computer", Date.from(Instant.ofEpochSecond(1729474306)), 235.5f);
        Product monitor = new Product(3, "15.6 inch monitor", "extended display panel", "Computer", Date.from(Instant.ofEpochSecond(1729474306)), 157.5f);
        Product huaweiPura = new Product(4, "Huawei Pura 70 Ultra", "Huawei phone", "Mobile", Date.from(Instant.ofEpochSecond(1729474306)), 900.0f);
        Product huaweiMate = new Product(5, "Huawei Mate 50 Pr", "Huawei phone", "Mobile", Date.from(Instant.ofEpochSecond(1729474306)), 1200.0f);
        Product iPhone16 = new Product(6, "iPhone 16 Pro", "iPhone", "Mobile", Date.from(Instant.ofEpochSecond(1729474306)), 2000.0f);
        Product iPhone14 = new Product(7, "iPhone 14 Pro", "iPhone", "Computer", Date.from(Instant.ofEpochSecond(1729474306)), 1800.0f);

        List<Product> products = new ArrayList<>();
        products.add(mouse);
        products.add(keyboard);
        products.add(monitor);
        products.add(huaweiPura);
        products.add(huaweiMate);
        products.add(iPhone16);
        products.add(iPhone14);
        

        System.out.println("Filtered stream =================");
        products.stream()
            .filter(x -> x.getPrice() > 1500.0f)
            .forEach(System.out::println);
        
        System.out.println("Ascending order =================");
        products.stream()
            .sorted(Comparator.comparing(Product::toString))
            .forEach(System.out::println);

        try {
            FileWriter fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);
            products.stream()
                .filter(x -> x.getPrice() > 1000.0f)
                .forEach(x -> {
                    try {
                        bw.append(x + "\n");
                    } catch (IOException e) {
                        System.err.println("Error within stream when writing to file.");
                    }
                });
            bw.flush();
            bw.close();
        } catch (IOException e) {
            System.err.println("Error writing to file.");
        }
    }
}
