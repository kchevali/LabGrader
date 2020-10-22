
public class Math {
    public static boolean trueRandom = true;
    private static Scanner scan = Scanner.randomReader();

    public static double random() {
        double random;
        if (trueRandom)
            random = java.lang.Math.random();
        else {
            if (!Scanner.isAutoMode)
                Settings.printlnI("*rand[0,1): ");
            random = scan.nextDouble();
        }
        FileTerminal.append("Random input: " + random);
        return random;
    }

    public static long round(double val) {
        return java.lang.Math.round(val);
    }

    public static int round(float val) {
        return java.lang.Math.round(val);
    }

    public static double pow(double val, double exp) {
        return java.lang.Math.pow(val, exp);
    }

    public static double abs(double val) {
        return java.lang.Math.abs(val);
    }

    public static int abs(int val) {
        return java.lang.Math.abs(val);
    }

    public static double floor(double val) {
        return java.lang.Math.floor(val);
    }

    public static int max(int a, int b) {
        return java.lang.Math.max(a, b);
    }

    public static int min(int a, int b) {
        return java.lang.Math.min(a, b);
    }
}