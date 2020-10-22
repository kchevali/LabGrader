import java.util.*;
import java.io.*;

class MySpringBreak {

    static Scanner scan = new Scanner(System.in);
    static String fileName = "springbreak.txt";

    public static void main(String[] args) {
        System.out.println("What are 4 places you would like to go for vacation?");
        run();
    }

    public static void run() {
        try {
            writeFile(fileName);
            readFile(fileName);
        } catch (FileNotFoundException e) {
            System.err.println("Error! Enter a filename: ");
            fileName = scan.nextLine();
            run();
        }
    }

    public static void writeFile(String fileName) throws FileNotFoundException {
        PrintWriter w = new PrintWriter(fileName);
        for (int i = 0; i < 4; i++)
            w.println(scan.nextLine());
        w.close();
    }

    public static void readFile(String fileName) throws FileNotFoundException {
        Scanner r = new Scanner(new File(fileName));
        while (r.hasNext())
            System.out.println(r.nextLine());
    }
}