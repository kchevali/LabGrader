import java.io.FileNotFoundException;

class BufferedReader {

    public Scanner scan;

    public BufferedReader(FileReader reader) {
        try {
            scan = new Scanner(new File(""));// should be ok that File will rename path
        } catch (FileNotFoundException e) {
        }
        scan.canReturnNull = true;
    }

    public String readLine() throws java.io.IOException {
        return scan.nextLine();
    }

    public boolean ready() {
        return scan.hasNext();
    }
}