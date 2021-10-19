import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Jack Shaw, Shalan Sharma
 * @version 0.1
 */
public class pebbles {

    /**
     * This is the nested class for the entire pebble game.
     * 
     */
    public static class PebbleGame{
        
        private ArrayList<ArrayList<Integer>> Bags;
        
        /**
         * This is the nested class to represent the player.
         * 
         */
        public static class Player{

            private ArrayList<Integer> pebbles;

            private int totalWeight;

            public Player(ArrayList<Integer> pebbles){
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
            // Get pebbles from csv file
            ArrayList<Integer> pebbles = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
            Player player = new Player(pebbles);
            System.out.println(player.pebbles);
            System.out.println(player.totalWeight);
            player.addPebble(32);
            System.out.println(player.pebbles);
            System.out.println(player.totalWeight);
        }

        public ArrayList<Integer> getBag(int position){
            return Bags.get(position);
        }

    }        
    public static void main(String[] args) {
        new PebbleGame();
    }
}
