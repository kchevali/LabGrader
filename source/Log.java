import java.util.ArrayList;

public class Log {

    static int commentCounter = 0, javaDocCounter = 0;
    static boolean useArrays = false, useRegex = false, noteEC = false, useRandom = false;

    public static void write(String message) {
        Settings.printlnB(message);
        Grader.write(message + "\n", File.findAbsolute("log.txt"));
    }

    public static void writeOnly(String message) {
        Grader.write(message + "\n", File.findAbsolute("log.txt"));
    }

    public static void debug(String message) {
        if (Settings.isDebug)
            Grader.write(message + "\n", File.findAbsolute("log.txt"));
    }

    public static void analyzeFile(File file) {
        boolean isComment = false;
        int bracket = 0;
        ArrayList<String> lines = Scanner.getLines(file.getAbsolutePath());
        Log.writeOnly(file.getName().toUpperCase() + " Comments");
        Log.writeOnly("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        for (String line : lines) {
            String formatLine = line.replaceAll(" ", "").toLowerCase();
            if (bracket > 0) {
                if (formatLine.contains("//")) {
                    commentCounter++;
                }
                if (formatLine.contains("/**")) {
                    javaDocCounter++;
                }
                if (formatLine.contains("[]") && !formatLine.contains("main(string")) {
                    useArrays = true;
                }
                if (formatLine.contains("arraylist<")) {
                    useArrays = true;
                }
                if (formatLine.contains(".matches(") || formatLine.contains(".matcher(")) {
                    useRegex = true;
                }
                if (formatLine.contains("math.random()") || formatLine.contains("=newrandom();")) {
                    useRandom = true;
                }
            } else if (bracket == 0) {
                if (line.contains("/*"))
                    isComment = true;
                if (line.contains("*/"))
                    isComment = false;
                if (formatLine.contains("extracredit")) {
                    line = line.toUpperCase();
                    noteEC = true;
                }
                if (line.contains("*/") || isComment || line.contains("//"))
                    Log.writeOnly(line);
            }
            bracket += CreateStudent.occurenceCount(line, '{');
            bracket -= CreateStudent.occurenceCount(line, '}');
        }
        Log.writeOnly("\nxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

    }

    public static void pushAnalysis() {
        Log.writeOnly("\n-PROGAM ANALYSIS-");
        Log.writeOnly("Comment Count: " + commentCounter);
        Log.writeOnly("Java Doc Count: " + javaDocCounter);
        Log.writeOnly("Use Arrays: " + useArrays);
        Log.writeOnly("Use Regex: " + useRegex);
        Log.writeOnly("Use Random: " + useRandom);
        Log.writeOnly("Extra Credit Noted: " + noteEC);
        Log.writeOnly("");
    }
}