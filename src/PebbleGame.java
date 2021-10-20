import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
* @author Jack Shaw, Shalan Sharma
* @version 0.1
*/
public class PebbleGame{
    
    private ArrayList<ArrayList<Integer>> whitebags;

    private ArrayList<ArrayList<Integer>> blackbags;
    /**
     * This is the nested class to represent the player.
     * 
     */
    public static class Player{

        private List<Integer> pebbles;

        private int totalWeight;

        public Player(){
            this.pebbles = new ArrayList<>();
            totalWeight = 0;
        }

        public List<Integer> getPebbles(){
            return pebbles;
        }

        public int getTotalWeight(){
            return totalWeight;
        }

        public void addPebble(int weight){
            pebbles.add(weight);
            totalWeight = totalPebbles();
        }

        private int totalPebbles(){
            int total = 0;
            for (int pebble : pebbles) {
                total += pebble;
            }
            return total;
        }
    }
    public PebbleGame(){
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
                new Player();
            }
            
            scanner.nextLine();
            for (int i = 0; i < 3; i++) {
                String fileName = "";
                System.out.println("Please enter a location of bag number "+i+" to load:");
                fileName = scanner.nextLine();
                blackbags.set(i, reader(fileName));
            }
        }catch (Exception e){
            e.printStackTrace();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return integers;
    }

    public static void main(String[] args) {
        new PebbleGame();
    }
}
