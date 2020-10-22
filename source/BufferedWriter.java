import java.util.Arrays;

public class BufferedWriter {

    private java.io.BufferedWriter writer;

    public BufferedWriter(FileWriter w) {
        writer = new java.io.BufferedWriter(w.fileWriter);
    }

    public void write(int obj) throws java.io.IOException {
        Log.debug("To File: " + obj);
        writer.write(obj);
    }

    public void write(char[] cbuf, int off, int len) throws java.io.IOException {
        Log.debug("To File: " + Arrays.toString(cbuf));
        writer.write(cbuf, off, len);
    }

    public void write(String s, int off, int len) throws java.io.IOException {
        Log.debug("To File: " + s);
        writer.write(s, off, len);
    }

    public void write(String s) throws java.io.IOException {
        Log.debug("To File: " + s);
        writer.write(s);
    }

    public void close() throws java.io.IOException {
        Log.debug("SAVED FILE");
        writer.close();
    }

    public void flush() throws java.io.IOException {
        writer.flush();
    }
}