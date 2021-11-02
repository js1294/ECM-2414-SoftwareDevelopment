import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;
import java.io.IOException;  
import java.io.FileWriter;


/**
 * A short program to implement the pebble game.
 * This will create a number of players that will run as concurrent threads.
 * Each player will try to get their total weight to pebbles to 100 to win.
 * 
 * @author Jack Shaw, Shalan Sharma
 * @version 0.8
 */
public class PebbleGame{
    
    private ArrayList<ArrayList<AtomicInteger>> whitebags; // Stores the white bag of pebbles in order A,B,C.

    private ArrayList<ArrayList<AtomicInteger>> blackbags; // Stores the black bag of pebbles in order X,Y,Z.

    private ArrayList<Player> players; // Stores an array of all players.

    private Random random; // Stores random for reuse within this class.

    private Scanner scanner; // Stores scanner for reuse within this class.

    private int turn; // Stores the current turn, to know what player should be playing.

    private boolean winner; // Stores if their a winner or not.

    private int numPlayers; // Stores the number of players 

    /**
     * This is the nested class to represent the player in pebble game.
     */
    public static class Player{

        private ArrayList<Integer> pebbles; // A list of all pebbles each player has.

        private int totalWeight; // Stores the total weight of all the pebbles, used for winning the game.

        private int blackNumber; // Stores the index of the black bag to make sure the player report file
        // and white bag discarded to is correct.

        private int choice; // Stores the pebble that will be removed.

        /**
         * The constuctor for the player.
         */
        public Player(){
            this.pebbles = new ArrayList<>();
            blackNumber = 0;
            totalWeight = 0;
        }

        /**
         * The adder method for the list of pebbles.
         * 
         * @param weight of the new pebbles being added.
         */
        public void addPebble(int weight){
            pebbles.add(weight);
            totalWeight += weight;
        }

        public void removePebble(int weight){
            pebbles.remove(Integer.valueOf(weight));
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
        random = new Random();
        winner = false;

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

        
        try{
            
            //Must be at least one players
            while(numPlayers <= 0){
                System.out.println("Please enter the number of players:");
                numPlayers = scanner.nextInt();
            }

            //Add pebbles to the bags
            scanner.nextLine();
            for (int i = 0; i < 3; i++) {
                System.out.println("Please enter a location of bag number "+i+" to load:");
                String fileName = scanner.nextLine();
                whitebags.add(new ArrayList<>());         
                blackbags.add(reader(fileName));
                
            }

        }catch (InputMismatchException e){
            System.out.println("\n"+e.toString()+"\n");
            scanner.nextLine();
            setUp();
        }

        //Each player needs 10 pebbles each.
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player());
            initialDrawer(players.get(i));           
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
        numPlayers = players.size();
        Thread[] threads = new Thread[numPlayers];
        for (turn = 0; turn < numPlayers; turn++){
            //Creates the threads to be run.
            threads[turn] = new Thread(new Runnable() {
            @Override
            public void run() {

                if(players.get(turn).totalWeight == 100) {

                    System.out.println("Player " + (turn+1) + " wins!");
                    winner = true;
                    return;
                }

                //shows the player their pebbles and total weight on each turn

                System.out.println("\nPlayer "+(turn+1)+"'s turn");
                System.out.println("Your current pebbles are: "+ players.get(turn).pebbles);
                System.out.println("Your current total weight is: "+players.get(turn).totalWeight);
                System.out.println("Please enter a pebble to discard:");
            
                players.get(turn).choice = scanner.nextInt();
                        
                //removes chosen pebble along with its weight
                players.get(turn).removePebble(players.get(turn).choice);
                ArrayList<AtomicInteger> whitebag = whitebags.get(players.get(turn).blackNumber);
                whitebag.add(new AtomicInteger(players.get(turn).choice));

                discardWriter(players.get(turn));
                drawer(players.get(turn));
                drawWriter(players.get(turn));
                System.out.println("Your current total weight is: "+players.get(turn).totalWeight);

                //TODO: REMOVE
                System.out.println("whitebags: " + whitebags);
                System.out.println("blackbags: " + blackbags);   


                


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
                Thread.currentThread().interrupt();
            }
        }

        if (!winner){
            mainGame();
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
    public ArrayList<AtomicInteger> reader(String fileName){
        String text;
        ArrayList<AtomicInteger> integers = new ArrayList<>();

        try (BufferedReader reader  = new BufferedReader(new FileReader(fileName))) {
            while((text = reader.readLine()) != null){
                for (String string : text.split(",")) {
                    for (int j = 0; j < numPlayers; j++) {
                        integers.add(new AtomicInteger(Integer.parseInt(string)));
                    }
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
        player.blackNumber = random.nextInt(3);
        ArrayList<AtomicInteger> blackbag = blackbags.get(player.blackNumber);
        if (!blackbag.isEmpty()){
            int randNum = random.nextInt(blackbag.size());
            player.addPebble(blackbag.get(randNum).get());
            blackbag.remove(randNum);
            System.out.println("correct is randNum is: " + player.pebbles.get(player.pebbles.size() - 1));
        }
            else{
                blackbags.get(player.blackNumber).addAll(whitebags.get(player.blackNumber));
                whitebags.get(player.blackNumber).clear();
                drawer(player);
            }     
    }


    /**
     * This method is used to draw the initial pebbles for a player.
     * Takes it from a random black bag.
     * 
     * @param player is the player to draw the pebbles to
     */
    public void initialDrawer(Player player){
        player.blackNumber = random.nextInt(3);
        ArrayList<AtomicInteger> blackbag = blackbags.get(player.blackNumber);
        for (int j = 0; j < 10; j++) {
            int randNum = random.nextInt(blackbag.size());
            player.addPebble(blackbag.get(randNum).get());
            blackbag.remove(randNum);
        }
    }




    public void discardWriter(Player player){

        String list = Arrays.toString(player.pebbles.toArray()).replace("[", "").replace("]", "");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("player" + (turn+1) + "_output.txt", true))) {
            bw.write("player" + (turn+1) + " has discarded a " + player.choice + " to bag " + (char)(player.blackNumber+'A') );
            bw.newLine();
            bw.write("player" + (turn+1) + " hand is " + list );
            bw.newLine();
    
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void drawWriter(Player player){

        String list = Arrays.toString(player.pebbles.toArray()).replace("[", "").replace("]", "");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("player" + (turn+1) + "_output.txt", true))) {
            bw.write("player" + (turn+1) + " has drawn a " + player.pebbles.get(player.pebbles.size()-1) + " from bag " + (char)(player.blackNumber+'A'+23) );
            bw.newLine();
            bw.write("player" + (turn+1) + " hand is " + list );
            bw.newLine();
            bw.newLine();
    
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
        


    //TODO: Remove
    public static void main(String[] args) {
        new PebbleGame();
    }
}
