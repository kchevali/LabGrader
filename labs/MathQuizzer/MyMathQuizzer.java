import java.util.*;

class MyMathQuizzer {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to Kevin's Super Math Program");
        System.out.println("Would you like to try +, -, *, / quiz?");
        String input;
        do {
            input = scan.next();
            if (!input.matches("\\+|\\-|\\*|/"))
                System.err.println("Please enter a valid symbol!");
        } while (!input.matches("\\+|\\-|\\*|/"));
        char op = input.charAt(0);

        int correct = 0, runs = 10;
        for (int i = 0; i < runs; i++) {
            int a = (int) (Math.random() * 10), b = (int) (Math.random() * 10), sol = 0;
            if (op == '+') {
                sol = a + b;
            } else if (op == '-') {
                if (b > a) {
                    int t = a;
                    a = b;
                    b = t;
                }
                sol = a - b;
            } else if (op == '*') {
                sol = a * b;
            } else if (op == '/') {
                sol = a;
                a *= ++b;
            }
            System.out.print("What is " + a + " " + op + " " + b + " = ");
            while (!scan.hasNextInt()) {
                System.out.println("Invalid number!");
                String t = scan.next();
            }
            int guess = scan.nextInt();
            if (guess == sol) {
                System.out.println("Correct!");
                correct++;
            } else {
                System.out.println("Wrong!");

            }
        }
        System.out.println("You got " + correct + " Correct and " + (runs - correct) + " Wrong");

    }
}