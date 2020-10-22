import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileNotFoundException;

class _CreateSpreadSheet {
    static HashMap<String, ArrayList<Grade>> grades = new HashMap<>();
    public static String mostRecentEntry;

    static int firstColumnWidth = 25, generalColumnWidth = 15;

    public static void main(String[] args) {
        Settings.loadSettings();
        loadAllGrades();
        buildAllGrades();
    }

    public static void loadAllGrades() {
        initGrades();
        loadGrades();
    }

    public static void initGrades() {
        for (String name : Settings.studentNames) {
            ArrayList<Grade> arr = new ArrayList<>();
            for (int i = 0; i < Settings.labNames.length; i++) {
                arr.add(null);
            }
            grades.put(name, arr);
        }
    }

    public static void loadGrades() {
        for (int i = 1; i <= Settings.labNames.length; i++) {
            String labName = Settings.labNames[i - 1];
            String filePath = File.findAbsolute(labName + ".txt");
            ArrayList<String> lines = Scanner.getLines(filePath);
            int labNum = -1;
            try {
                labNum = Integer.parseInt(lines.get(0).split(":")[1]);
            } catch (NumberFormatException e) {
                Settings.printlnB(Settings.RED + "Error: set lab number in: " + filePath);
                System.exit(1);
            }

            String lastName = null, firstName = null, commentLines = "", deadlineStatus = "";
            double correct = 0, crash = 0, struct = 0, comment = 0, varName = 0, redund = 0, ec = 0, total = 0;

            boolean getComments = false;
            for (int j = 1; j < lines.size(); j++) {
                String line = lines.get(j);
                if (line.length() > 0) {
                    if (line.startsWith("Additional Comments:")) {
                        getComments = true;
                    } else if (getComments) {
                        commentLines += line + "\n";
                    } else if (line.startsWith("Submission:")) {
                        deadlineStatus = line;
                    } else if (line.startsWith("====")) {
                        if (firstName != null) {
                            String key = firstName + " " + lastName;
                            mostRecentEntry = key;
                            ArrayList<Grade> gradeArr = grades.get(key);
                            if (gradeArr != null) {
                                if (gradeArr.get(i - 1) != null) {
                                    Settings.printlnB(Settings.RED + "Duplicate entry for: " + key + " @ " + labName);
                                }
                                gradeArr.set(i - 1, new Grade(firstName, lastName, correct, crash, struct, comment,
                                        varName, redund, ec, total, deadlineStatus, commentLines, i));
                            } else {
                                Settings.printlnB(Settings.RED + "Error: Cannot find name - " + key + " @ " + labName);
                            }
                        }

                        // RESET VARIABLES
                        lastName = null;
                        firstName = null;
                        commentLines = "";
                        deadlineStatus = "";
                        correct = 0;
                        crash = 0;
                        struct = 0;
                        comment = 0;
                        varName = 0;
                        redund = 0;
                        ec = 0;
                        total = 0;

                    } else {
                        String[] parts = line.split(" ");
                        if (line.startsWith("Name: ")) {
                            if (parts.length < 3) {
                                Settings.printlnB(Settings.RED + "AllGrades: Invalid Name: " + line + " @ " + labName);
                                System.exit(1);
                            }
                            firstName = parts[1];
                            lastName = parts[2];
                        } else {
                            // Settings.printlnB("Found: " + line);
                            double value = Double.parseDouble(parts[1].split("/")[0]);
                            if (line.startsWith("Correctness: ")) {
                                correct = value;
                            } else if (line.startsWith("Crashing: ")) {
                                crash = value;
                            } else if (line.startsWith("Structure: ")) {
                                struct = value;
                            } else if (line.startsWith("Comments: ")) {
                                comment = value;
                            } else if (line.startsWith("Variable_Names: ")) {
                                varName = value;
                            } else if (line.startsWith("Redundacy: ") || line.startsWith("UML: ")) {
                                redund = value;
                            } else if (line.startsWith("Extra_Credit: ")) {
                                ec = value;
                            } else if (line.startsWith("Total: ")) {
                                total = value;
                            }
                        }
                    }
                } else {
                    getComments = false;
                }
            }
        }

    }

    private static void buildAllGrades() {
        StringBuilder sb = new StringBuilder();
        StringBuilder sbCSV = new StringBuilder();// csv
        sb.append(fit(firstColumnWidth, ""));
        for (String labName : Settings.labNames) {
            sb.append(fit(generalColumnWidth, labName));
            sbCSV.append(",").append(labName);
        }
        sb.append(fit(firstColumnWidth, ""));
        sb.append(fit(generalColumnWidth, "Average") + "\n");
        sbCSV.append(",,Average\n");
        // sb.append("Completion\n");
        boolean isDashed = true;
        for (String name : Settings.studentNames) {
            StringBuilder line = new StringBuilder();
            line.append(fit(firstColumnWidth, name));// Name - 1st Column
            sbCSV.append(name).append(",");
            double total = 0;
            ArrayList<Grade> arr = grades.get(name);
            for (int i = 0; i < Settings.labNames.length; i++) {
                double t = arr.get(i) == null ? 0 : arr.get(i).total;
                boolean isLate = arr.get(i) == null ? false : arr.get(i).isLate();
                line.append(fit(generalColumnWidth, "" + t + (isLate ? "L" : "")));
                sbCSV.append(t).append(isLate ? "L" : "").append(",");
                total += t;
            }
            line.append(fit(firstColumnWidth, name));// Name - 1st Column
            sbCSV.append(name).append(",");

            double avg = (Math.round(total * 100 / arr.size()) / 100.0);
            line.append(avg).append("\n");
            sbCSV.append(avg).append("\n");
            // sb.append(Math.round(completeCount*10000 / arr.size())/100.0 + "\n");
            sb.append((isDashed = !isDashed) ? line.toString() : line.toString().replaceAll("  ", "- "));
        }
        sb.append(fit(firstColumnWidth, "\nAverage(exclude zero)") + " ");
        sbCSV.append("\nAverage(exclude zero)");
        for (int i = 0; i < Settings.labNames.length; i++) {
            double total = 0;
            int completeCount = 0;
            for (String name : Settings.studentNames) {
                Grade grade = grades.get(name).get(i);
                double t = grade == null ? 0 : grade.total;
                total += t;
                if (t > 0)
                    completeCount++;
            }
            String avg = "" + Math.round(total * 100 / completeCount) / 100.0;
            sb.append(fit(generalColumnWidth, avg));
            sbCSV.append(",").append(avg);

        }
        try {
            PrintWriter w = PrintWriter.createWriter("allGrades.txt");
            w.print(sb.toString());
            w.close();
            Settings.printlnB(Settings.GREEN + "Done!");
        } catch (FileNotFoundException e) {
            Settings.printlnB(Settings.RED + "AllGrades: Cannot write to file: allGrades.txt");
        }

        try {
            PrintWriter w = PrintWriter.createWriter("allGrades.csv");
            w.print(sbCSV.toString());
            w.close();
            Settings.printlnB(Settings.GREEN + "Done!");
        } catch (FileNotFoundException e) {
            Settings.printlnB(Settings.RED + "AllGrades: Cannot write to file: allGrades.csv");
        }

    }

    public static String fit(int i, String x) {
        return String.format("%-" + i + "s", x);
    }

    public static double getGradeTotal(String name, int labIndex) {
        ArrayList<Grade> g = grades.get(name);
        return g == null || g.get(labIndex) == null ? -1 : g.get(labIndex).total;
    }

    public static ArrayList<Integer> missingLabs(int studentIndex) {
        // System.out.println("Checking: " + Settings.studentNames.get(studentIndex));
        ArrayList<Grade> g = grades.get(Settings.studentNames.get(studentIndex));
        ArrayList<Integer> labs = new ArrayList<>();
        for (int i = 0; i < Settings.labNames.length; i++)
            if (g.get(i) == null) {
                labs.add(i);
            }
        return labs;
    }

    static class Grade implements Comparable<Grade> {
        String lastName, firstName, commentLines, deadlineStatus;
        double correct, crash, struct, comment, varName, redund, ec, total;
        int labNo;

        public Grade(String firstName, String lastName, double correct, double crash, double struct, double comment,
                double varName, double redund, double ec, double total, String deadlineStatus, String commentLines,
                int labNo) {
            this.lastName = lastName;
            this.firstName = firstName;
            this.correct = correct;
            this.crash = crash;
            this.struct = struct;
            this.comment = comment;
            this.varName = varName;
            this.redund = redund;
            this.ec = ec;
            this.total = total;
            this.deadlineStatus = deadlineStatus;
            this.labNo = labNo;
        }

        public boolean isLate() {
            return this.deadlineStatus.toLowerCase().contains("late");
        }

        public int compareTo(Grade b) {
            return b.labNo - labNo;
        }
    }
}