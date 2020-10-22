class Pause {
    public static void main(String[] args) {
        pause();
    }

    public static void pause() {
        FileTerminal.push();
        Settings.printlnB(Settings.YELLOW + "\n-Press Enter to Continue-");
        try {
            Scanner.nextConsoleLine();
        } catch (Exception e) {
            // TODO: handle exception
        }

        // scan.close();
    }
}