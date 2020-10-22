import java.util.*;

class MyDaysMonth {
    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print("Enter a month (1-12):");
        int month = getInt(1, 12);
        System.out.print("Enter a year:");
        int year = getInt(0, Integer.MAX_VALUE);
        System.out.println(new DaysMonth(month, year));
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

class DaysMonth {
    int month, year;
    static int[] days = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    public DaysMonth(int month, int year) {
        this.month = month;
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int numberOfDays() {
        return days[month - 1] + (month == 2 && isLeapYear() ? 1 : 0);
    }

    public boolean isLeapYear() {
        return (year % 400) == 0 || ((year % 4) == 0 && (year % 100) != 0);
    }

    public String toString() {
        return numberOfDays() + " days";
    }

}