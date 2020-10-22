import java.io.FileNotFoundException;
import java.util.ArrayList;

class FileTerminal {
    static PrintWriter writer = null;
    static StringBuilder buffer = new StringBuilder();
    static StringBuilder log = new StringBuilder();

    public static void push(String text) {
        append(text);
        push();
    }

    public static void push() {
        try {
            writer = PrintWriter.createWriter("terminal.txt");
            writer.print(buffer.toString());
            writer.close();
        } catch (FileNotFoundException e) {
            Settings.printlnB(Settings.RED + "FileTerminal: Cannot find terminal.txt");
            System.exit(1);
        }
    }

    public static void append(String text) {
        buffer.append(text).append("\n");
    }

    public static void set(String text) {
        logTerminal();
        buffer = new StringBuilder();
        append(text);
    }

    public static void logTerminal() {// reads terminal and pushes to log
        ArrayList<String> lines = Scanner.getLines(File.findAbsolute("terminal.txt"));
        boolean isNotes = false;
        for (String line : lines) {
            if (isNotes)
                Log.writeOnly(line);
            if (line.contains("*[[DONE]]"))
                isNotes = true;
            log.append(line).append("\n");
        }
    }

    public static void pushLog() {// write log to log
        logTerminal();
        Log.writeOnly("\nTERMINAL HISTORY\n=============================\n");
        Log.writeOnly(log.toString());
    }
}