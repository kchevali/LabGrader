import java.util.*;

class MyRPS {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String[] moves = { "Rock", "Paper", "Scissors", "Lizard", "Spock" };
        int[][] judge = { { 0, -1, 1, 1, -1 }, { 1, 0, -1, -1, 1 }, { -1, 1, 0, 1, -1 }, { -1, 1, -1, 0, 1 },
                { 1, -1, 1, -1, 0 } };
        String[] results = { "Lose", "Tie", "Win" };
        String again;
        do {
            System.out.println("Lets play rock, paper, scissors, lizard, spock!");
            int player = -1;
            do {
                String move = scan.nextLine();
                for (int i = 0; i < moves.length; i++)
                    if (moves[i].equalsIgnoreCase(move))
                        player = i;
                if (player == -1)
                    System.out.println("Please type rock, paper, scissors, lizard or spock!");
            } while (player == -1);
            int cpu = (int) (Math.random() * moves.length);
            System.out.println("You " + results[judge[player][cpu] + 1] + "! You played " + moves[player]
                    + " and I played " + moves[cpu] + ".");
            System.out.println("Do you want to play again?");
            again = scan.nextLine().toLowerCase();
        } while (again.length() == 0 || again.charAt(0) != 'n');
    }
}