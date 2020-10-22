import java.security.*;
import java.util.Arrays;

public class RunStudent {

    public static void main(String[] args) {
        Settings.loadSettings();
        Scanner.setAutoMode(true);
        File[] inputFiles = File.create(File.findAbsolute(Settings.labName + "/")).listFiles();
        Arrays.sort(inputFiles);
        ExitTrappedException.forbidSystemExitCall();
        for (File file : inputFiles) {
            String fileName = file.getName();
            String[] fileParts = fileName.split("\\.");
            if (fileParts.length > 1 && fileParts[1].equals("in")) {
                Scanner.readOrderFile();
                try {
                    Scanner.loadInputs(fileName);
                    Student.main(args);
                    Settings.println("Run: " + Settings.GREEN + "Successful");
                } catch (ExitTrappedException e) {
                    // System.out.println("Caught the exit() call!");
                    // } catch (ClassNotFoundException e) {
                    // Log.write("Student File did not compile!");
                } catch (Exception e) {
                    Settings.printlnB(Settings.RED + "\n*[[CRASH]]");
                    Log.write("CRASH: " + e);
                    if (Settings.isDebug) {
                        FileTerminal.append("*Debug Enabled PrintStack:");
                        e.printStackTrace();
                    }
                }
                FileTerminal.append("\n*[[DONE]]");
                Pause.pause();
            }

        }
        FileTerminal.pushLog();
    }

    private static class ExitTrappedException extends SecurityException {

        private static final long serialVersionUID = 1L;

        private static void forbidSystemExitCall() {
            final SecurityManager securityManager = new SecurityManager() {
                public void checkPermission(Permission permission) {
                    if ("exitVM.0".equals(permission.getName())) {
                        throw new ExitTrappedException();
                    }
                }
            };
            System.setSecurityManager(securityManager);
        }
    }
}