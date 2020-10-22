import java.util.LinkedList;

class InputScanner {

    private LinkedList<String> list;

    public InputScanner(LinkedList<String> list) {
        this.list = list;
    }

    public String peekFirst() {
        return list.peekFirst();
    }

    public String pollFirst() {
        return list.pollFirst();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void addFirst(String s) {
        list.addFirst(s);
    }
}