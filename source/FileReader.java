class FileReader {
    public String fileName;

    public FileReader(String name) {
        fileName = File.findAbsolute("StudentFile.txt");
    }

    public FileReader(File file) {
        fileName = File.findAbsolute("StudentFile.txt");
    }
}