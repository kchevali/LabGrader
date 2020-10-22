import java.util.*;

class MyESP {
    static String[] fishes = { "ahi", "opah", "mahimahi", "onaga", "ono" };
    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        int right = 0, runs = 10;
        for (int i = 0; i < runs; i++) {
            System.out.println("I'm thinking of a fish");
            String user, rand = randomFish();

            do {
                user = userFish();
                if (!isValidFish(user)) {
                    System.out.println("Thats not a valid fish!");
                }
            } while (!isValidFish(user));
            if (user.equals(rand)) {
                System.out.println("RIGHT");
                right++;
            } else {
                System.out.println("WRONG");

            }
        }
        System.out.println("You got " + right + " right and " + (runs - right) + " wrong!");
    }

    public static String randomFish() {
        return fishes[(int) (Math.random() * fishes.length)];
    }

    public static String userFish() {
        return scan.nextLine().toLowerCase();
    }

    public static boolean isValidFish(String fish) {
        for (String f : fishes) {
            if (f.equals(fish))
                return true;
        }
        return false;
    }
}