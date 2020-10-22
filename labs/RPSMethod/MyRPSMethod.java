import java.util.*;

class MyRPSMethod {

    static Scanner scan = new Scanner(System.in);
    static String[] moves = { "Rock", "Paper", "Scissors", "Lizard", "Spock" };
    static String[] results = { "Lose", "Tie", "Win" };
    static int[][] judge = { { 0, -1, 1, 1, -1 }, { 1, 0, -1, -1, 1 }, { -1, 1, 0, 1, -1 }, { -1, 1, -1, 0, 1 },
            { 1, -1, 1, -1, 0 } };

    public static void main(String[] args) {
        String again;
        do {
            run();

            System.out.println("Do you want to play again?");
            again = scan.nextLine().toLowerCase();
        } while (again.length() == 0 || again.charAt(0) != 'n');
    }

    public static int getUser() {
        int player = -1;
        do {
            String move = scan.nextLine();
            for (int i = 0; i < moves.length; i++)
                if (moves[i].equalsIgnoreCase(move))
                    player = i;
            if (player == -1)
                System.out.println("Please type rock, paper, scissors, lizard or spock!");
        } while (player == -1);
        return player;
    }

    public static int getCPU() {
        return (int) (Math.random() * moves.length);
    }

    public static String getResult(int player, int cpu) {
        return results[judge[player][cpu] + 1];
    }

    public static void print(int player, int cpu) {
        System.out.println("You " + getResult(player, cpu) + "! You played " + moves[player] + " and I played "
                + moves[cpu] + ".");
    }

    public static void run() {
        System.out.println("Lets play rock, paper, scissors, lizard, spock!");
        print(getUser(), getCPU());
    }
}