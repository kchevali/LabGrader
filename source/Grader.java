
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.io.IOException;
import java.time.*;
import java.time.format.*;

// import edu.nyu.cs.javagit.api.*;

public class Grader {//

    static HashMap<String, ArrayList<String>> commentMap = new HashMap<>();
    static String onTimeStatus = "On Time";

    public static void main(String[] args) {
        Settings.loadSettings();
        String outputLocation = File.findAbsolute(Settings.labName + ".txt");
        if (!File.create(outputLocation).exists()) {
            Settings.printlnB(Settings.RED + "Missing " + Settings.labName + ".txt in Lab folder");
            System.exit(1);
        }
        _CreateSpreadSheet.loadAllGrades();
        File.loadGradedAssignments();
        // Settings.printlnB(Settings.YELLOW + "Demo does not need to be graded");
        // return;
        loadComments();
        printLog();
        printCriteria();
        write(getGrades(getName(true)), outputLocation);
    }

    public static void loadComments() {
        HashMap<String, String> map = Scanner.loadHashMap(File.findAbsolute("GeneralComments.txt"), ":");
        for (String key : map.keySet()) {
            commentMap.put(key, new ArrayList<String>(Arrays.asList(map.get(key).split(","))));
        }
        commentMap.put("//Correctness", Scanner.getLines(File.findInLabDirectory("AutoComments.txt")));
    }

    public static String getName(boolean canAsk) {
        ArrayList<File> filesInProgess = readFilesInProgress();
        ArrayList<String> repoNames = Settings.repoNames;
        ArrayList<String> studentNames = Settings.studentNames;
        if (filesInProgess.size() > 0) {
            File current = filesInProgess.get(0);
            if (current != null && Settings.isManual)
                Settings.printlnI("Manual System - Main File Name: " + current.getName());
            String repoBase = Settings.repoBase;

            while (current != null) {
                String fileName = current.getName();
                if (fileName.startsWith(repoBase)) {
                    String repoName = fileName.substring(repoBase.length());
                    for (int i = 0; i < repoNames.size(); i++)
                        if (repoName.equals(repoNames.get(i))) {
                            System.out.println("Student Name: " + studentNames.get(i));
                            return studentNames.get(i);
                        }
                }
                current = current.getParentFile();
            }
        }
        // System.out.println("Getting names for GRADER");

        if (!Settings.isManual) {
            ArrayList<RepoFiles> repoFiles = CreateStudent.filterFiles(CreateStudent.getNotGradedFiles(),
                    Settings.labIndex);
            // System.out.println("Repo files:");
            // for (RepoFiles r : repoFiles) {
            // System.out.println(Settings.studentNames.get(r.studentIndex));
            // }
            if (!repoFiles.isEmpty()) {
                String name = studentNames.get(repoFiles.get(0).studentIndex);
                System.out.println("Student Name: " + name);
                return name;
            }
        }

        while (canAsk) {
            System.out.print("(Press 'Enter' for new name)\nStudent Name: ");
            String name = Scanner.nextConsoleLine().toLowerCase();
            // if (name.length() == 0) {
            // System.out.println("Enter New Name: ");
            // name = Scanner.nextConsoleLine();
            // if (name.length() > 0) {
            // write(name + "\n", File.findAbsolute("names.txt"));
            // Settings.printlnI("Creating new name: '" + name + "'");
            // return name;
            // } else {
            // Settings.printlnI("Name not saved!");
            // }
            // }
            for (String student : studentNames) {
                if (student.toLowerCase().contains(name)) {
                    System.out.println("Is the Student: " + student + "?");
                    if (Scanner.getYes())
                        return student;
                }
            }
        }
        return null;
    }

    public static void printFile(String name, String location) {
        ArrayList<String> lines = Scanner.getLines(location);
        if (lines.size() > 0) {
            System.out.println("\n-" + name + "-");
            for (String line : lines) {
                System.out.println(line);
            }
        } else {
            System.out.println("-Empty " + name + "-");
        }
        System.out.println("");
    }

    public static void fileToTerminal(String name, String location) {
        ArrayList<String> lines = Scanner.getLines(location);
        if (lines.size() > 0) {
            FileTerminal.append("\n-" + name + "-");
            for (String line : lines) {
                FileTerminal.append(line);
            }
        } else {
            FileTerminal.append("-Empty " + name + "-");
        }
        FileTerminal.append("");
    }

    public static void printLog() {
        fileToTerminal("LOG", File.findAbsolute("log.txt"));
        FileTerminal.push();
    }

    public static void printCriteria() {
        printFile("CRITERIA", File.findAbsolute("Criteria.txt"));
    }

    static String[] menuItems = { "Correctness", "Crashing", "Structure", "Comments", "Variable_Names", "Redundacy",
            "Extra_Credit" };
    static String[] altMenuItems = { "", "", "", "", "", "UML", "" };
    static double[] maxItems = { 3.0, 3.0, 1.0, 1.0, 1.0, 1.0, 0.0 };

    public static String getGrades(String name) {
        StringBuilder b = new StringBuilder("Name: ");
        b.append(name).append("\n\n");
        double total = 0.0;
        System.out.println("\nGrading:");
        String line = "";

        ArrayList<String> autoComments = new ArrayList<>();
        for (int i = 0; i < menuItems.length; i++) {
            ArrayList<String> umlList = Storage.get("uml");
            boolean isUML = umlList != null && umlList.size() > 0 && Boolean.parseBoolean(umlList.get(0));
            boolean isAlternate = altMenuItems[i].length() > 0 && (i == 5 && isUML);
            String item = isAlternate ? altMenuItems[i] : menuItems[i];
            System.out.print(item + ": ");
            try {
                line = Scanner.nextConsoleLine();
                double grade = line.length() == 0 ? 0 : Double.parseDouble(line);
                b.append(item).append(": ").append(grade).append("/").append(maxItems[i]).append("\n");
                total += grade;
                ArrayList<String> comments = commentMap.get("//" + item);
                if (grade != maxItems[i] && comments != null) {
                    int index = 1;
                    Settings.printlnI("Select comment for " + item + " (Press Enter to Skip)");
                    for (String comment : comments) {
                        Settings.printlnI((index++) + ". " + comment);
                    }
                    int[] indicies = Scanner.getIntArray(comments.size(), " ");
                    if (indicies.length > 0) {
                        for (int commentIndex : indicies) {
                            autoComments.add(comments.get(commentIndex - 1));
                        }
                        Settings.printlnB("Added Comment(s)!");
                    } else {
                        Settings.printlnB("Comment NOT Added");
                    }
                }
            } catch (java.lang.NumberFormatException e) {
                return getGrades(name);
            }
        }
        Scanner.nextConsoleLine();
        b.append("\nTotal: ").append(total);
        System.out.print("Total: " + total + "\n");
        ArrayList<File> filesInProgess = readFilesInProgress();
        String deadlineStatus = "";

        if (filesInProgess.size() == 0) {
            Settings.printlnB(Settings.RED + "Grader: Empty Files In Progress");
            System.exit(0);
        }
        deadlineStatus = getDeadlineStatus(filesInProgess);
        b.append("\nSubmission: ").append(deadlineStatus).append("\n");
        System.out.println("Comments: ");
        boolean hasComments = false;

        do {
            line = Scanner.nextConsoleLine();
            if (line.length() > 0) {
                if (!hasComments) {
                    hasComments = true;
                    b.append("\nAdditional Comments:\n");
                }
                b.append(line).append("\n");
            }
        } while (line.length() != 0);

        for (String comment : autoComments) {
            if (!hasComments) {
                hasComments = true;
                b.append("\nAdditional Comments:\n");
            }
            Settings.printlnI("Added: " + comment);
            b.append(comment).append("\n");
        }

        System.out.println("Submit?");
        if (Scanner.getYes()) {
            b.append("\n========================\n");
            markFilesAsGraded(filesInProgess);
            if (!Settings.isManual && !deadlineStatus.equals(onTimeStatus)) {
                logLateAssignment(name);
            }
            Storage.clear();
            Settings.printlnB(Settings.GREEN + "-Grade Submitted: " + deadlineStatus + "-");
            return b.toString();
        }
        Settings.printlnB(Settings.RED + "-Grade NOT Submitted-");
        return getGrades(name);

    }

    public static void logLateAssignment(String name) {
        ArrayList<String> lines = Scanner.getLines(File.findAbsolute("late.txt"));
        String lastDate = null;
        for (String line : lines) {
            if (line.matches("(\\d{2}/){2}\\d{4}")) {
                lastDate = line;
            }
        }
        LocalDate today = LocalDate.now();// For reference
        String todayDate = today.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        StringBuilder b = new StringBuilder();
        if (!todayDate.equals(lastDate)) {
            b.append("\n").append(todayDate).append("\n");
        }
        b.append(name).append(" @ Lab: ");
        if (Settings.labIndex >= 0) {
            b.append(Settings.labIndex + 1).append(" (").append(Settings.labName).append(")\n");
        } else {
            b.append(Settings.labName).append("\n");
        }
        write(b.toString(), File.findAbsolute("late.txt"));
    }

    public static LocalDate getSubmitDate(File file) {
        String command = "cd " + file.getParentFile().getSafeAbsolutePath()
                + " && git log -1 --format=%cd --date=short-local " + file.getSafeName();
        ArrayList<String> lines = CreateStudent.exec(command);
        String dateString;
        if (lines.size() == 0 || lines.get(0) == null) {
            if (Settings.isManual)
                return null;
            Settings.printlnB(Settings.RED + "Grader: Could not read timestamp of: " + file.getName());
            // System.exit(1);
            System.out.print("Enter Github submit date(yyyy-MM-dd):");
            dateString = Scanner.nextConsoleLine();
        } else {
            dateString = lines.get(0);
        }

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // System.out.println("GIT SUBMIT DATE: " + lines.get(0));
        return LocalDate.from(format.parse(dateString));

    }

    public static int daysSinceDueDate(int labIndex, LocalDate date) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate dueDate = LocalDate.from(format.parse(Settings.labDates[labIndex]));
        LocalDate graceDate = dueDate.plusDays(Settings.gracePeriod);
        Period period = Period.between(graceDate, date);
        return period.getDays() + period.getMonths() * 30 + period.getYears() * 365;
    }

    public static String getDeadlineStatus(ArrayList<File> filesInProgess) {
        if (filesInProgess.size() == 0) {
            Settings.printlnB(Settings.RED + "Submit date is unknown! Setting to '-'");
            return "-";
        }
        LocalDate submitDate = getSubmitDate(filesInProgess.get(0));
        if (submitDate == null)
            return "-";

        // System.out.println("Submit Date: " + submitDate);
        // System.out.println("Due Date: " + dueDate);
        // System.out.println("Grace Date: " + graceDate);

        int daysSinceGrace = daysSinceDueDate(Settings.labIndex, submitDate);
        // System.out.println("Days since grace: " + daysSinceGrace);
        return daysSinceGrace <= 0 ? onTimeStatus
                : ((daysSinceGrace < 7 ? (daysSinceGrace + " Day") : ((daysSinceGrace /= 7) + " Week"))
                        + (daysSinceGrace > 1 ? "s" : "") + " Late");
    }

    public static ArrayList<File> readFilesInProgress() {
        ArrayList<File> files = new ArrayList<>();
        ArrayList<String> lines = Storage.get("progressfiles");
        if (lines != null) {
            for (String line : lines) {
                File file = File.create(Settings.repoPath + Settings.repoBase + line);
                if (file != null)
                    files.add(file);
            }
        }
        return files;
    }

    public static void markFilesAsGraded(ArrayList<File> filesInProgress) {
        StringBuilder b = new StringBuilder();
        for (File file : filesInProgress) {
            b.append(file.getUniqueName(Settings.repoBase)).append("\n");
        }
        write(b.toString(), File.findAbsolute("Gradedfiles.txt"));
    }

    public static void write(String message, String location) {
        try {
            Files.write(Paths.get(location), message.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Could not write to: " + location);
        }
    }
}