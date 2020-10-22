
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Scanner implements AutoCloseable {
    // isAutoMode
    // default is false, set to true at Scanner.loadAutoScanner()
    // New Scanners with default constructors use the order.txt file
    public static boolean isAutoMode = false;
    private boolean isStudentFileScan = false, isAutoScan = false;
    public boolean canReturnNull = false;// This is for buffered readers only
    private boolean isClosed = false, isRandomReader = false;
    private java.util.Scanner myScan = null;
    private static java.util.Scanner consoleScan = new java.util.Scanner(System.in);
    private InputScanner input = null;

    static String[] allCommands = { "//safe_end", "//crash", "//c", "//none", "//not_rand", "//repeat", "//pause",
            "//read_input_file", "//uml" };
    static LinkedList<String> randItems = new LinkedList(), inputItems = new LinkedList();

    public static void setAutoMode(boolean isAuto) {// Called at Run Student
        isAutoMode = isAuto;
        Math.trueRandom = isAuto;
    }

    public static void readOrderFile() {
        randItems.clear();
        inputItems.clear();
        ArrayList<String> lines = getLines(File.findInLabDirectory("order.txt"));
        for (String line : lines) {
            if (line.startsWith("//r_")) {
                randItems.addLast(line);
                // System.out.println("Random Item: " + line);
            } else {
                if (!checkForSetting(line)) {
                    inputItems.addLast(line);
                    // System.out.println("Input Item: " + line);
                }
            }

        }
    }

    public static HashMap<String, String> loadHashMap(String location, String delimiter) {
        ArrayList<String> lines = getLines(location);
        HashMap<String, String> map = new HashMap<>();
        for (String line : lines) {
            if (line.length() > 0) {
                String[] parts = line.split(delimiter);
                if (parts.length == 1 || parts[1].length() == 0) {
                    Settings.printlnB(Settings.RED + "Map Missing: '" + delimiter + "' in " + line + " at " + location);
                    return null;
                }
                map.put("//" + parts[0], parts[1]);
            }
        }
        return map;

    }

    public static void printCommands() {
        // FileTerminal.append("Commands: ");
        // for (String command : allCommands)
        // FileTerminal.append(command + ",");
        // FileTerminal.append("");
    }

    public static void loadInputs(String fileName) {
        Settings.mergeMap(loadHashMap(File.findInLabDirectory(fileName), ":"));
        String title = "--", descript = "--";
        if (Settings.settingsMap.containsKey("//title"))
            title = Settings.settingsMap.get("//title");
        if (Settings.settingsMap.containsKey("//description"))
            descript = Settings.settingsMap.get("//description");

        FileTerminal.set("*-----------------------------------*\n| Name: " + title
                + "\n*-----------------------------------*\n Description: " + descript
                + "\n*-----------------------------------*\n");
        printCommands();
        Log.debug(title);
    }

    public Scanner(InputStream in) {
        myScan = new java.util.Scanner(in);
        if (isAutoMode) {
            input = new InputScanner(inputItems);
            isAutoScan = true;
        }
    }

    public Scanner() {
    }

    public Scanner(File file) throws java.io.FileNotFoundException {
        try {
            myScan = new java.util.Scanner(File.findFile("StudentFile.txt").myFile);
            isStudentFileScan = true;
        } catch (Exception e) {
            Log.write("Error: File not found - StudentFile.txt");
        }
    }

    public Scanner(FileReader file) throws java.io.FileNotFoundException {
        try {
            myScan = new java.util.Scanner(File.findFile("StudentFile.txt").myFile);
            isStudentFileScan = true;
        } catch (Exception e) {
            Log.write("Error: File not found - StudentFile.txt");
        }
    }

    public Scanner(String s) {
        myScan = new java.util.Scanner(s);
    }

    // private since should call getLines
    private static Scanner fileReader(String location) {
        try {
            Scanner scan = new Scanner();
            scan.myScan = new java.util.Scanner(File.create(location).myFile);
            return scan;
        } catch (Exception e) {
            // e.printStackTrace();
            if (location.contains("\\ ")) {
                return fileReader(location.replaceAll("\\\\ ", " "));
            }
            Settings.printlnB(Settings.RED + "Scanner: Cannot find file at '" + location + "'");
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public static Scanner randomReader() {
        Scanner scan = new Scanner();
        scan.input = new InputScanner(randItems);
        scan.myScan = new java.util.Scanner(System.in);
        scan.isAutoScan = isAutoMode;
        scan.isRandomReader = true;
        return scan;
    }

    public String peekNext() {
        return isAutoScan ? input.peekFirst() : null;
    }

    public String peekProcessed() {
        if (!isAutoScan)
            return null;
        String next = process(input.pollFirst());
        input.addFirst(next);
        return next;
    }

    private String readNext(boolean isToken) {
        if (isClosed)
            throw new IllegalStateException();
        if (!hasNext()) {
            if (canReturnNull)
                return null;
            if (isAutoScan) {
                Settings.printlnB(Settings.RED + "Ran out of input!");
                System.out.print("\nRun: ");
                Settings.printlnB(Settings.RED + "FAILED");
                System.exit(0);
            }

        }
        if (isAutoScan) {
            // Settings.printlnI(Settings.GRAY + "Inputting: " + input.peekFirst());
            return process(input.pollFirst());
        }
        if (isStudentFileScan) {
            String line = isToken ? myScan.next() : myScan.nextLine();
            Log.writeOnly("Retrieve from file: " + line);
            FileTerminal.append("Retrieve from file: " + line);
            return process(line);
        }
        return isToken ? myScan.next() : myScan.nextLine();
    }

    public String nextLine() {
        String line = readNext(false);
        if (isAutoScan && !isRandomReader)
            Settings.printlnI(Settings.GRAY + line);
        return line;
    }

    public String next() {
        String line = readNext(true);
        if (isAutoScan && !isRandomReader)
            Settings.printlnI(Settings.GRAY + line);
        return line;
    }

    public int nextInt() {
        return Integer.parseInt(next());
    }

    public long nextLong() {
        return Long.parseLong(next());
    }

    public double nextDouble() {
        return Double.parseDouble(next());
    }

    public float nextFloat() {
        return Float.parseFloat(next());
    }

    public boolean nextBoolean() {
        return Boolean.parseBoolean(next());
    }

    public boolean hasNext() {
        return isAutoScan ? !input.isEmpty() : myScan.hasNext();
    }

    public boolean hasNextLine() {
        return isAutoScan ? !input.isEmpty() : myScan.hasNextLine();
    }

    public boolean hasNextInt() {
        return isAutoScan ? hasNext() && peekProcessed().matches("-?\\d+") : myScan.hasNextInt();
    }

    public boolean hasNextDouble() {
        return isAutoScan ? hasNext() && peekProcessed().matches("-?\\d+\\.?\\d*") : myScan.hasNextDouble();
    }

    public boolean hasNextFloat() {
        return hasNextDouble();
    }

    public void close() {
        // myScan.close();
        // isClosed = true;
    }

    public String process(String in) {
        // System.out.println("Processing: " + in);
        if (checkForCommand(in))
            return readNext(true);
        String shortcut = processShortcut(in);
        return shortcut == null ? in : shortcut;
    }

    public String processShortcut(String in) {
        // System.out.println("Processing shortcut: " + in);
        if (Settings.settingsMap == null)
            return null;
        String[] parts = in.split("//");
        if (parts.length <= 1)
            return null;

        String shortcut = "";
        if (!Settings.containsMap(in)) {
            FileTerminal.push();
            Settings.printlnI("Request instuction for: " + in);
            shortcut = nextConsoleLine();
            shortcut = shortcut.length() == 0 ? "//none" : shortcut;
            Settings.settingsMap.put(in, shortcut);
            FileTerminal.append("Stored: '" + shortcut + "' for '" + in + "'");
        } else {
            shortcut = Settings.settingsMap.get(in);
        }

        String next = process(shortcut);
        next = next == null ? shortcut : next;
        // FileTerminal.append("Entering: " + in + ": " + next);

        return next;

    }

    public static boolean checkForSetting(String in) {
        switch (in) {
            case "//not_rand":
                Math.trueRandom = false;
                return true;
            // Use(one per line): '//repeat' '#' 'instruction'
            case "//read_input_file":
                Settings.printlnB("Loading Input File!");
                ArrayList<String> lines = getLines(File.findInLabDirectory("inputFile.txt"));
                try {
                    PrintWriter writer = PrintWriter.createWriter("StudentFile.txt");
                    for (String line : lines) {
                        writer.println(line);
                    }
                    writer.close();
                    return true;
                } catch (FileNotFoundException e) {
                    Settings.printlnB("Scanner: Could not read input file to save to StudentFile.txt");
                    System.exit(1);
                }
            case "//uml":
                if (Storage.get("uml") == null) {
                    Storage.insert("uml", "true");
                }
                return true;
            default:
                break;
        }
        return false;

    }

    public boolean checkForCommand(String in) {
        switch (in) {
            case "//safe_end":
                FileTerminal.append("\n*[[SAFE END]]");
                System.out.print("\nRun: ");
                Settings.printlnI(Settings.GREEN + "Successful");
                System.exit(0);
            case "//crash":
            case "//c":
                FileTerminal.append("\n*[[MANUAL CRASH]]");
                System.out.print("\nRun: ");
                Settings.printlnB(Settings.RED + "FAILED");
                System.exit(0);
            case "//none":
                return true;
            // Use(one per line): '//repeat' '#' 'instruction'
            case "//repeat":
                if (!hasNextInt()) {
                    Settings.printlnB(Settings.RED + "Scanner: order.txt used //repeat without integer in next line");
                    System.exit(1);
                }
                int count = nextInt();
                String next = peekNext();
                for (int i = 0; i < count; i++)
                    inputItems.addFirst(next);
                return true;
            case "//pause":
                Settings.printlnB(Settings.YELLOW + "Press Enter");
                nextConsoleLine();
                return true;
            default:
                break;
        }
        return false;

    }

    public static String nextConsoleLine() {
        return consoleScan.nextLine();
    }

    public static boolean getYes() {
        return isYes(nextConsoleLine().toLowerCase());
    }

    public static boolean isYes(String line) {
        return line.length() == 0 || line.contains("y")
                || line.matches("ok|sure|sounds good|ci|gotcha|k|got it|do it|please do");
    }

    /**
     * Validates input for an int between 1 and max inclusive. also accepts empty
     * input
     * 
     * @param max max value to validate inclusive
     * @return a valid integer or 0 for empty input
     */
    public static int getInt(int max) {
        String line = nextConsoleLine();
        if (line.length() == 0)
            return 0;
        try {
            int num = Integer.parseInt(line);
            if (num > 0 && num <= max)
                return num;
        } catch (Exception e) {
        }
        Settings.printlnB(Settings.RED + "Please a enter a number between 1 and " + max);
        return getInt(max);
    }

    public static int[] getIntArray(int max, String delimiter) {
        if (max == 0)
            return new int[0];
        String line = nextConsoleLine();
        if (line.length() == 0)
            return new int[0];

        String[] parts = line.split(delimiter);
        int[] nums = new int[parts.length];
        try {
            for (int i = 0; i < nums.length; i++) {
                int num = Integer.parseInt(parts[i]);
                if (num <= 0 || num > max)
                    throw new Exception();
                nums[i] = num;
            }
            return nums;
        } catch (Exception e) {
        }
        Settings.printlnB(Settings.RED + "Vaild: 1 - " + max + " Delimiter: '" + delimiter + "'");
        return getIntArray(max, delimiter);
    }

    public static ArrayList<String> getLines(String location) {
        ArrayList<String> arr = new ArrayList<>();
        Scanner scan = Scanner.fileReader(location);
        while (scan.hasNextLine()) {
            arr.add(scan.nextLine());
        }
        scan.close();
        return arr;
    }
}