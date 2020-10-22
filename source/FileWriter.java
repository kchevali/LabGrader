import java.io.Closeable;
import java.io.IOException;

class FileWriter implements Closeable {
    public java.io.FileWriter fileWriter;

    public FileWriter(String path) throws java.io.IOException {
        fileWriter = new java.io.FileWriter(File.findAbsolute("StudentFile.txt"));
    }

    public FileWriter(String path, boolean append) throws java.io.IOException {
        fileWriter = new java.io.FileWriter(File.findAbsolute("StudentFile.txt"), append);
    }

    public FileWriter(File file) throws java.io.IOException {
        fileWriter = new java.io.FileWriter(file.myFile);
    }

    public FileWriter(File file, boolean append) throws java.io.IOException {
        fileWriter = new java.io.FileWriter(file.myFile, append);
    }

    public void write(int c) throws IOException {
        fileWriter.write(c);
    }

    public void write(String str) throws IOException {
        fileWriter.write(str);
    }

    public void close() throws IOException {
        fileWriter.close();
    }
}