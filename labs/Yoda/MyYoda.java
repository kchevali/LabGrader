import java.util.*;

class MyYoda {
    public static void main(String[] args) {
        String message;
        System.out.println("Translate an English sentence of up to 4 words to Yoda Speak");
        System.out.println("Type 'yoda' to quit.");
        Scanner scan = new Scanner(System.in);
        do {
            System.out.print("English:");
            message = scan.nextLine().trim();
            while (message.contains("  "))
                message = message.replaceAll("  ", " ");
            if (!message.equalsIgnoreCase("yoda")) {
                String yoda = English2Yoda(message);
                if (yoda != null)
                    System.out.println("Yoda:" + yoda);
            }
        } while (!message.equalsIgnoreCase("yoda"));
    }

    public static String[] parse(String message) {
        return message.split(" ");
    }

    public static String join(String[] arr) {
        StringBuilder b = new StringBuilder();
        if (arr.length > 0) {
            b.append(arr[0]);
            for (int i = 1; i < arr.length; i++)
                b.append(" ").append(arr[i]);
        }
        return b.toString();
    }

    public static void swap(String[] arr, int a, int b) {
        String t = arr[a];
        arr[a] = arr[b];
        arr[b] = t;
    }

    public static String English2Yoda(String message) {
        String[] parts = parse(message);
        if (parts[0].length() == 0) {
            System.out.println("Please enter a message!");
            return null;
        } else if (parts.length > 4) {
            System.out.println("Translation Error!");
            return null;
        }
        if (parts.length == 4) {
            swap(parts, 0, 2);
            swap(parts, 1, 3);
        } else {
            String last = parts[parts.length - 1];
            for (int i = parts.length - 1; i > 0; i--)
                parts[i] = parts[i - 1];
            parts[0] = last;
        }
        return join(parts);
    }

}