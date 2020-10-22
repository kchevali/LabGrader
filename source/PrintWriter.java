
public class PrintWriter implements AutoCloseable {

    private java.io.PrintWriter writer;
    private boolean isStudentWriter = true;

    public PrintWriter() {
    }

    public PrintWriter(String name) throws java.io.FileNotFoundException {
        writer = new java.io.PrintWriter(File.findAbsolute("StudentFile.txt"));
    }

    public PrintWriter(File file) throws java.io.FileNotFoundException {
        writer = new java.io.PrintWriter(file.myFile);
    }

    public PrintWriter(FileWriter fw) throws java.io.FileNotFoundException {
        writer = new java.io.PrintWriter(fw.fileWriter);
    }

    public static PrintWriter createWriter(String name) throws java.io.FileNotFoundException {
        return createWriterPath(File.findAbsolute(name));
    }

    public static PrintWriter createWriterPath(String path) throws java.io.FileNotFoundException {
        PrintWriter w = new PrintWriter();
        w.writer = new java.io.PrintWriter(path);
        w.isStudentWriter = false;
        return w;
    }

    public void println(Object obj) {
        if (isStudentWriter) {
            Log.writeOnly("To File: " + obj);
            FileTerminal.append("Wrote To File: " + obj);
        }
        writer.println(obj);
    }

    public void print(Object obj) {
        if (isStudentWriter) {
            Log.writeOnly("To File: " + obj);
            FileTerminal.append("Wrote To File: " + obj);
        }
        writer.print(obj);
    }

    public void close() {
        if (isStudentWriter)
            Log.debug("SAVED FILE");
        writer.close();
    }

    public void flush() {
        writer.flush();
    }
}