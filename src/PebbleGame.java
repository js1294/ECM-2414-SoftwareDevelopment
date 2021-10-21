import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;


/**
* @author Jack Shaw, Shalan Sharma
* @version 0.1
*/
public class PebbleGame{
    
    private ArrayList<ArrayList<Integer>> whitebags; // Stored in order A,B,C

    private ArrayList<ArrayList<Integer>> blackbags; // Stored in order X,Y,Z

    private ArrayList<Player> players;

    private Random random;

    /**
     * This is the nested class to represent the player.
     * 
     */
    public static class Player{

        private ArrayList<Integer> pebbles;

        private int totalWeight;

        public Player(){
            this.pebbles = new ArrayList<>();
            totalWeight = 0;
        }

        public ArrayList<Integer> getPebbles(){
            return pebbles;
        }

        public int getTotalWeight(){
            return totalWeight;
        }

        public void addPebble(int weight){
            pebbles.add(weight);
            totalWeight = totalPebbles();
        }

        public void removePebble(int weight){
            pebbles.remove(pebbles.indexOf(weight));
            totalWeight = totalPebbles();
        }

        private int totalPebbles(){
            int total = 0;
            for (int pebble : pebbles) {
                total += pebble;
            }
            return total;
        }

        public String toString(){
            return pebbles.toString();
        }
    }

    /**
     * The constructor for pebble game.
     * This creates the bags, players and assigns
     * pebbles to the players and bags
     */
    public PebbleGame(){

        //Initalising the ArrayLists.
        whitebags = new ArrayList<>(3);
        blackbags = new ArrayList<>(3);
        players = new ArrayList<>();

        System.out.println("Welcome to the PebbleGame!!\n"+
        "You will be asked to enter the number of players\n"+
        "and then for the location of three files in turn containing"+
        "comma seperated intger values for the pebbles weights. "+
        "The integer values must be strictly positive.\n"+
        "The game will then be simulated, and the output written to rules in this directory.\n");

        int numPlayers = 0;
        try (Scanner scanner = new Scanner(System.in)){

            System.out.println("Please enter the number of players:");
            numPlayers = scanner.nextInt();
            
            //Creates the players
            for (int i = 0; i < numPlayers; i++) {
                players.add(new Player());
            }

            //Add pebbles to the bags
            String fileName = "";
            scanner.nextLine();
            for (int i = 0; i < 3; i++) {
                System.out.println("Please enter a location of bag number "+i+" to load:");
                fileName = scanner.nextLine();
                blackbags.add(reader(fileName));
                whitebags.add(new ArrayList<>());
            }
        }catch (Exception e){
            e.printStackTrace();
        } 

        //Each player needs 10 pebbles each.
        for (Player player : players) {
            for (int i = 0; i < 10; i++) {
                drawer(player);
            }
        }

        System.out.println(blackbags);
        System.out.println(whitebags);
        System.out.println(players);
    }

    /**
     * A method to read the CSV files used for the black bags.
     * This reads the file splits the file into strings
     * and then converts them into a list of integers.
     * 
     * @param fileName the name of the CSV file.
     * @return the list of integers that was contained within the CSV file.
     */
    public ArrayList<Integer> reader(String fileName){
        String text;
        ArrayList<Integer> integers = new ArrayList<>();

        try (BufferedReader reader  = new BufferedReader(new FileReader(fileName))) {
            while((text = reader.readLine()) != null){
                for (String string : text.split(",")) {
                    integers.add(Integer.parseInt(string));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return integers;
    }

    /**
     * This method is used to draw a pebble for a player.
     * Takes it from a random black bag that isn't empty.
     * 
     * @param player is the player to draw the pebble to
     */
    public void drawer(Player player){
        random = new Random();
        int blackNumber = random.nextInt(3);
        ArrayList<Integer> blackbag = blackbags.get(blackNumber);
        if (!blackbag.isEmpty()){
            player.addPebble(blackbag.get(random.nextInt(blackbag.size())));
        }
        else{
            blackbags.set(blackNumber, whitebags.get(blackNumber));
            drawer(player);
        }
    }

    public static void main(String[] args) {
        new PebbleGame();
    }
}
