import java.util.*;
import java.io.*;

class MyFile {
    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        int index;
        do {
            System.out.println("Would you like to:");
            System.out.println("1. Enter new data");
            System.out.println("2. Display the data");
            System.out.println("3. Total the data");
            System.out.println("4. Exit program");

            do {
                while (!scan.hasNextInt()) {
                    String t = scan.next();
                }
                index = scan.nextInt();
                if (index < 1 || index > 4)
                    System.err.println("Please enter a number between 1 and 4");
            } while (index < 1 || index > 4);

            PrintWriter w;
            Scanner s;
            File file;
            switch (index) {
            case 1:
                System.out.println("How many weeks of data?");
                int weeks;

                do {
                    while (!scan.hasNextInt()) {
                        String t = scan.next();
                        System.err.println("Invalid value for weeks!");
                    }
                    weeks = scan.nextInt();
                    if (weeks <= 0)
                        System.out.println("Weeks should be greater than zero!");
                } while (weeks <= 0);

                w = new PrintWriter("sales.txt");
                for (int i = 0; i < weeks; i++) {
                    System.out.print("Week " + (i + 1) + " Figure: ");
                    while (!scan.hasNextDouble()) {
                        String t = scan.next();
                        System.err.println("Invalid data point!");
                    }
                    w.println(scan.nextDouble());
                }
                w.close();
                break;
            case 2:
                file = new File("sales.txt");
                if (!file.exists()) {
                    new PrintWriter("sales.txt").close();
                }
                s = new Scanner(file);
                while (s.hasNext()) {
                    if (!s.hasNextDouble()) {
                        System.err.println(s.next() + " is not a valid data point!");
                    } else {
                        System.out.println(s.nextDouble());
                    }
                }
                s.close();
                break;
            case 3:
                double total = 0;
                file = new File("sales.txt");
                if (!file.exists()) {
                    new PrintWriter("sales.txt").close();
                }
                s = new Scanner(file);
                while (s.hasNext()) {
                    if (!s.hasNextDouble()) {
                        System.err.println(s.next() + " is not a valid data point!");
                    } else {
                        total += s.nextDouble();
                    }
                }
                System.out.println("Total: " + total);
                s.close();
                break;
            default:
            }
        } while (index != 4);
    }
}