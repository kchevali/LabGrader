import java.util.ArrayList;
import java.nio.channels.FileChannel;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.io.IOException;

public class _NewWeek {
    public static void main(String[] args) {
        Settings.loadSettings();
        String[] prevLabs = Settings.labNames;
        String[] prevDates = Settings.labDates;

        System.out.println("Which lab would you like to add?");
        File labFolder = File.findFile("labs");
        ArrayList<File> labFolders = new ArrayList<File>();
        for (File f : labFolder.listFiles()) {
            if (f.isDirectory() && !hasFile(prevLabs, f))
                labFolders.add(f);
        }
        for (int i = 0; i < labFolders.size(); i++) {
            System.out.println((i + 1) + ". " + labFolders.get(i).getName());
        }
        int labNumber = Scanner.getInt(labFolders.size()) - 1;

        System.out.println("Enter deadline of lab (MM/DD/YYYY)");
        String deadline = Scanner.nextConsoleLine();

        String labName = labFolders.get(labNumber).getName();
        System.out.println("Would you like to build " + labName + "?");
        if (!Scanner.getYes())
            return;

        String gradeTemplate = File.find("current/Template.txt");
        String gradeDest = File.find("current/") + labName + ".txt";
        try {
            copy(gradeTemplate, gradeDest);
        } catch (IOException e) {
            System.err.println("ERROR: Cannot copy current/Template.txt to " + gradeDest);
            return;
        }
        Settings.setSettingEntry("all_labs", appendToString(prevLabs, labName));
        Settings.setSettingEntry("lab_dates", appendToString(prevDates, deadline));
        Settings.setSettingEntry("lab_name", labName);
        Settings.storeSetting();
        System.out.println("DONE");

    }

    public static void copy(String src, String dest) throws IOException {

        ArrayList<String> lines = Scanner.getLines(src);
        PrintWriter writer = PrintWriter.createWriterPath(dest);
        for (String line : lines) {
            writer.println(line);
        }
        writer.close();
        // System.out.println("Created: " + dest);
    }

    public static boolean hasFile(String[] files, File file) {
        for (String name : files) {
            if (file.getName().equals(name))
                return true;
        }
        return false;
    }

    public static String appendToString(String[] arr, String entry) {
        StringBuilder b = new StringBuilder();
        for (String x : arr)
            b.append(x).append(",");
        String out = b.append(entry).toString();
        // System.out.println("FINAL APPEND: " + out);
        return out;
    }
}
