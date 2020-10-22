import java.util.*;

class MyFreezeBoil {

    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Enter a temperature");
        FreezingAndBoiling fb = new FreezingAndBoiling(getInt());

        Stuff ethyl = new Stuff("Ethyl", -173, 172);
        Stuff oxygen = new Stuff("Oxygen", -362, -306);
        Stuff water = new Stuff("Water", 32, 212);
        Stuff[] all = { ethyl, oxygen, water };

        System.out.println(fb.show(all));
    }

    public static int getInt() {
        while (!scan.hasNextInt()) {
            System.err.println("Please enter a valid number!");
            scan.next();
        }
        return scan.nextInt();
    }
}

class FreezingAndBoiling {
    int temperature;

    public FreezingAndBoiling(int temperature) {
        this.temperature = temperature;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String show(Stuff[] all) {
        String out = "";
        for (Stuff s : all) {
            if (s.isFreezing(temperature))
                out += s.show("freeze");
            if (s.isBoiling(temperature))
                out += s.show("boil");
        }
        return out;
    }

}

class Stuff {
    String name;
    int boil, freeze;

    public Stuff(String name, int freeze, int boil) {
        this.name = name;
        this.freeze = freeze;
        this.boil = boil;
    }

    public boolean isFreezing(int temperature) {
        return freeze >= temperature;
    }

    public boolean isBoiling(int temperature) {
        return boil <= temperature;
    }

    public String show(String type) {
        return name + " will " + type + ".\n";
    }
}