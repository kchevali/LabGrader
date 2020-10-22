import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.lang.reflect.*;

public class ClassAnalysis {

    public static void main(String[] args) {
        Settings.loadSettings();
        ArrayList<String> names = Storage.get("classnames");
        ArrayList<String> lines = Scanner.getLines("Student.java");
        // names.add(0, "Student");
        for (String name : names) {
            // try {// Does not work for private methods/fields
            Class c;
            try {
                c = Class.forName(name);
            } catch (ClassNotFoundException e) {
                continue;
                // Settings.printlnB("ClassAnalysis: Could not read: " + name);
            } catch (NoClassDefFoundError e) {
                Settings.printlnB(Settings.RED + "ClassAnalysis: Could not find: " + name);
                e.printStackTrace();
                System.exit(1);
                return;
            }
            Method[] m = c.getDeclaredMethods();
            // count recursion
            boolean[] isRecursive = new boolean[m.length];
            for (int i = 0; i < m.length; i++) {
                String declaration = m[i].toString();
                String methodName = declaration.split(name + "\\.")[1].split(" ")[0].split("\\(")[0];
                int bracket = 0;
                boolean matched = false;
                for (String line : lines) {
                    line = line.split("//")[0];// remove comments
                    // System.out.println("Line: " + line);

                    // method header regex
                    if (matched && line.matches(".*" + methodName + "(\\s)*[(].*[)].*")) {
                        isRecursive[i] = true;
                        // System.out.println("FOUND");
                        break;
                    }

                    int posBracket = CreateStudent.occurenceCount(line, '{');
                    int negBracket = CreateStudent.occurenceCount(line, '}');

                    bracket += posBracket;
                    bracket -= negBracket;
                    if (!matched && line.matches("((\\s)*[a-zA-z\\[\\]]+)+(\\s)+" + methodName + "(\\s)*[(].*[)].*")) {
                        matched = true;
                        bracket = posBracket > 0 ? 0 : -1;
                        // System.out.println("MATCHED");
                    }
                    if (negBracket > 0 && bracket < 0 && matched)
                        break;
                }
            }

            Constructor[] con = c.getDeclaredConstructors();
            String superName = c.getSuperclass().getSimpleName().toUpperCase();
            Log.writeOnly(name.toUpperCase() + (superName.equals("OBJECT") ? "" : " extends " + superName)
                    + " | Constructor Count: " + con.length + " | Method Count: " + m.length);
            for (int i = 0; i < con.length; i++) {
                String declaration = con[i].toString();
                String line = declaration.startsWith("public") ? "+ " : declaration.startsWith("private") ? "- " : "  ";
                // System.out.println("Declaration: " + declaration);
                line += declaration.contains(" ") ? declaration.split(" ")[1] : declaration;
                Log.writeOnly(line);
            }
            for (int i = 0; i < m.length; i++) {
                String declaration = m[i].toString();
                String line = declaration.startsWith("public") ? "+ " : declaration.startsWith("private") ? "- " : " ";
                line += declaration.split(name + "\\.")[1].split(" ")[0];
                if (isRecursive[i])
                    line += "(recursive)";
                Log.writeOnly(line);
            }
            Log.writeOnly("");
        }
    }
}