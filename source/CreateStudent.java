
import java.util.ArrayList;
import java.util.HashSet;
import java.io.FileNotFoundException;
import java.time.*;

public class CreateStudent {

    static StringBuilder b = new StringBuilder();

    public static void main(String[] args) {
        Settings.loadSettings();
        Settings.clearFile("terminal.txt");
        Settings.clearFile("StudentFile.txt");
        Settings.clearFile("log.txt");

        exec("code -r Student.java");
        String terminalPath = File.findAbsolute("terminal.txt");
        exec("code -r " + terminalPath);

        File.loadGradedAssignments();
        if (!File.create(File.findAbsolute(Settings.labName + ".txt")).exists()) {
            Settings.printlnB(Settings.RED + "Missing " + Settings.labName + ".txt in Lab folder");
            System.exit(1);
        }
        ArrayList<File> pathFiles = getPathFiles();
        if (pathFiles.size() == 0 || pathFiles.get(0) == null) {
            ArrayList<File> progessFiles = Grader.readFilesInProgress();
            if (progessFiles.size() == 0) {
                Settings.printlnB(Settings.RED
                        + "CreateStudent: Cannot run previous Student.java if Progress Files are cleared!");
                System.exit(1);
            }
            Grader.getName(false);// prints Name
            Settings.printlnI(Settings.BLUE + "Running previous files:");
            for (File file : progessFiles) {
                Settings.printlnI("\t" + file.getName());
                Log.analyzeFile(file);
            }
            Log.pushAnalysis();
            Settings.printlnI("");
            saveClassNames();
            return;
        }
        try {
            HashSet<String> filesInProgressSet = new HashSet<>();

            for (File file : pathFiles) {
                String pathName = file.getUniqueName(Settings.repoBase);
                b.append("//Path Name: ").append(pathName).append("\n");
                Log.analyzeFile(file);

                String zipFilePath = File.verifyZippedPath(pathName);
                if (!filesInProgressSet.contains(zipFilePath)) {
                    Storage.insert("progressfiles", zipFilePath);
                    filesInProgressSet.add(zipFilePath);
                }

            }
            Log.pushAnalysis();
            // Mark these paths as in progress for Grader

            ArrayList<String> imports = Scanner.getLines(File.findAbsolute("Imports.txt"));
            for (String imp : imports) {
                if (imp.startsWith("//"))
                    break;
                b.append("\n").append(imp);
            }
            b.append("\n\n");
            readFiles(pathFiles);

            // save to Student.java
            PrintWriter writer = PrintWriter.createWriter("Student.java");
            writer.println(b.toString());
            writer.close();

            saveClassNames();

            Settings.printlnB(Settings.GREEN + "Done!");
        } catch (FileNotFoundException e) {
            Settings.printlnB(Settings.RED + "Cannot find: Student.java");
        }
    }

    public static void readFiles(ArrayList<File> files) {
        // String[] pathParts = files.get(0).getAbsolutePath().split("/");
        // String mainFileName = pathParts[pathParts.length - 1].split("\\.")[0];
        // Settings.printlnI("Main File Name: " + mainFileName);
        String mainFileName = null;
        for (int i = 0; i < files.size(); i++) {
            String pathName = files.get(i).getSafeAbsolutePath();
            mainFileName = readFile(pathName, mainFileName, files.size());
        }
    }

    // Reads from Student.java
    public static void saveClassNames() {
        ArrayList<String> lines = Scanner.getLines(File.findAbsolute("Student.java"));
        int bracket = 0;
        for (String line : lines) {
            // System.out.println("Line: " + line);

            if (bracket == 0 && line.contains("class")) {
                String[] tokens = line.split(" ");
                for (int i = 0; i < tokens.length; i++) {
                    if (tokens[i].equals("class") && i < tokens.length - 1) {
                        // System.out.println("FOUND: " + tokens[i + 1]);
                        Storage.insert("classnames", tokens[i + 1]);
                    }
                }
            }
            bracket += occurenceCount(line, '{');
            bracket -= occurenceCount(line, '}');
        }
    }

    public static boolean inQuotes(String line, String search) {
        if (line == null || search == null || search.length() >= line.length())
            return false;
        boolean in = false;
        for (int i = 0; i < line.length() - search.length(); i++) {
            if (line.charAt(i) == '"')
                in = !in;
            else if (in && line.substring(i, i + search.length()).equals(search)) {
                return true;
            }
        }
        return false;
    }

    // return the mainFileName
    // arg: mainFileName - null -> isMainFile
    public static String readFile(String path, String mainFileName, int pathCount) {
        int bracket = 0;
        ArrayList<String> lines = Scanner.getLines(path);
        for (String line : lines) {
            String formatLine = line.replaceAll(" ", "").toLowerCase();
            if (bracket > 0) {
                if (mainFileName != null && !inQuotes(line, mainFileName)
                        && !line.matches(".*[a-zA-Z0-9]" + mainFileName + ".*"))
                    line = line.replaceAll(mainFileName, "Student");

                // ------Comments out: nextLine() buffers---------
                // if (formatLine.contains(".nextline();") && !formatLine.contains("=")
                // && !formatLine.contains("return")) {
                // line = "//AUTO-COMMENT - INPUT BUFFER REMOVER: " + line;
                // }
                b.append(line).append("\n");
            } else if (bracket == 0) {
                if (line.matches("\\s*(public )?class .+")) {
                    String[] tokens = line.replace("{", " {").split(" ");
                    boolean foundClassToken = false;
                    for (int i = 0; i < tokens.length; i++) {
                        // System.out.println("Token: " + tokens[i]);
                        if (tokens[i].equals("class")) {
                            foundClassToken = true;
                            if (mainFileName == null)
                                b.append("public ");
                        }

                        if (foundClassToken) {
                            if (i > 0 && tokens[i - 1].equals("class") && mainFileName == null) {
                                mainFileName = tokens[i].replaceAll("[^a-zA-Z0-9]", "");
                                tokens[i] = "Student";
                            }
                            b.append(tokens[i]).append(" ");
                        }
                    }
                    b.append("\n");
                } else if (line.contains("{")) {
                    b.append(line).append("\n");
                }
            } else {
                Settings.printlnB(Settings.RED + "Error: Bracket Unbalanced!");
            }

            bracket += occurenceCount(line, '{');
            bracket -= occurenceCount(line, '}');
        }
        return mainFileName;
    }

    public static int occurenceCount(String s, char ch) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ch)
                count++;
        }
        return count;
    }

    // public static int getNextStudentIndex(int labIndex) {
    // return getNextStudentIndex(0, labIndex);
    // }

    public static ArrayList<RepoFiles> getNotGradedFiles() {
        ArrayList<RepoFiles> allRepoFiles = new ArrayList<>();
        for (int i = 0; i < Settings.studentNames.size(); i++) {
            RepoFiles repoFiles = new RepoFiles(getRepoFolder(i), i);
            if (repoFiles.isMissingLabs() && repoFiles.hasJavaFiles())
                allRepoFiles.add(repoFiles);
        }
        return allRepoFiles;
    }

    public static ArrayList<RepoFiles> filterFiles(ArrayList<RepoFiles> repoFiles, int labIndex) {
        ArrayList<RepoFiles> out = new ArrayList<>();
        for (RepoFiles r : repoFiles) {
            if (!r.isGraded(labIndex) && r.hasJavaFiles()) {
                out.add(r);
            }
        }
        return out;
    }

    public static void deleteFolder(File folder) {
        try {
            File[] files = folder.listFiles();
            if (files != null) { // some JVMs return null for empty dirs
                for (File f : files) {
                    if (f.isDirectory()) {
                        deleteFolder(f);
                    } else {
                        f.delete();
                    }
                }
            }
            // folder.delete();
        } catch (Exception e) {
            // Nothing to delete
        }
    }

    public static ArrayList<String> exec(String command) {
        // System.out.println("Running command: " + command);
        String[] args = new String[] { "/bin/bash", "-c", command };
        try {
            ProcessBuilder pb = new ProcessBuilder(args).inheritIO();
            File file = File.create("gitDateTemp.txt");// temp file to get output
            pb.redirectOutput(file.myFile);
            Process p = pb.start();
            p.waitFor();
            ArrayList<String> lines = Scanner.getLines(file.getSafeAbsolutePath());
            file.delete();
            return lines;
        } catch (Exception e) {
            Settings.printlnB(Settings.RED + "CreateStudent: Could not perform exec() on: " + command);
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<File> getDemoPath() {
        Settings.printlnI("\nRunning DEMO");
        pickLab();
        ArrayList<File> paths = new ArrayList<>();
        paths.add(File.create(File.findAbsolute("My" + Settings.labName + ".java")));
        return paths;
    }

    public static int pickLab() {
        Settings.printlnB("Which lab?");
        String[] labNames = Settings.labNames;
        for (int j = 0; j < labNames.length; j++) {
            Settings.printlnI("  " + (j + 1) + ". " + labNames[j]);
        }
        int labIndex = Scanner.getInt(labNames.length) - 1;
        if (labIndex >= 0) {
            Settings.setLabIndex(labIndex);
            Settings.println(Settings.GRAY + "Loaded: " + Settings.labName + " as lab #" + (labIndex + 1));
        }
        return labIndex;
    }

    public static ArrayList<File> getPathFiles() {
        int labIndex = Settings.labIndex;
        Settings.println(Settings.GRAY + "Loaded: " + Settings.labName + " as lab #" + (labIndex + 1));
        deleteFolder(File.create(Settings.zipPath));

        if (!Settings.isManual) {
            _CreateSpreadSheet.loadAllGrades();
            ArrayList<RepoFiles> allRepoFiles = getNotGradedFiles();
            if (allRepoFiles.isEmpty()) {
                Settings.printlnB(Settings.YELLOW
                        + ((Grader.daysSinceDueDate(Settings.latestLabIndex, LocalDate.now()) > 0) ? "No more files!"
                                : "No more early submissions!"));
                return getDemoPath();
            }
            displayLabStatus(allRepoFiles);
            ArrayList<RepoFiles> labRepoFiles = filterFiles(allRepoFiles, labIndex);
            boolean isGradingComplete = hasAnyFiles(labRepoFiles);
            Settings.printlnI("Type 'demo' + to run practice files...");
            Settings.printlnB(
                    isGradingComplete ? "All Grading Complete!\nRun Next Unmarked File?" : "Run Next Student?");
            String line = Scanner.nextConsoleLine().toLowerCase();
            if (Scanner.isYes(line)) {
                // Run automatic student detection protocol
                Storage.clear();
                for (RepoFiles repoFiles : labRepoFiles) {
                    repoFiles.processNonJavaFiles();
                    ArrayList<File> paths = repoFiles.getPaths(isGradingComplete);
                    if (paths != null)
                        return paths;
                }
                for (RepoFiles repoFiles : allRepoFiles) {
                    repoFiles.processNonJavaFiles();
                    ArrayList<File> paths = repoFiles.getPaths(isGradingComplete);
                    if (paths != null)
                        return paths;
                }
                System.out.println(Settings.YELLOW + "No more files!");
                System.exit(1);
                return null;
            } else if (line.equals("demo")) {
                return getDemoPath();
            }
        }

        // Settings.printlnI("Last Student in File: " +
        // _CreateSpreadSheet.mostRecentEntry);
        Settings.printlnB("\n-Running Manual System- (Press Enter to run previous w/o rebuilding)");
        String location;

        if (Settings.manualPath == null) {
            Settings.printlnB("Enter Directory Path: ");
            location = Scanner.nextConsoleLine().replaceAll("\\\\", "");
        } else {
            Settings.printlnB("Found Directory Path!");
            location = Settings.manualPath;
        }

        if (location.length() == 0)
            return new ArrayList<>();
        RepoFiles repoFiles = new RepoFiles(File.create(location), -1);
        if (!repoFiles.hasFiles()) {
            Settings.printlnB(Settings.GREEN + "Grading Complete!");
            System.exit(1);
        }
        ArrayList<File> paths = repoFiles.getPaths(true);
        return paths == null ? getPathFiles() : paths;
    }

    public static File getRepoFolder(int studentIndex) {
        String repoBase = Settings.repoPath + Settings.repoBase;
        String repo = Settings.repoNames.get(studentIndex), repoPath = repoBase + repo + "/";
        File repoFolder = File.create(repoPath);
        if (!repoFolder.exists()) {
            String repoName = Settings.repoBase + repo;
            // Clone Repo
            Settings.printI("Missing repo. Do you want to clone " + repoName + "?");
            if (!Scanner.getYes())
                System.exit(1);
            String command = "cd " + Settings.repoPath + " && git clone https://github.com/CSCI2911/" + repoName
                    + ".git";
            if (exec(command) != null)
                Settings.printI("Clone Successful!");
            repoFolder = File.create(repoPath);
        }
        return repoFolder;
    }

    public static boolean hasAnyFiles(ArrayList<RepoFiles> repoFiles) {
        for (RepoFiles r : repoFiles) {
            if (r.hasFiles())
                return true;
        }
        return false;
    }

    public static void displayLabStatus(ArrayList<RepoFiles> repoFiles) {
        int total = 0;
        for (int k = 0; k < Settings.labNames.length; k++) {
            int studentCount = filterFiles(repoFiles, k).size();
            total += studentCount;
            if (studentCount > 0)
                Settings.printlnB("Lab " + Settings.labNames[k] + ": " + studentCount + " REMAINING");
        }
        Settings.printlnI(Settings.GRAY + "Total: " + total + " remaining\n");
    }

}

class RepoFiles {

    ArrayList<File> javaFiles = new ArrayList<>(), nonJavaFiles = new ArrayList<>();
    ArrayList<Integer> missingLabs = new ArrayList<>();
    int studentIndex;
    File directory;

    static String[] nonJavaExt = { "txt", "class", "docx", "doc", "pdf", "png", "jpeg", "jpg", "csv", "xls", "xlsx",
            "xlsm", "exe", null };

    public RepoFiles(File directory, int studentIndex) {
        this.studentIndex = studentIndex;
        this.directory = directory;
        if (studentIndex >= 0) {
            this.missingLabs = _CreateSpreadSheet.missingLabs(studentIndex);
        }
        javaFiles.addAll(directory.searchNotGradedByExt("java"));
        if (Settings.allowNonJava) {
            for (String ext : nonJavaExt)
                nonJavaFiles.addAll(directory.searchNotGradedByExt(ext));
        }
    }

    public boolean hasJavaFiles() {
        return javaFiles.size() > 0;
    }

    public boolean hasFiles() {
        return javaFiles.size() + nonJavaFiles.size() > 0;
    }

    public boolean isGraded(int labIndex) {
        if (studentIndex < 0) {
            // if (hasJavaFiles())
            // System.out.println("Unknown Student Found: " + javaFiles.get(0).getName());
            return false;
        }
        String name = Settings.studentNames.get(studentIndex);
        return _CreateSpreadSheet.grades.get(name).get(labIndex) != null;
    }

    public static void eraseFile(File file) {
        try {
            PrintWriter writer = new PrintWriter(new File(""));
            ArrayList<String> lines = Scanner.getLines(file.getSafeAbsolutePath());
            for (String line : lines) {
                writer.println(line);
            }
            writer.close();
            Settings.printlnI("File saved!");
        } catch (FileNotFoundException e) {
            Settings.printlnB(Settings.RED + "CreateStudent Error: Saving file failed!");
        }
    }

    public String getFileName(File file) {
        String fileName = Settings.isManual ? file.getAbsolutePath() : file.getName();
        if (Settings.isManual) {
            fileName = fileName.replaceAll("[^a-zA-Z.]", "_");
            while (fileName.contains("__"))
                fileName = fileName.replaceAll("__", "_");
        }
        int maxLength = 60;
        if (fileName.length() > maxLength) {
            fileName = fileName.substring(fileName.length() - maxLength, fileName.length());
        }
        return fileName;
    }

    public ArrayList<File> getPaths(boolean isGradingComplete) {
        Settings.printlnB(Settings.GRAY + "Directory: " + directory.getName() + "(Press Enter for more options)");

        if (Settings.allowNonJava && nonJavaFiles.size() > 0) {
            System.out.println("NON Java Files:");
            for (int j = 1; j <= nonJavaFiles.size(); j++) {
                String fileName = getFileName(nonJavaFiles.get(j - 1));
                Settings.printlnI(Settings.GREEN + "  " + (j) + ". " + fileName);
            }
        }

        System.out.println("\nJava Files:");
        for (int j = 1; j <= javaFiles.size(); j++) {
            String fileName = getFileName(javaFiles.get(j - 1));
            Settings.printlnI(Settings.BLUE + "  " + (j) + ". " + fileName);
        }
        int[] indices = Scanner.getIntArray(javaFiles.size(), " ");
        if (indices.length == 0) {
            Settings.printlnI("Press Enter to set complete");
            int labIndex = CreateStudent.pickLab();
            if (labIndex < 0) {
                setToComplete(javaFiles);// Setting all files to complete
                setToComplete(nonJavaFiles);
                return null;
            } else {
                return getPaths(isGradingComplete);
            }
        }
        ArrayList<File> paths = new ArrayList<>(indices.length);
        for (int j = 0; j < indices.length; j++) {
            paths.add(javaFiles.get(indices[j] - 1));
        }
        if (isGradingComplete) {
            int labIndex = Settings.labIndex;
            double score = studentIndex < 0 ? -1
                    : _CreateSpreadSheet.getGradeTotal(Settings.studentNames.get(studentIndex), labIndex);
            if (score < 0) {
                Settings.printlnI("This assignment has not been graded yet!");
            } else {
                Settings.printlnI("This assignment has been GRADED. Score: " + score);
                Settings.printlnB("Do you want to mark these files complete?");
                if (Scanner.getYes()) {
                    setToComplete(paths);
                    return null;
                }
            }
        }
        return paths;
    }

    public void processNonJavaFiles() {
        if (nonJavaFiles.size() > 0) {
            Settings.printlnB(Settings.YELLOW + "Non-Java File(s) DETECTED!");
            for (int j = 1; j <= nonJavaFiles.size(); j++) {
                File file = nonJavaFiles.get(j - 1);
                Settings.printlnI("Do you want to set " + file.getName() + " to StudentFile (for writing/reading)?");
                if (Scanner.getYes()) {
                    eraseFile(new File(""));// erases StudentFile.txt
                } else {
                    Settings.printlnI("Do you want to set " + file.getName() + " to complete?");
                    if (Scanner.getYes()) {
                        ArrayList<File> singleFile = new ArrayList<>();
                        singleFile.add(file);
                        setToComplete(singleFile);// erases StudentFile.txt
                    } else {
                        Settings.printlnI("No action taken");
                    }
                }
            }
            Settings.printlnB("");
        }
    }

    public void setToComplete(ArrayList<File> files) {
        if (files.size() == 0)
            return;
        StringBuilder completedFiles = new StringBuilder();
        HashSet<String> completedPaths = new HashSet<>();
        for (File file : files) {
            String path = File.verifyZippedPath(file.getUniqueName(Settings.repoBase));
            if (!completedPaths.contains(path)) {
                completedFiles.append(path).append("\n");
                completedPaths.add(path);
            }
        }
        Grader.write(completedFiles.toString(), File.findAbsolute("Gradedfiles.txt"));
        File.loadGradedAssignments();
        System.out.println("Set " + files.size() + " file(s) as complete!");
    }

    public boolean isMissingLabs() {
        return !missingLabs.isEmpty();
    }
}