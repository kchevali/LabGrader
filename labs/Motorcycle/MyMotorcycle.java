import java.util.*;

class MyMotorcycle {
    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print("Enter the year of your Motorcycle:");
        int year = getInt(1900, 2021);
        System.out.print("Enter the make of your Motorcycle:");
        String make = scan.next();

        Motorcycle motor = new Motorcycle(year, make);
        for (int i = 0; i < 3; i++) {
            System.out.println(motor);
            motor.acclerate();
        }
        for (int i = 0; i < 3; i++) {
            System.out.println(motor);
            motor.brake();
        }
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

}

class Motorcycle {
    int year;
    double speed = 0;
    String make;

    public Motorcycle(int year, String make) {
        this.year = year;
        this.make = make;
    }

    public Motorcycle() {
        this.year = 2020;
        this.make = "--";
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String toString() {
        return "A " + year + " " + make + " going " + speed + " miles per hour.";
    }

    public void acclerate() {
        speed += 5;
    }

    public void brake() {
        speed -= 5;
    }

}