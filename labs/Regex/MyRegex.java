import java.util.*;

class MyRegex {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String password;
        do {
            System.out.println("\nCheck your password security:");
            password = scan.nextLine();
        } while (!isValid(password));
        System.out.println("Valid Password");
    }

    public static boolean isValid(String pass) {
        if (!pass.matches(".{8,}")) {
            System.err.println("Invalid Password: length must be atleast 8.");
            return false;
        } else if (!pass.matches(".*\\d.*\\d.*")) {
            System.err.println("Invalid Password: must include two digits.");
            return false;
        } else if (!pass.matches(".*[!#$%&\\-_].*")) {
            System.err.println("Invalid Password: must include one of the following:!#$%&-_");
            return false;
        }
        return true;
    }
}