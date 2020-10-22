
public class Random {

    public int nextInt(int n) {
        return (int) java.lang.Math.floor(Math.random() * n);
    }

    public double nextDouble() {
        return Math.random();
    }
}
