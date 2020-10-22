import java.util.*;

class MyConversion {

    static Scanner scan = new Scanner(System.in);
    static double[] ratio = { 0.001, 39.3701, 3.28084, 1.09361 };
    static String[] names = { "kilometers", "inches", "feet", "yards" };

    public static void main(String[] args) {
        int index;
        System.out.println("Enter the number of meters");
        double meters = getDouble();
        while (true) {
            showMenu();
            index = getInt(1, getMaxIndex()) - 1;

            if (isQuit(index))
                break;
            System.out.println(meters + " meter(s) is converted to " + convert(meters, index) + " " + getName(index));

        }
    }

    public static String getName(int index) {
        return names[index];
    }

    public static double getRatio(int index) {
        return ratio[index];
    }

    public static int getMaxIndex() {
        return names.length + 1;
    }

    public static int getInt(int min, int max) {
        while (!scan.hasNextInt()) {
            System.err.println("Invalid input. Please pick a valid number!");
            scan.next();
        }
        int out = scan.nextInt();
        if (out < min || out > max) {
            System.out.println("Please enter a number between " + min + " and " + max);
            return getInt(min, max);
        }
        return out;
    }

    public static double getDouble() {
        while (!scan.hasNextDouble()) {
            System.err.println("Invalid input. Please pick a valid number!");
            scan.next();
        }
        return scan.nextDouble();
    }

    public static void showMenu() {
        System.out.println("Pick your conversion from meters:");
        for (int i = 0; i < names.length; i++) {
            System.out.println((i + 1) + ". " + getName(i));
        }
        System.out.println((names.length + 1) + ". exit\n");
    }

    public static double convert(double num, int index) {
        return num * getRatio(index);
    }

    public static boolean isQuit(int index) {
        return index == names.length;
    }

}