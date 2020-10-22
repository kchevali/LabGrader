import java.util.ArrayList;
import java.util.HashMap;

public class Storage {
    static HashMap<String, ArrayList<String>> storage = new HashMap<>();

    public static void loadStorage() {
        ArrayList<String> lines = Scanner.getLines(File.findAbsolute("storage.txt"));
        for (String line : lines) {
            String[] parts = line.split(":");
            insertToMap(parts[0], parts[1]);
        }
    }

    public static ArrayList<String> get(String key) {
        return storage.get(key);
    }

    private static void insertToMap(String key, String content) {
        if (storage.get(key) == null) {
            storage.put(key, new ArrayList<>());
        }
        storage.get(key).add(content);
    }

    public static void insert(String key, String content) {
        Grader.write(key + ":" + content + "\n", File.findAbsolute("storage.txt"));
        insertToMap(key, content);
    }

    public static void clear() {
        storage = new HashMap<>();
        Settings.clearFile("storage.txt");
    }

}