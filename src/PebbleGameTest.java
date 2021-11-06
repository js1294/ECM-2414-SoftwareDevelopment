package src;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.*;
import org.junit.rules.TemporaryFolder;

import src.PebbleGame.Player;

/**
 * This class is used for the testing of pebbles game,
 * containing both intergration testing and unit tests.
 */
public class PebbleGameTest {

  private static PebbleGame pebbleGame;

  private static Player player;

  private String validFile;

  private String tooLowFile;

  private String negativeFile;
  
  private String tooFewFile;

  private String playerFile;

  private ArrayList<ArrayList<AtomicInteger>> blackBags;

  private ArrayList<ArrayList<AtomicInteger>> whiteBags;

  private ArrayList<AtomicInteger> validBag;

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  /**
   * This method is used to create static objects of each class.
   */
  @BeforeClass
  public static void mainSetup(){
    pebbleGame = new PebbleGame();
    player = new Player();
  }
  
  /**
   * This method is used to setup files and arrays ready for testing.
   */
  @Before
  public void setup(){
    validFile = "valid.csv";
    tooLowFile = "tooLowFile.csv";
    negativeFile = "negativeFile.csv";
    tooFewFile = "tooFewFile.csv";
    playerFile = "player1_output.txt";

    writer(validFile, "1,2,3,4,5,6,7,8,9,10,11");
    writer(tooLowFile, "1,1,1,1,1,1,1,1,1,1,1");
    writer(negativeFile, "-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,100");
    writer(tooFewFile, "100,100");
    writer(playerFile, "");
    pebbleGame.setNumPlayers(1);

    blackBags = new ArrayList<>(3);
    whiteBags = new ArrayList<>(3);

    validBag = new ArrayList<>(Arrays.asList(new AtomicInteger(1),
    new AtomicInteger(2), new AtomicInteger(3), new AtomicInteger(4),
    new AtomicInteger(5), new AtomicInteger(6), new AtomicInteger(7),
    new AtomicInteger(8),new AtomicInteger(9), new AtomicInteger(10),
    new AtomicInteger(11)));
  }

  /**
   * This method is used to create and write temporary files for testing.
   * 
   * @param fileName the name of the file being created.
   * @param write what to write to the file.
   */
  public void writer(String fileName, String write){
    //Create temporary file
    try {
      folder.newFile(fileName);
    } catch (IOException e) {
      fail("IOException");
    }
    //Write to Temporary file
    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))){
      bufferedWriter.write(write);
    } catch (IOException e) {
      fail("IOException");
    }
  }

  /**
   * This method is used to read temporary files.
   * 
   * @param fileName the file name being read.
   * @return a list of each line being read.
   */
  public String reader(String fileName){
    //Read a Temporary file
    StringBuilder builder = new StringBuilder();
    String line = "";
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))){
      while ((line = bufferedReader.readLine()) != null){
        builder.append(line);
      }
    } catch (IOException e) {
      fail("IOException");
    }
    return builder.toString();
  }

  //Unit Tests
  @Test
  public void initialDrawerTest(){

    // Test with all black bags filled.
    blackBags.add(validBag);
    blackBags.add(validBag);
    blackBags.add(validBag);

    whiteBags.add(new ArrayList<>());
    whiteBags.add(new ArrayList<>());
    whiteBags.add(new ArrayList<>());

    // Validate length of bags is correct for test.
    assertEquals(11, blackBags.get(0).size());
    assertEquals(11, blackBags.get(1).size());
    assertEquals(11, blackBags.get(2).size());
    assertEquals(0, whiteBags.get(0).size());
    assertEquals(0, whiteBags.get(1).size());
    assertEquals(0, whiteBags.get(2).size());
    assertEquals(1, pebbleGame.getNumPlayers());

    pebbleGame.setBlackbags(blackBags);
    pebbleGame.setWhitebags(whiteBags);

    player.setPebbles(new ArrayList<>());
    assertEquals(0, player.getPebbles().size());
    assertEquals(11, pebbleGame.getBlackbags().get(player.getBlackNumber()).size());
    assertEquals(0, pebbleGame.getWhitebags().get(player.getBlackNumber()).size());

    pebbleGame.initialDrawer(player);

    assertEquals(10, player.getPebbles().size());
    assertEquals(1, pebbleGame.getBlackbags().get(player.getBlackNumber()).size());
    assertEquals(0, pebbleGame.getWhitebags().get(player.getBlackNumber()).size());
  }

  @Test
  public void drawerTest(){

    // Test with all black bags filled and white bags empty.
    blackBags.add(validBag);
    blackBags.add(validBag);
    blackBags.add(validBag);

    whiteBags.add(new ArrayList<>());
    whiteBags.add(new ArrayList<>());
    whiteBags.add(new ArrayList<>());

    // Validate length of bags is correct for test.
    assertEquals(11, blackBags.get(0).size());
    assertEquals(11, blackBags.get(1).size());
    assertEquals(11, blackBags.get(2).size());
    assertEquals(0, whiteBags.get(0).size());
    assertEquals(0, whiteBags.get(1).size());
    assertEquals(0, whiteBags.get(2).size());
    assertEquals(1, pebbleGame.getNumPlayers());

    pebbleGame.setBlackbags(blackBags);
    pebbleGame.setWhitebags(whiteBags);

    player.setPebbles(new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10)));
    assertEquals(10, player.getPebbles().size());
    assertEquals(11, pebbleGame.getBlackbags().get(player.getBlackNumber()).size());
    assertEquals(0, pebbleGame.getWhitebags().get(player.getBlackNumber()).size());

    pebbleGame.drawer(player);

    assertEquals(11, player.getPebbles().size());
    assertEquals(10, pebbleGame.getBlackbags().get(player.getBlackNumber()).size());
    assertEquals(0, pebbleGame.getWhitebags().get(player.getBlackNumber()).size());

    //Test with all white bags filled and black bags empty.
    validBag = new ArrayList<>(Arrays.asList(new AtomicInteger(1),
    new AtomicInteger(2), new AtomicInteger(3), new AtomicInteger(4),
    new AtomicInteger(5), new AtomicInteger(6), new AtomicInteger(7),
    new AtomicInteger(8),new AtomicInteger(9), new AtomicInteger(10),
    new AtomicInteger(11)));

    blackBags.clear();
    blackBags.add(new ArrayList<>());
    blackBags.add(new ArrayList<>());
    blackBags.add(new ArrayList<>());

    whiteBags.clear();
    whiteBags.add(validBag);
    whiteBags.add(validBag);
    whiteBags.add(validBag);

    // Validate length of bags is correct for test.
    assertEquals(0, blackBags.get(0).size());
    assertEquals(0, blackBags.get(1).size());
    assertEquals(0, blackBags.get(2).size());
    assertEquals(11, whiteBags.get(0).size());
    assertEquals(11, whiteBags.get(1).size());
    assertEquals(11, whiteBags.get(2).size());
    assertEquals(1, pebbleGame.getNumPlayers());

    pebbleGame.setBlackbags(blackBags);
    pebbleGame.setWhitebags(whiteBags);

    player.setPebbles(new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10)));
    assertEquals(10, player.getPebbles().size());
    assertEquals(0, pebbleGame.getBlackbags().get(player.getBlackNumber()).size());
    assertEquals(11, pebbleGame.getWhitebags().get(player.getBlackNumber()).size());

    pebbleGame.drawer(player);

    assertEquals(11, player.getPebbles().size());
    assertEquals(10, pebbleGame.getBlackbags().get(player.getBlackNumber()).size());
    assertEquals(0, pebbleGame.getWhitebags().get(player.getBlackNumber()).size());
  }

  @Test
  public void readerTest(){
    ArrayList<AtomicInteger> pebbles;

    try {
      //Testing using one player, valid csv file.
      pebbleGame.setNumPlayers(1);
      pebbles = pebbleGame.reader(validFile);
      assertTrue(11 <= pebbles.size());

      //Testing using a reasonable number of players
      pebbleGame.setNumPlayers(4);
      pebbles = pebbleGame.reader(validFile);
      assertTrue(11 <= pebbles.size());

      //Testing using hundred players, valid csv file.
      pebbleGame.setNumPlayers(100);
      pebbles = pebbleGame.reader(validFile);
      assertTrue(110 <= pebbles.size());
    } catch (IOException | TotalTooLowException | NegativeWeightException | TooFewValuesException e) {
      fail("Invalid Exception");
    }

    //Testing using one player, csv file which would result in no player being able to win.
    try {
      pebbleGame.setNumPlayers(1);
      pebbleGame.reader(tooLowFile);
      fail("No FileWeightTooLowException");
    } catch (IOException | TooFewValuesException | NegativeWeightException e){
      fail("Invalid Exception");
    } catch (TotalTooLowException e){}

    //Testing using hundred players, csv file which would result in no player being able to win.
    try {
      pebbleGame.setNumPlayers(100);
      pebbleGame.reader(tooLowFile);
      fail("No FileWeightTooLowException");
    } catch (IOException | TooFewValuesException | NegativeWeightException e){
      fail("Invalid Exception");
    } catch (TotalTooLowException e){}

    //Testing using one player, csv file would result in negative pebbles in the bags.
    try {
      pebbleGame.setNumPlayers(1);
      pebbleGame.reader(negativeFile);
      fail("No NegativeWeightPebbleException");
    } catch (IOException | TooFewValuesException | TotalTooLowException e){
      fail("Invalid Exception");
    } catch (NegativeWeightException e){}

    //Testing using hundred players, csv file would result in negative pebbles in the bags.
    try {
      pebbleGame.setNumPlayers(100);
      pebbleGame.reader(negativeFile);
      fail("No NegativeWeightPebbleException");
    } catch (IOException | TooFewValuesException | TotalTooLowException e){
      fail("Invalid Exception");
    } catch (NegativeWeightException e){}

    //Testing using one player, csv file would result in too few pebbles for the players.
    try {
      pebbleGame.setNumPlayers(1);
      pebbleGame.reader(tooFewFile);
      fail("No TooFewValuesException");
    } catch (IOException | NegativeWeightException | TotalTooLowException e){
      fail("Invalid Exception");
    } catch (TooFewValuesException e){}

    //Testing using hundred players, csv file would result in too few pebbles for the players.
    try {
      pebbleGame.setNumPlayers(100);
      pebbleGame.reader(tooFewFile);
      fail("No TooFewValuesException");
    } catch (IOException | NegativeWeightException | TotalTooLowException e){
      fail("Invalid Exception");
    } catch (TooFewValuesException e){}
  }

  @Test
  public void discardWriter(){
    //Testing with a choice of 2 and a bag of A
    player.setPebbles(new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10)));
    assertEquals(10, player.getPebbles().size());
    player.setChoice(2);
    player.setBlackNumber(0);

    pebbleGame.discardWriter(player);

    assertEquals(10, player.getPebbles().size()); //Should have no impact on player's pebbles
    String file = reader(playerFile);
    assertEquals("player1 has discarded a "+player.getChoice()+" to bag Aplayer1 hand is 1, 2, 3, 4, 5, 6, 7, 8, 9, 10", file);

    //Testing that the file has appended correctly and with a choice of 22 and a bag of C.
    player.setPebbles(new ArrayList<>(Arrays.asList(6,2,8,22)));
    player.setChoice(22);
    player.setBlackNumber(2);

    pebbleGame.discardWriter(player);

    assertEquals(4, player.getPebbles().size()); //Should have no impact on player's pebbles
    file = reader(playerFile);
    assertEquals("player1 has discarded a 2 to bag Aplayer1 hand is 1, 2, 3, 4, 5, 6, 7, 8, 9, 10"+
    "player1 has discarded a "+player.getChoice()+" to bag Cplayer1 hand is 6, 2, 8, 22", file);
  }

  @Test
  public void drawWriter(){
    //Testing with a bag of X
    player.setPebbles(new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10)));
    assertEquals(10, player.getPebbles().size());
    player.setBlackNumber(0);

    pebbleGame.drawWriter(player);

    assertEquals(10, player.getPebbles().size()); //Should have no impact on player's pebbles
    String file = reader(playerFile);
    assertEquals("player1 has drawn a "+player.getPebbles().get(player.getPebbles().size()-1)+
    " from bag Xplayer1 hand is 1, 2, 3, 4, 5, 6, 7, 8, 9, 10", file);

    //Testing that the file has appended correctly and with a bag of Z.
    player.setPebbles(new ArrayList<>(Arrays.asList(6,2,8,22)));
    player.setBlackNumber(2);

    pebbleGame.drawWriter(player);

    assertEquals(4, player.getPebbles().size()); //Should have no impact on player's pebbles
    file = reader(playerFile);
    assertEquals("player1 has drawn a 10 from bag Xplayer1 hand is 1, 2, 3, 4, 5, 6, 7, 8, 9, 10"+
    "player1 has drawn a "+player.getPebbles().get(player.getPebbles().size()-1)+" from bag Zplayer1 hand is 6, 2, 8, 22", file);
  }
}
