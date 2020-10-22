import java.util.HashMap;
import java.util.Map.Entry;
import java.io.FileNotFoundException;
import java.util.ArrayList;

class Settings {

    public static String settingsFileLocation = "../settings.txt";
    public static String labFileBaseLocation;
    public static HashMap<String, String> settingsMap = new HashMap<>();
    public static String labName, repoBase, labDate, filePaths, repoPath, manualPath, zipPath;
    public static String[] labNames, labDates;
    public static boolean isDebug, isManual, allowNonJava;
    public static int gracePeriod, labIndex, latestLabIndex;
    public static ArrayList<String> studentNames, repoNames;

    public static void loadSettings() {
        // load map
        settingsMap = Scanner.loadHashMap(settingsFileLocation, ":");

        // load vars
        isManual = Boolean.parseBoolean(settingsMap.get("//manual"));
        allowNonJava = !Boolean.parseBoolean(settingsMap.get("//ignore_nonjava"));
        labName = getString("lab_name");
        labNames = getStringArray("all_labs");
        repoBase = getString("repo_base");
        repoPath = getString("repo_path");
        zipPath = getString("zip_path");
        manualPath = getString("manual_path");
        labDates = getStringArray("lab_dates");
        labIndex = getLabIndex();
        latestLabIndex = labNames.length - 1;
        labDate = labIndex >= 0 ? labDates[labIndex] : null;
        gracePeriod = getInt("grace_period");
        labFileBaseLocation = "../Labs/" + labName + "/";
        filePaths = getFilePaths();

        // HashMap<String, String> shortcutMap = new HashMap<>();
        // Scanner.loadHashMap(labFileBaseLocation + "shortcut.txt", ":");
        // mergeMap(shortcutMap);

        if (isDebug = Boolean.parseBoolean(settingsMap.get("//debug")))
            printlnB("[DEBUG MODE]");

        if (manualPath.equals("none")) {
            manualPath = null;
        }
        File.loadSystemFiles();
        Storage.loadStorage();
        loadNames();
    }

    public static void mergeMap(final HashMap<String, String> map) {
        for (final Entry<String, String> entry : map.entrySet())
            settingsMap.put(entry.getKey(), entry.getValue());
    }

    private static String getString(final String s) {
        final String res = settingsMap.get("//" + s);
        if (res == null) {
            Log.write("Settings: Cannot load String key - " + s);
            System.exit(1);
        }
        return res;
    }

    private static String[] getStringArray(final String s) {
        return getString(s).split(",");
    }

    private static int getInt(final String s) {
        final String raw = getString(s);
        try {
            return Integer.parseInt(raw);
        } catch (final Exception e) {
            printlnB("Settings: Invalid int - " + s + ": " + raw);
            System.exit(1);
        }
        return 0;
    }

    private static double getDouble(final String s) {
        final String raw = getString(s);
        try {
            return Double.parseDouble(raw);
        } catch (final Exception e) {
            printlnB("Settings: Invalid double - " + s + ": " + raw);
            System.exit(1);
        }
        return 0;
    }

    private static int getLabIndex() {
        for (int i = 0; i < labNames.length; i++)
            if (labName.equals(labNames[i])) {
                if (i >= labDates.length) {
                    printlnB("Settings: Missing date in txt");
                    System.exit(1);
                }
                return i;
            }
        if (!isManual) {
            // Thread.dumpStack();
            printlnB("Settings: add '" + labName + "' to labNames to txt");
            System.exit(1);
        }
        return -1;
    }

    public static boolean containsMap(final String key) {
        return settingsMap.containsKey(key);
    }

    private static String getFilePaths() {
        final String raw = getString("file_paths");
        if (!File.create(raw).exists()) {
            printlnB("Settings: Cannot Load File Paths");
            System.exit(1);
        }
        return raw;
    }

    public static void setLabIndex(int labIndex) {
        if (Settings.labIndex != labIndex) {
            // System.out.println("Changing lab to: " + labIndex);
            setSetting("lab_name", labNames[labIndex]);
        }
    }

    // Set Setting to the file
    public static void setSetting(String key, final String value) {
        setSettingEntry(key, value);
        storeSetting();
    }

    public static void setSettingEntry(String key, final String value) {
        settingsMap.put("//" + key, value);
    }

    public static void storeSetting() {
        final ArrayList<String> lines = Scanner.getLines(settingsFileLocation);
        final StringBuilder b = new StringBuilder();
        for (final String line : lines) {
            String key = line.split(":")[0];
            b.append(settingsMap.containsKey("//" + key) ? key + ":" + settingsMap.get("//" + key) : line).append("\n");
        }
        try {
            final PrintWriter writer = PrintWriter.createWriter(settingsFileLocation);
            writer.println(b.toString());
            writer.close();
        } catch (final Exception e) {
            printlnB("Settings: Cannot store settings");
        }
        loadSettings();
    }

    public static void println(final Object o, String code) {
        print(o + code + "\n");
    }

    public static void println(final Object o) {
        print(o + "\n");
    }

    public static void print(final Object o, String code) {
        print(o + code);
    }

    public static void print(final Object o) {
        System.out.print(o + "\u001b[0m");
    }

    public static void printI(final Object o) {
        print(ITALICS + o);
    }

    public static void printB(final Object o) {
        print(BOLD + o);
    }

    public static void printlnI(final Object o) {
        println(ITALICS + o);
    }

    public static void printlnB(final Object o) {
        println(BOLD + o);
    }

    static final String RED = "\u001b[31m";
    static final String GREEN = "\u001b[32m";
    static final String YELLOW = "\u001b[33;1m";
    static final String BLUE = "\u001b[34m";
    static final String MAGENTA = "\u001b[35m";
    static final String CYAN = "\u001b[36m";
    static final String GRAY = "\u001b[30;1m";
    static final String PINK = "\u001b[31;1m";
    static final String LIME = "\u001b[32;1m";
    static final String BACK_RED = "\u001b[41m";
    static final String BACK_GREEN = "\u001b[42m";
    static final String BACK_YELLOW = "\u001b[43m";
    static final String BACK_BLUE = "\u001b[44m";
    static final String BACK_WHITE = "\u001b[47m";
    static final String BOLD = "\u001b[1m";
    static final String UNDERLINE = "\u001b[4m";
    static final String REV = "\u001b[7m";
    static final String ITALICS = "\u001b[3m";

    public static void clearFile(final String fileName) {
        try {
            PrintWriter.createWriter(fileName).close();
        } catch (FileNotFoundException e) {
            printlnB("Settings: Could not clear: " + fileName);
        }
    }

    private static void loadNames() {
        ArrayList<String> lines = Scanner.getLines(File.findAbsolute("names.txt"));
        studentNames = new ArrayList<>();
        repoNames = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(":");
            if (parts.length > 1 && parts[1].length() > 0) {
                String name = parts[0], repoName = parts.length < 2 ? null : parts[1];
                studentNames.add(name);
                repoNames.add(repoName);
            } else if (parts.length > 0) {
                // Settings.printlnB(Settings.RED + "Settings: invalid name and repo - '" + line
                // + "'");
            }
        }
    }
}