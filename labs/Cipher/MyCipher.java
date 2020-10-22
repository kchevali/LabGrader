import java.util.*;

class MyCipher {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.print("Message:");
        String message = scan.nextLine();
        String upper = message.toUpperCase();

        System.out.print("\n(Enter a number for the casear cipher and a string for the vignere cipher)\nKey:");
        String key = scan.nextLine().toLowerCase();
        boolean isCasear = key.matches("[0-9]+");
        int numKey = 0;
        if (isCasear)
            numKey = Integer.parseInt(key);

        for (int i = 0, j = 0; i < message.length(); i++, j = (j + 1) % key.length()) {
            int letter = message.charAt(i);
            if (Character.isLetter(letter)) {
                int shift = isCasear ? numKey : key.charAt(j) - 'a';
                letter = ((upper.charAt(i) - 'A' + shift) % 26 + 'A') + (letter <= 'Z' ? 0 : 32);
            }
            System.out.print((char) letter);

        }
        System.out.println();

    }
}