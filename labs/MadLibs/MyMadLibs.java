import java.util.*;
import java.io.*;

class MyMadLibs {
    public static void main(String[] args) throws Exception {

        String[][] rules = { { "_noun_", "pillow", "ball", "rock", "rocket", "nerf blaster", "baseball bat" },
                { "_verb_", "run", "swim", "crawl", "chase", "cry", "throw" },
                { "_pastverb_", "threw", "missed", "punched", "kicked", "bought", "hugged" },
                { "_adjective_", "blue", "messy", "powerful", "awkward", "reddish", "wholesome" },
                { "_adverb_", "quickly", "slowly", "easily", "eagerly", "gracefully", "carefully" },
                { "_location_", "my house", "school", "the beach", "the forest", "the dungeon", "Mars" },
                { "_animal_", "dog", "snake", "cat", "horse", "hobo", "monkey" },
                { "_name_", "Kevin", "John", "Abe", "Joe", "Jonah", "Bill" } };

        Scanner scan = new Scanner(new File("madlib.txt"));
        StringBuilder b = new StringBuilder();
        while (scan.hasNext())
            b.append(scan.nextLine()).append("\n");
        String text = b.toString();
        while (text.contains("_"))
            for (String[] row : rules)
                text = text.replaceFirst(row[getCode()], row[getRand(row.length - 1)]);
        System.out.println(text);
    }

    public static int getCode() {
        return 0;
    }

    public static int getRand(int length) {
        return (int) (Math.random() * length) + 1;
    }
}