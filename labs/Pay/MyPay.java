import java.util.Scanner;

class MyPay {

    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        int[] employeeIds = { 102938, 293847, 384756, 475610, 192837, 381029 };
        PayRoll payroll = new PayRoll(employeeIds.length);

        for (int i = 0; i < employeeIds.length; i++) {
            System.out.println("Enter the hourly pay rate for employee number " + employeeIds[i] + ":");
            double rate = getDouble(10.0);
            System.out.println("Enter the hours worked for employee number " + employeeIds[i] + ":");
            int hours = getInt(0);
            payroll.setPay(new Pay(employeeIds[i], hours, rate), i);
        }
        System.out.println(payroll);
    }

    public static int getInt(int min) {
        while (!scan.hasNextInt()) {
            System.err.println("ERROR: Invalid entry. Please enter an integer!");
            scan.next();
        }
        int num = scan.nextInt();
        if (num < min) {
            System.err.println("ERROR: Please enter a number greater than " + min);
            return getInt(min);
        }
        return num;
    }

    public static double getDouble(double min) {
        while (!scan.hasNextDouble()) {
            System.out.println("Invalid entry. Please enter a number!");
            scan.next();
        }
        double num = scan.nextDouble();
        if (num < min) {
            System.out.println("Please enter a number greater than " + min);
            return getDouble(min);
        }
        return num;
    }
}

class PayRoll {
    Pay[] payroll;

    public PayRoll(int n) {
        payroll = new Pay[n];
    }

    public void setPay(Pay pay, int index) {
        payroll[index] = pay;
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("PAYROLL DATA\n=======================\n");
        for (int i = 0; i < payroll.length; i++) {
            Pay pay = payroll[i];
            if (pay != null) {
                b.append(pay.toString());
                if (i < payroll.length - 1)
                    b.append("\n\n");
            }
        }
        return b.toString();
    }

    public double getGrossPay(int employeeId) throws Exception {
        for (Pay pay : payroll) {
            if (pay.getEmployeeId() == employeeId) {
                return pay.getWage();
            }
        }
        throw new Exception("Unknown employee id: " + employeeId);
    }
}

class Pay {
    private int employeeId, hours;
    private double payRate, wage;

    public Pay(int employeeId, int hours, double payRate) {
        this.employeeId = employeeId;
        this.hours = hours;
        this.payRate = payRate;
        updateWage();
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
        updateWage();
    }

    public double getPayRate() {
        return payRate;
    }

    public void setPayRate(double payRate) {
        this.payRate = payRate;
        updateWage();
    }

    public double getWage() {
        return wage;
    }

    public void updateWage() {
        this.wage = this.hours * this.payRate;
    }

    public String toString() {
        return String.format("Employee ID: %d\nGross pay: $%.2f", employeeId, wage);
    }
}