import java.io.InputStream;
import java.util.zip.*;
import java.util.Enumeration;
import java.nio.file.*;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.HashMap;

class File implements Comparable<File> {

    static HashMap<String, String> zipSource = new HashMap<>();
    static HashSet<String> gradedFiles;
    public java.io.File myFile;

    static String[] noSearchDirectories = { ".git" };

    public File() {//
        myFile = null;
    }

    public File(String path) {//
        myFile = new java.io.File(File.findAbsolute("StudentFile.txt"));
    }

    public static File create(String path) {
        if (path.length() == 0)
            return null;
        File file = new File();
        file.myFile = new java.io.File(path);
        return file;
    }

    public static File create(java.io.File file) {
        return file == null ? null : create(file.getAbsolutePath());
    }

    public boolean exists() {
        return myFile.exists();
    }

    public boolean createNewFile() throws IOException {
        return myFile.createNewFile();
    }

    public long lastModified() {
        return myFile.lastModified();
    }

    public long length() {
        return myFile.length();
    }

    public File[] listFiles() {
        try {
            java.io.File[] files = myFile.listFiles();
            File[] myFiles = new File[files.length];
            for (int i = 0; i < files.length; i++)
                myFiles[i] = create(files[i]);
            return myFiles;
        } catch (NullPointerException e) {
            Settings.printlnB(Settings.RED + "No files to list: " + this.getName());
            e.printStackTrace();
            return new File[0];
        }

    }

    public String getSafeName() {
        return safeString(getName());
    }

    public String getName() {
        return myFile.getName();
    }

    public String getSameName() {
        return myFile.getName();
    }

    public String getBaseName() {
        String name = myFile.getName();
        return name.contains(".") ? name.split("\\.")[0] : name;
    }

    /**
     * returns the unique ending of the directory and the fileName
     * 
     * @param directoryRoot
     * @return
     */
    public String getUniqueName(String directoryRoot) {
        return getAbsolutePath().contains(directoryRoot) ? getAbsolutePath().split(directoryRoot)[1]
                : getAbsolutePath();
    }

    public int compareTo(File other) {
        return myFile.compareTo(other.myFile);
    }

    public boolean isDirectory() {
        return myFile.isDirectory();
    }

    public boolean isFile() {
        return myFile.isFile();
    }

    public String getAbsolutePath() {
        return removeBackstep(myFile.getAbsolutePath());
    }

    public String getSafeAbsolutePath() {
        return safeString(getAbsolutePath());
    }

    public String safeString(String s) {
        return s.replaceAll(" ", "\\\\ ");
    }

    public void delete() {
        myFile.delete();
    }

    public boolean contains(String s) {
        String[] nameParts = myFile.getName().split("\\.");
        return nameParts[nameParts.length - 1].equalsIgnoreCase(s);
    }

    public static String getNewFileName(String path, String ext) {
        char ch = 'a';
        File file;
        do {
            file = create(path + (ch++) + "." + ext);
        } while (file.exists());
        return file.getSafeAbsolutePath();
    }

    public ArrayList<File> searchNotGradedByExt(String ext) {
        // System.out.println("Searching for " + ext + " at " + getName());
        ArrayList<File> javaFiles = new ArrayList<>();
        if (isFile()) {
            if ((contains(ext) || (ext == null && !getName().contains("."))) && !hasBeenGraded())
                javaFiles.add(this);
        } else if (isDirectory()) {
            for (String directory : noSearchDirectories) {
                if (directory.equals(getName())) {
                    return javaFiles;
                }
            }
            File[] files = this.listFiles();
            for (File file : files) {
                javaFiles.addAll(file.searchNotGradedByExt(ext));
            }
        } else if (contains("zip") && !hasBeenGraded()) {
            ZipFile zipFile = null;
            try {
                zipFile = new ZipFile(getAbsolutePath());
            } catch (IOException e) {
                Settings.printlnB(Settings.RED + "Zip Error: Cannot open zip - " + getName());
                System.exit(1);
            }

            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File info = File.create(entry.getName());
                // System.out.println("Zip File: " + info.getName());
                if (info.contains(ext)) {
                    InputStream stream;
                    try {
                        stream = zipFile.getInputStream(entry);
                    } catch (ZipException e) {
                        Settings.printlnB(Settings.RED + "Zip Error: Invalid format - cannot read zip - " + getName());
                        continue;
                    } catch (IOException e) {
                        Settings.printlnB(Settings.RED + "Zip Error: Cannot read zip - " + getName());
                        continue;
                    }

                    String zipPath = Settings.zipPath + "/";
                    String pathString = zipPath + getBaseName() + "-" + info.getName();
                    // System.out.println("Zip Path String: " + pathString);
                    Path path = Paths.get(pathString);
                    // int counter = 1;
                    // while (Files.exists(path)) {
                    // pathString = zipPath + counter++ + info.getName();
                    // path = Paths.get(pathString);
                    // }
                    try {
                        Files.copy(stream, path);
                    } catch (IOException e) {
                        Settings.printlnB(
                                Settings.RED + "Zip Error: Cannot copy zip " + getName() + " to " + pathString);
                        e.printStackTrace();
                        // continue;
                        System.exit(1);
                    }

                    File unzipped = File.create(pathString);
                    javaFiles.add(unzipped);
                    // System.out.println("ADDED UNZIPPED: " + unzipped.getName());
                    // file.getUniqueName(Settings.repoBase)
                    zipSource.put(unzipped.getSafeAbsolutePath(), getUniqueName(Settings.repoBase));
                }

            }

            try {
                zipFile.close();
            } catch (IOException e) {
                Settings.printlnB(Settings.RED + "Zip Error: Cannot close zip - " + getName());
            }
        }
        Collections.sort(javaFiles);
        return javaFiles;
    }

    public static String verifyZippedPath(String path) {
        return zipSource.containsKey(path) ? zipSource.get(path) : path;
    }

    public static void loadGradedAssignments() {
        gradedFiles = new HashSet<>(Scanner.getLines(File.findAbsolute("GradedFiles.txt")));
    }

    public File getParentFile() {
        return create(myFile.getParentFile());
    }

    public boolean hasBeenGraded() {
        return gradedFiles.contains(getUniqueName(Settings.repoBase));
    }

    // =====================================================================
    // System Files
    // =====================================================================

    private static ArrayList<String> paths;
    private static HashMap<String, String> systemPaths = new HashMap<>();
    private static boolean isLoaded = false;

    public static void loadSystemFiles() {
        String labName = Settings.labName;

        paths = Scanner.getLines(Settings.filePaths);
        paths.add("../");
        paths.add("../semesters/");

        // adding lab directory to all paths
        int length = paths.size();
        for (int i = 0; i < length; i++) {
            paths.add(paths.get(i) + labName + "/");
        }

        // adding ../ to all paths
        length = paths.size();
        for (int i = 0; i < length; i++) {
            paths.add("../" + paths.get(i));
        }
        paths.add("");// file in root
        isLoaded = true;
    }

    // checks for ../
    // EX: path/to/dir + ../src = path/to/src
    public static String removeBackstep(String path) {
        LinkedList<String> list = new LinkedList<>();
        String[] dirs = path.split("/");
        for (int i = 0; i < dirs.length; i++) {
            String dir = dirs[i];
            if (dir.equals("..") && !list.isEmpty()) {
                list.pollLast();
            } else {
                list.add(dir);
            }
        }
        StringBuilder b = new StringBuilder();
        if (!list.isEmpty())
            b.append(list.poll());
        while (!list.isEmpty())
            b.append("/").append(list.poll());
        return b.toString();
    }

    public static String find(String fileName) {
        if (!isLoaded)
            Settings.loadSettings();
        String cache = systemPaths.get(fileName);
        if (cache != null)
            return cache;

        for (String path : paths) {
            String complete = path + fileName;
            File file = create(complete);
            if (file.exists()) {
                systemPaths.put(fileName, complete);
                return complete;
            }
        }
        Settings.printlnB(Settings.RED + "File: Could not find: " + fileName);
        System.exit(1);
        return null;
    }

    public static String findAbsolute(String fileName) {
        File file = findFile(fileName);
        return file == null ? null : file.getAbsolutePath();
    }

    public static File findFile(String fileName) {
        String path = find(fileName);
        return path == null ? null : create(path);
    }

    public static String findInLabDirectory(String fileName) {
        return find(Settings.labName + "/" + fileName);
    }

}