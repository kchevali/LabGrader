import java.util.*;

class MyCoin {
    public static void main(String[] args) {
        Coin nickel = new Coin();
        int heads = 0, runs = 10;
        for (int i = 0; i < runs; i++) {
            System.out.println(nickel);
            heads += nickel.isHeads();
            nickel.toss();
        }
        System.out.println("Heads: " + heads + " Tails: " + (runs - heads));
    }
}

class Coin {
    String face;

    public Coin() {
        toss();
    }

    public void toss() {
        face = Math.random() < 0.5 ? "heads" : "tails";
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public int isHeads() {
        return face.equals("heads") ? 1 : 0;
    }

    @Override
    public String toString() {
        return face;
    }

}