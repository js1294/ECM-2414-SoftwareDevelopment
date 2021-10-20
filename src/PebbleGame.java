import java.util.ArrayList;
import java.util.List;


/**
* @author Jack Shaw, Shalan Sharma
* @version 0.1
*/
public class PebbleGame{
    
    private ArrayList<Integer> whitebag1;
    
    private ArrayList<Integer> whitebag2;

    private ArrayList<Integer> whitebag3;

    private ArrayList<Integer> blackbag1;

    private ArrayList<Integer> blackbag2;

    private ArrayList<Integer> blackbag3;
    /**
     * This is the nested class to represent the player.
     * 
     */
    public static class Player{

        private List<Integer> pebbles;

        private int totalWeight;

        public Player(List<Integer> pebbles){
            this.pebbles = pebbles;
            totalWeight = totalPebbles();
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
        "The game will then be simulated, and the output written to rules in this directory.");
        
        try {
            System.out.println("Please enter the number of players");
        } catch (Exception e) {
            //TODO: handle exception
        }
        
    }
    public static void main(String[] args) {
        new PebbleGame();
    }
}
