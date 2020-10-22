
public class MyRainfall {

    static int[] rain = { 72, 84, 43, 5, 2, 1, 2, 0, 3, 7, 93, 189 };
    static String[] months = { "January", "Feburary", "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December" };

    public static void main(String[] args) {
        double average = getAvg();
        System.out.println("Total snowfall: " + getTotal());
        System.out.println("The average monthly snowfall: " + average);
        System.out.println("The month with the most snow: " + months[getMostRainIndex()]);
        System.out.println("The month with the least snow: " + months[getLeastRainIndex()]);
        System.out.println("Number of months above the average: " + monthCountAbove(average));
        System.out.println("Number of months below the average: " + monthCountBelow(average));
    }

    public static int getTotal() {
        int total = 0;
        for (int data : rain) {
            total += data;
        }
        return total;
    }

    public static double getAvg() {
        return (double) getTotal() / rain.length;
    }

    public static int getLeastRainIndex() {
        int index = 0;
        for (int i = 1; i < rain.length; i++) {
            if (rain[i] < rain[index]) {
                index = i;
            }
        }
        return index;
    }

    public static int getMostRainIndex() {
        int index = 0;
        for (int i = 1; i < rain.length; i++) {
            if (rain[i] > rain[index]) {
                index = i;
            }
        }
        return index;
    }

    public static int monthCountAbove(double value) {
        int count = 0;
        for (int i = 0; i < rain.length; i++) {
            if (rain[i] > value) {
                count++;
            }
        }
        return count;
    }

    public static int monthCountBelow(double value) {
        int count = 0;
        for (int i = 0; i < rain.length; i++) {
            if (rain[i] < value) {
                count++;
            }
        }
        return count;
    }

}