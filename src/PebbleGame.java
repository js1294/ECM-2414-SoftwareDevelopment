package src;

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

    private int numPlayers; // Stores the number of players.

    private boolean finished; //Stores whether the game should finish or not

    /**
     * This is the nested class to represent the player in pebble game.
     */
    public static class Player{

        private ArrayList<Integer> pebbles; // A list of all pebbles each player has.

        private int totalWeight; // Stores the total weight of all the pebbles, used for winning the game.

        private int blackNumber; // Stores the index of the random black bag to identify paired white bag.

        private int choice; // Stores the pebble that will be removed.

        /**
         * The constuctor for the player.
         */
        public Player(){
            this.pebbles = new ArrayList<>();
            blackNumber = 0;
            totalWeight = 0;
        }

        //Getter and setter methods are used for testing purposes only.
        public ArrayList<Integer> getPebbles() {
            return pebbles;
        }

        public int getBlackNumber() {
            return blackNumber;
        }

        public int getTotalWeight() {
            return totalWeight;
        }

        public int getChoice() {
            return choice;
        }

        public void setPebbles(ArrayList<Integer> pebbles) {
            this.pebbles = pebbles;
        }

        public void setChoice(int choice) {
            this.choice = choice;
        }

        public void setBlackNumber(int blackNumber) {
            this.blackNumber = blackNumber;
        }

        public void setTotalWeight(int totalWeight) {
            this.totalWeight = totalWeight;
        }

        /**
         * The adder method for the list of pebbles.
         * Also, adds to total weight.
         * 
         * @param weight of the new pebbles being added.
         * @throws InputMismatchException when a invalid weight is entered
         * (e.g. negative integers or strings).
         */
        public void addPebble(int weight) throws InputMismatchException{
            if (weight <= 0){
                throw new InputMismatchException();
            }
            pebbles.add(weight);
            totalWeight += weight;
        }

        /**
         * The remover method for the list of pebbles.
         * Also, removes from total weight.
         * 
         * @param weight of the pebble being removed.
         * @throws NullPointerException when an pebble is removed but doesn't exsist.
         */
        public void removePebble(int weight) throws NullPointerException{
            if (pebbles.remove(Integer.valueOf(weight))){
                totalWeight -= weight;
            }
            else{
                throw new NullPointerException();
            }
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
        turn = 0;
        finished = false;
    }

    //Getter and setter methods are used for testing purposes only.
    public ArrayList<ArrayList<AtomicInteger>> getBlackbags() {
        return blackbags;
    }

    public ArrayList<ArrayList<AtomicInteger>> getWhitebags() {
        return whitebags;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getTurn() {
        return turn;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public boolean getFinished() {
        return finished;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public void setBlackbags(ArrayList<ArrayList<AtomicInteger>> blackbags) {
        this.blackbags = blackbags;
    }

    public void setWhitebags(ArrayList<ArrayList<AtomicInteger>> whitebags) {
        this.whitebags = whitebags;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }
    
    /**
     * This method sets up the game by creating the players
     * , creating the bags and giving pebbles to the players.
     */
    public void setUp(){
        System.out.println("\n****WELCOME TO PEBBLEGAME****\n"+
        "\nYou will first be asked to enter the number of players,\n"+
        "and then for the location of three files containing positive comma-separated-integers.\n"+
        "The game will then be played until a winning player has achieved a total weight of 100.\n"+
        "To exit the game, enter 'E' at any input.\n");

        numPlayers = 0;

        try{
            //Must be at least one players
            while(numPlayers <= 0 && !finished){
                System.out.println("\nPlease enter the number of players:");
                if (scanner.hasNextInt()) { 
                    numPlayers = scanner.nextInt();
                } else if (scanner.hasNext()){        
                    String userInput = scanner.next();
                    //comparing the input value with letter e ignoring the case
                    if(userInput.equals("E")){
                        finished = true;
                    }
                }  
            }

            //Add pebbles to the bags
            scanner.nextLine();
            int index = 0;
            while (index < 3 && !finished){
                System.out.println("Please enter a location of bag number "+index+" to load:");
                String fileName = scanner.nextLine();
                if(fileName.equals("E")){
                    finished = true;
                    break;
                }
                whitebags.add(new ArrayList<>());         
                blackbags.add(reader(fileName));
                index++;             
            }
        }
        catch (InputMismatchException | IOException
         | NegativeWeightException | TooFewValuesException e){
            System.out.println("\n"+e.toString()+"\n");
            setUp();
        }

        //Each player needs 10 pebbles each.
        int index = 0;
        while (index < numPlayers && !finished){
            players.add(new Player());
            initialDrawer(players.get(index)); 
            index++;          
        }
        mainGame();
    }

    /**
     * The main game, each player is run concurrently as a thread.
     */
    public void mainGame(){
        if(!finished){
            numPlayers = players.size();
            Thread[] threads = new Thread[numPlayers];
            for (turn = 0; turn < numPlayers; turn++){
                //Creates the threads to be run.
                 threads[turn] = new Thread(new Runnable() {
                @Override
                public void run() {
                    playerThead();
                }
                });
            }
    
            // The turn value is updated here to make sure it is correct inside the thread.
            for (turn = 0; turn < numPlayers; turn++) {
                try {
                    threads[turn].start();
                    threads[turn].join(); // Allows for the threads to work concurrently
                } catch (InterruptedException e) {
                    System.out.println("\n"+e.toString()+"\n");
                    Thread.currentThread().interrupt();
                }
            }
            mainGame();
        }
    }

    /**
     * The method that will be run in a thread for each player.
     */
    public void playerThead(){
         // This only gets triggered if a player wins from the initial draw
        if(players.get(turn).totalWeight == 100 && !finished) {
            System.out.println("\nPlayer " + (turn+1) + "'s total weight is " + players.get(turn).totalWeight);
            System.out.println("Player " + (turn+1) + " wins!");
            finished = true; 
        }
        if (!finished){
            // Shows the player their pebbles and total weight at the start of each turn
            System.out.println("\nPlayer "+(turn+1)+"'s turn");
            System.out.println("Your current pebbles are: "+ players.get(turn).pebbles);
            System.out.println("Your current total weight is: "+players.get(turn).totalWeight);
            System.out.println("Please enter a pebble to discard:");

            if (scanner.hasNextInt()){
                players.get(turn).choice = scanner.nextInt();
                try {
                    players.get(turn).removePebble(players.get(turn).choice); // Removes chosen pebble along with its weight
                    ArrayList<AtomicInteger> whitebag = whitebags.get(players.get(turn).blackNumber);
                    whitebag.add(new AtomicInteger(players.get(turn).choice)); // Adds same pebble to whitebag
        
                    discardWriter(players.get(turn));
                    drawer(players.get(turn));
                    drawWriter(players.get(turn));

                } catch (NullPointerException e) {
                    System.out.println("\n"+e.toString()+"\n");
                    System.out.println("Please enter a valid pebble.");
                    playerThead();
                }

            }else if (scanner.hasNext()){
                String userInput = scanner.next();
                if(userInput.equals("E")){
                    finished = true;
                }else{
                    System.out.println("Please enter a valid pebble.");
                    playerThead();
                }
            }        
        }   

        if(players.get(turn).totalWeight == 100) {
            System.out.println("\nPlayer " + (turn+1) + "'s total weight is " + players.get(turn).totalWeight);
            System.out.println("Player " + (turn+1) + " wins!");
            finished = true; 
        }
    }

    /**
     * A method to read the CSV files used for the black bags.
     * This reads the file splits the file into strings
     * and then converts them into a list of integers.
     * 
     * @param fileName the name of the CSV file.
     * @return the list of integers that was contained within the CSV file.
     * @throws IOException when an I/O exception has occured.
     * @throws NegativeWeightException when a negative or zero weight is used.
     */
    public ArrayList<AtomicInteger> reader(String fileName)
    throws IOException, NegativeWeightException,
    TooFewValuesException{
        String text;
        int size = 0;
        ArrayList<AtomicInteger> integers = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        while((text = reader.readLine()) != null){
            for (String string : text.split(",")) {
                int integer = Integer.parseInt(string);
                if (integer <= 0){// Checks for negative or zero weight pebbles.
                    reader.close();
                    throw new NegativeWeightException();
                }
                for (int j = 0; j < numPlayers; j++) {
                    integers.add(new AtomicInteger(integer));
                }
                size++;
            }
        }
        reader.close();
        if (size < 11){
            throw new TooFewValuesException(); // Checks to see if there are enough pebbles.
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

    /**
     * This method is used to update the individual player file, when discarding a pebble.
     * 
     * @param player is the player currently discarding a pebble.
     */
    public void discardWriter(Player player){

        String list = Arrays.toString(player.pebbles.toArray()).replace("[", "").replace("]", "");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("player" + (turn+1) + "_output.txt", true))) {
            bw.write("player" + (turn+1) + " has discarded a " + player.choice + " to bag " + (char)(player.blackNumber+'A') );
            bw.newLine();
            bw.write("player" + (turn+1) + " hand is " + list );
            bw.newLine();
            bw.newLine();
    
        } catch (IOException e) {
            System.out.println("\n"+e.toString()+"\n");
        }
    }

    /**
     * This method is used to update the individual player file, when adding a pebble.
     * 
     * @param player is the player currently adding a pebble.
     */
    public void drawWriter(Player player){

        String list = Arrays.toString(player.pebbles.toArray()).replace("[", "").replace("]", "");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("player" + (turn+1) + "_output.txt", true))) {
            bw.write("player" + (turn+1) + " has drawn a " + player.pebbles.get(player.pebbles.size()-1) + " from bag " + (char)(player.blackNumber+'A'+23) );
            bw.newLine();
            bw.write("player" + (turn+1) + " hand is " + list );
            bw.newLine();
            bw.newLine();
    
        } catch (IOException e) {
            System.out.println("\n"+e.toString()+"\n");
        }
    }
}
