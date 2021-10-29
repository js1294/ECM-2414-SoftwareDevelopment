import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
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

    private Scanner scanner;

    private int turn;

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
            totalWeight += weight;
        }

        public void removePebble(int weight){
            totalWeight -= weight;
        }

        public String toString(){
            return pebbles.toString();
        }
    }

    /**
     * The constructor for pebble game.
     */
    public PebbleGame(){

        //Initalising the ArrayLists.
        whitebags = new ArrayList<>(3);
        blackbags = new ArrayList<>(3);
        players = new ArrayList<>();
        scanner = new Scanner(System.in);

        setUp();
    }

    /**
     * This method sets up the game by creating the players
     * , creating the bags and giving pebbles to the players.
     */
    public void setUp(){
        System.out.println("Welcome to the PebbleGame!!\n"+
        "You will be asked to enter the number of players\n"+
        "and then for the location of three files in turn containing "+
        "comma seperated intger values for the pebbles weights. "+
        "The integer values must be strictly positive.\n"+
        "The game will then be simulated, and the output written to rules in this directory.\n");

        int numPlayers = 0;
        try{
            
            //Must be at least one players
            while(numPlayers <= 0){
                System.out.println("Please enter the number of players:");
                numPlayers = scanner.nextInt();
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
        }catch (InputMismatchException e){
            System.out.println("\n"+e.toString()+"\n");
            scanner.nextLine();
            setUp();
        }

        //Each player needs 10 pebbles each.
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player());
            for (int j = 0; j < 10; j++) {
                drawer(players.get(i));
            }
        }

        //TODO: REMOVE
        System.out.println(blackbags);
        System.out.println(whitebags);
        System.out.println(players);

        mainGame();
    }

    /**
     * The main game
     */
    public void mainGame(){
        int numPlayers = players.size();
        Thread[] threads = new Thread[numPlayers];
        for (turn = 0; turn < numPlayers; turn++){
            //Creates the threads to be run.
            threads[turn] = new Thread(new Runnable() {
            @Override
            public void run() {
            


            System.out.println("Player "+(turn+1)+"'s turn");
            System.out.println("Your current pebbles are: "+players.get(turn).getPebbles());
            System.out.println("Your current total weight is: "+players.get(turn).getTotalWeight());
            System.out.println("Please enter a pebble to discard:");
    
            int choice = scanner.nextInt();
                
            //removes chosen pebble along with its weight
            players.get(turn).removePebble(choice);
            players.get(turn).getPebbles().remove(Integer.valueOf(choice));
    
            drawer(players.get(turn));
            System.out.println(players.get(turn).getPebbles());
            System.out.println("Your current total weight is: "+players.get(turn).getTotalWeight());
    
    


            


            

            }
        });
        }
        // The turn value is updated here to make sure it is correct inside the thread.
        for (turn = 0; turn < numPlayers; turn++) {
            try {
                threads[turn].start();
                threads[turn].join(); // Allows for the threads to work concurrently
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
            System.out.println("\n"+e.toString()+"\n");
            setUp();
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
            int randNum = random.nextInt(blackbag.size());
            player.addPebble(blackbag.get(randNum));
            blackbag.remove(randNum);
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
