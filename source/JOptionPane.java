class JOptionPane {
    static final int ERROR_MESSAGE = 0;

    public static void showMessageDialog(Object parent, Object message) {
        System.out.println("JOPTIONPANE: " + message);
    }

    public static void showMessageDialog(Object parent, Object message, String title, int messageType) {
        System.out.println("JOPTIONPANE: | " + title + " | " + message);
    }

    public static String showInputDialog(Object message) {
        System.out.println("JOPTIONPANE INPUT: " + message);
        Scanner scan = new Scanner(System.in);
        String line = scan.nextLine();
        scan.close();
        return line;
    }

    public static String showInputDialog(Object parent, Object message) {
        return showInputDialog(message);
    }
}