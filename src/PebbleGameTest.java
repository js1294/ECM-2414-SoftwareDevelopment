package src;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
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

  private String negativeFile;
  
  private String tooFewFile;

  private String playerFile;

  private String samefile;

  private ByteArrayOutputStream out;

  private static PrintStream backupOut;

  private static InputStream backupIn;

  @Rule
  public TemporaryFolder folder = new TemporaryFolder(); // Used to create a temporary file.

  /**
   * This method is used to create static objects of each class.
   */
  @BeforeClass
  public static void mainSetup(){
    pebbleGame = new PebbleGame();
    player = new Player();
    backupOut = System.out; // Used for resetting the input and output stream
    backupIn = System.in;
  }

    /**
   * This method sets up the test with everything at a default value.
   */
  @Before
  public void setupEmpty(){
    ArrayList<Player> players = new ArrayList<>(Arrays.asList(player));
    pebbleGame.setPlayers(players);
    pebbleGame.setTurn(0);
    pebbleGame.setFinished(false);
    pebbleGame.setNumPlayers(1);
    pebbleGame.setBlackbags(new ArrayList<>());
    pebbleGame.setWhitebags(new ArrayList<>(Arrays.asList(new ArrayList<>(),new ArrayList<>(),new ArrayList<>())));

    player.setBlackNumber(0);
    player.setChoice(0);
    player.setPebbles(new ArrayList<>());
    player.setTotalWeight(0);
    
    out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out)); // Sets the output stream, so that it can be read.
  }

  /**
   * This method is used to create some temporary files used for reading and writing to.
   */
  @Before
  public void createFiles(){
    validFile = "valid.csv";
    negativeFile = "negativeFile.csv";
    tooFewFile = "tooFewFile.csv";
    samefile = "sameFile.csv";
    playerFile = "player1_output.txt";

    writer(validFile, "1,2,3,4,5,6,7,8,9,10,11");
    writer(negativeFile, "-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,100");
    writer(tooFewFile, "100,100");
    writer(playerFile, "");
    writer(samefile, "11,11,11,11,11,11,11,11,11,11,11");
  }

  /**
   * This method restores both the input and output stream to system.in or system.out.
   */
  @After
  public void restoreInput(){
    System.setIn(backupIn);
    System.setOut(backupOut);
  }

  /**
   * This method sets everything up with the black bags filled with a valid set of pebbles
   */
  public void setupValid(){
    ArrayList<ArrayList<AtomicInteger>> blackBags = new ArrayList<>(3);
    ArrayList<ArrayList<AtomicInteger>> whiteBags = new ArrayList<>(3);

    ArrayList<AtomicInteger> validBag = new ArrayList<>(Arrays.asList(new AtomicInteger(1),
    new AtomicInteger(2), new AtomicInteger(3), new AtomicInteger(4),
    new AtomicInteger(5), new AtomicInteger(6), new AtomicInteger(7),
    new AtomicInteger(8),new AtomicInteger(9), new AtomicInteger(10),
    new AtomicInteger(11)));

    blackBags.add(validBag);
    blackBags.add(validBag);
    blackBags.add(validBag);
    
    whiteBags.add(new ArrayList<>());
    whiteBags.add(new ArrayList<>());
    whiteBags.add(new ArrayList<>());

    ArrayList<Player> players = new ArrayList<>(Arrays.asList(player));
    pebbleGame.setPlayers(players);
    pebbleGame.setTurn(0);
    pebbleGame.setFinished(false);
    pebbleGame.setNumPlayers(1);
    pebbleGame.setBlackbags(blackBags);
    pebbleGame.setWhitebags(whiteBags);

    player.setBlackNumber(0);
    player.setChoice(0);
    player.setPebbles(new ArrayList<>());
    player.setTotalWeight(0);

    out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out)); // Sets the output stream, so that it can be read.
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

  /**
   * This method is used to test inputs.
   * It changes the input stream to the values needed.
   * 
   * @param input the value to be inputted.
   */
  public void testInput(String input){
    ByteArrayInputStream in = new ByteArrayInputStream((input).getBytes());
    System.setIn(in); // Reassigns the system.in so that it is the simulated input.
    pebbleGame.setScanner(new Scanner(System.in)); //Set scanner to correct input stream.
  }

  /**
   * This is the intergration test for pebbleGame.
   * 
   * The first tests are to test that each input will exit immediatly upon entering e.
   * 
   * The second tests is to check that you cannot enter a negative number or string of players.
   */
  @Test
  public void pebbleGameTest(){

    //Testing that e will stop at each input, currently when prompted to enter the number of players.
    assertEquals(false, pebbleGame.getFinished());

    testInput("E"+System.lineSeparator()+"");
    pebbleGame.setUp();

    assertEquals(true, pebbleGame.getFinished());
    assertTrue(out.toString().contains("Please enter the number of players:"));
    assertFalse(out.toString().contains("Please enter a location of bag number 0 to load:")); //Should stop immediately

    // Testing that at the first location of the bag should exit on entering e.
    setupEmpty();
    assertEquals(false, pebbleGame.getFinished());

    testInput("3"+System.lineSeparator()+"E");
    pebbleGame.setUp();

    assertEquals(true, pebbleGame.getFinished());
    assertTrue(out.toString().contains("Please enter the number of players:"));
    assertTrue(out.toString().contains("Please enter a location of bag number 0 to load:"));
    assertFalse(out.toString().contains("Please enter a location of bag number 1 to load:")); //Should stop immediately

    // Testing that at the second location of the bag should exit on entering e.
    setupEmpty();
    assertEquals(false, pebbleGame.getFinished());

    testInput("3"+System.lineSeparator()+validFile+System.lineSeparator()+"E");
    pebbleGame.setUp();

    assertEquals(true, pebbleGame.getFinished());
    assertTrue(out.toString().contains("Please enter the number of players:"));
    assertTrue(out.toString().contains("Please enter a location of bag number 0 to load:"));
    assertTrue(out.toString().contains("Please enter a location of bag number 1 to load:"));
    assertFalse(out.toString().contains("Please enter a location of bag number 2 to load:")); //Should stop immediately

    // Testing that at the third location of the bag should exit on entering e.
    setupEmpty();
    assertEquals(false, pebbleGame.getFinished());

    testInput("3"+System.lineSeparator()+validFile+System.lineSeparator()+validFile+System.lineSeparator()+"E");
    pebbleGame.setUp();

    assertEquals(true, pebbleGame.getFinished());
    assertTrue(out.toString().contains("Please enter the number of players:"));
    assertTrue(out.toString().contains("Please enter a location of bag number 0 to load:"));
    assertTrue(out.toString().contains("Please enter a location of bag number 1 to load:"));
    assertTrue(out.toString().contains("Please enter a location of bag number 2 to load:"));
    assertFalse(out.toString().contains("Please enter a pebble to discard:")); //Should stop immediately

    // Testing that at the first player discarding a pebble it should stop.
    setupEmpty();
    assertEquals(false, pebbleGame.getFinished());

    testInput("3"+System.lineSeparator()+validFile+System.lineSeparator()+validFile+System.lineSeparator()+
    validFile+System.lineSeparator()+"E");
    pebbleGame.setUp();

    assertEquals(true, pebbleGame.getFinished());
    assertTrue(out.toString().contains("Please enter the number of players:"));
    assertTrue(out.toString().contains("Please enter a location of bag number 0 to load:"));
    assertTrue(out.toString().contains("Please enter a location of bag number 1 to load:"));
    assertTrue(out.toString().contains("Please enter a location of bag number 2 to load:"));
    assertTrue(out.toString().contains("Player 1's turn"));
    assertFalse(out.toString().contains("Player 2's turn")); //Should stop immediately

    // Testing that at the second player discarding a pebble it should stop.
    setupEmpty();
    assertEquals(false, pebbleGame.getFinished());

    // Gets the list of player, gets the first players pebbles, get the first pebble and turns it into a string.
    testInput("3"+System.lineSeparator()+samefile+System.lineSeparator()+samefile+System.lineSeparator()+
    samefile+System.lineSeparator()+11+System.lineSeparator()+"E");

    pebbleGame.setUp();

    assertEquals(true, pebbleGame.getFinished());
    assertTrue(out.toString().contains("Please enter the number of players:"));
    assertTrue(out.toString().contains("Please enter a location of bag number 0 to load:"));
    assertTrue(out.toString().contains("Please enter a location of bag number 1 to load:"));
    assertTrue(out.toString().contains("Please enter a location of bag number 2 to load:"));
    assertTrue(out.toString().contains("Player 1's turn"));
    assertTrue(out.toString().contains("Player 2's turn"));
    assertFalse(out.toString().contains("Player 3's turn")); //Should stop immediately

    //Testing that a negative number of players will result in it being asked again.
    setupEmpty();
    assertEquals(false, pebbleGame.getFinished());

    testInput("-1"+System.lineSeparator()+"1"+System.lineSeparator()+"E");
    pebbleGame.setUp();

    assertEquals(true, pebbleGame.getFinished());
    assertTrue(out.toString().contains("Please enter the number of players:"));
    assertTrue(out.toString().contains("Please enter a location of bag number 0 to load:"));
    assertFalse(out.toString().contains("IOException"));

    //Testing that a string inputted as a number of players will result in it being asked again.
    setupEmpty();
    assertEquals(false, pebbleGame.getFinished());

    testInput("string"+System.lineSeparator()+"1"+System.lineSeparator()+"E");
    pebbleGame.setUp();

    assertEquals(true, pebbleGame.getFinished());
    assertTrue(out.toString().contains("Please enter the number of players:"));
    assertTrue(out.toString().contains("Please enter a location of bag number 0 to load:"));
    assertFalse(out.toString().contains("IOException"));
  }

  /**
   * This is a unit test of the playerthread.
   * This acts as if there is one player and it is a single thread.
   * 
   * The first test is to test that inputting e ends the game.
   * 
   * The second test is to test that a total of 100 at the start would lead to a player to win.
   * 
   * The third test is to test that a total of 100 will lead to a player to win.
   * 
   * The fourth test is to test that will all values being correct,
   * a player can remove a pebble and have a random one added again.
   * 
   * The fifth test is to test if inputting an incorrect integer value
   * will result in a error before requiring another input.
   * 
   * The sixth test is to test if inputting a string value will result
   * in an error before requiring another input.
   */
  @Test
  public void playerThreadTest(){

    //Test that if e is entered the game ends.

    testInput("E");
    pebbleGame.playerThead();

    assertEquals(0, player.getPebbles().size()); //Check no pebble was removed.
    assertEquals(true, pebbleGame.getFinished());

    //Reset values, Test that starting with a 100 weight leads to a win.
    setupEmpty();
    
    player.addPebble(100); //Total weight now equals 100.
    assertEquals(1, player.getPebbles().size());
    assertEquals(100, player.getTotalWeight());
    assertEquals(false, pebbleGame.getFinished());

    pebbleGame.playerThead();

    assertEquals(1, player.getPebbles().size()); //Check no pebble was removed.
    assertEquals(100, player.getTotalWeight());
    assertEquals(true, pebbleGame.getFinished());
    assertTrue(out.toString().contains("Player 1 wins"));

    //Reset values, Test that removing and adding a new pebble leads to a win.
    setupEmpty();
    ArrayList<AtomicInteger> tenBag = new ArrayList<>(Arrays.asList(new AtomicInteger(10),
    new AtomicInteger(10), new AtomicInteger(10), new AtomicInteger(10),
    new AtomicInteger(10), new AtomicInteger(10), new AtomicInteger(10),
    new AtomicInteger(10),new AtomicInteger(10), new AtomicInteger(10),
    new AtomicInteger(10)));

    pebbleGame.setBlackbags(new ArrayList<>(Arrays.asList(tenBag,tenBag,tenBag)));

    player.addPebble(90); //Total weight now equals 90.
    player.addPebble(20); //Total weight now equals 110.
    assertEquals(2, player.getPebbles().size());
    assertEquals(110, player.getTotalWeight());
    assertEquals(false, pebbleGame.getFinished());

    testInput("20"); //Should remove the 20 and replace it with a 10, resulting in a win.
    pebbleGame.playerThead();

    assertEquals(2, player.getPebbles().size()); // Size should remain constant
    assertEquals(100, player.getTotalWeight());
    assertEquals(true, pebbleGame.getFinished());
    assertTrue(out.toString().contains("Player 1 wins"));

    //Reset values, Test that successfully removes pebble and adds a new one.
    setupValid();
    player.setPebbles(new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10)));
    player.setTotalWeight(55);

    assertEquals(10, player.getPebbles().size());
    assertEquals(55, player.getTotalWeight());
    assertEquals(false, pebbleGame.getFinished());
    assertEquals(1, pebbleGame.getNumPlayers());
    assertEquals(11, pebbleGame.getBlackbags().get(player.getBlackNumber()).size());
    assertEquals(0, pebbleGame.getWhitebags().get(player.getBlackNumber()).size());

    testInput("2"); // Should remove this pebble and then add a random other
    pebbleGame.playerThead();

    assertEquals(10, player.getPebbles().size());
    assertTrue(2 != player.getPebbles().get(1)); // Check 2 was removed.
    assertTrue(10 == player.getPebbles().get(8)); // Check 10 was shifted down an index.
    assertTrue(1 <= player.getPebbles().get(9) && 11 >= player.getPebbles().get(9)); // A random number was added from the bags.
    assertEquals(false, pebbleGame.getFinished());
    assertEquals(1, pebbleGame.getNumPlayers());

    //Reset values, test that trying to add an invalid integer will result in an error.
    setupValid();
    player.setPebbles(new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10)));
    player.setTotalWeight(55);

    assertEquals(10, player.getPebbles().size());
    assertEquals(55, player.getTotalWeight());
    assertEquals(false, pebbleGame.getFinished());
    assertEquals(1, pebbleGame.getNumPlayers());
    assertEquals(11, pebbleGame.getBlackbags().get(player.getBlackNumber()).size());
    assertEquals(0, pebbleGame.getWhitebags().get(player.getBlackNumber()).size());

    testInput("100"+System.lineSeparator()+"2"); // Should produce an error then ask for another pebble, where it works correctly.
    pebbleGame.playerThead();

    assertTrue(out.toString().contains("Please enter a valid pebble.")); // Check error was found.
    assertTrue(out.toString().contains("NullPointerException")); 
    assertEquals(10, player.getPebbles().size());
    assertTrue(2 != player.getPebbles().get(1)); // Check 2 was removed.
    assertTrue(10 == player.getPebbles().get(8)); // Check 10 was shifted down an index.
    assertTrue(1 <= player.getPebbles().get(9) && 11 >= player.getPebbles().get(9)); // A random number was added from the bags.
    assertEquals(false, pebbleGame.getFinished());
    assertEquals(1, pebbleGame.getNumPlayers());
    
    //Reset values, test that trying to add a string will result in an error.
    setupValid();
    player.setPebbles(new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10)));
    player.setTotalWeight(55);

    assertEquals(10, player.getPebbles().size());
    assertEquals(55, player.getTotalWeight());
    assertEquals(false, pebbleGame.getFinished());
    assertEquals(1, pebbleGame.getNumPlayers());
    assertEquals(11, pebbleGame.getBlackbags().get(player.getBlackNumber()).size());
    assertEquals(0, pebbleGame.getWhitebags().get(player.getBlackNumber()).size());

    testInput("Astring"+System.lineSeparator()+"2"); // Should produce an error then ask for another pebble, where it works correctly.
    pebbleGame.playerThead();

    assertTrue(out.toString().contains("Please enter a valid pebble.")); // Check error was found.
    assertEquals(10, player.getPebbles().size());
    assertTrue(2 != player.getPebbles().get(1)); // Check 2 was removed.
    assertTrue(10 == player.getPebbles().get(8)); // Check 10 was shifted down an index.
    assertTrue(1 <= player.getPebbles().get(9) && 11 >= player.getPebbles().get(9)); // A random number was added from the bags.
    assertEquals(false, pebbleGame.getFinished());
    assertEquals(1, pebbleGame.getNumPlayers());
  }

  /**
   * This is a unit test on the method initialDrawer.
   * 
   * This tests that the initial drawer will transfer
   * ten pebbles to the player, leaving at least one
   * ebble inside each of the black bags.
   */
  @Test
  public void initialDrawerTest(){

    setupValid();

    player.setPebbles(new ArrayList<>());
    assertEquals(0, player.getPebbles().size());
    assertEquals(1, pebbleGame.getNumPlayers());
    assertEquals(11, pebbleGame.getBlackbags().get(player.getBlackNumber()).size());
    assertEquals(0, pebbleGame.getWhitebags().get(player.getBlackNumber()).size());

    pebbleGame.initialDrawer(player);

    assertEquals(10, player.getPebbles().size());
    assertEquals(1, pebbleGame.getBlackbags().get(player.getBlackNumber()).size());
    assertEquals(0, pebbleGame.getWhitebags().get(player.getBlackNumber()).size());
  }

  /**
   * This is the unit test for the method drawerTest.
   * 
   * The first test checks that a pebble will be drawn to the
   * said player when the black bags are filled but the white bags are empty.
   * 
   * The second test checks that a pebble will be drawn to the
   * said player when the white bags are filled but the black bags are empty.
   * This requires at least once for the white bags to swap pebbles with the black bags.
   */
  @Test
  public void drawerTest(){

    setupValid();

    player.setPebbles(new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10)));
    assertEquals(1, pebbleGame.getNumPlayers());
    assertEquals(10, player.getPebbles().size());
    assertEquals(11, pebbleGame.getBlackbags().get(player.getBlackNumber()).size());
    assertEquals(0, pebbleGame.getWhitebags().get(player.getBlackNumber()).size());

    pebbleGame.drawer(player);

    assertEquals(11, player.getPebbles().size());
    assertEquals(10, pebbleGame.getBlackbags().get(player.getBlackNumber()).size());
    assertEquals(0, pebbleGame.getWhitebags().get(player.getBlackNumber()).size());

    //Test with all white bags filled and black bags empty.
    ArrayList<AtomicInteger> validBag = new ArrayList<>(Arrays.asList(new AtomicInteger(1),
    new AtomicInteger(2), new AtomicInteger(3), new AtomicInteger(4),
    new AtomicInteger(5), new AtomicInteger(6), new AtomicInteger(7),
    new AtomicInteger(8),new AtomicInteger(9), new AtomicInteger(10),
    new AtomicInteger(11)));

    pebbleGame.setBlackbags(new ArrayList<>(Arrays.asList(new ArrayList<>(),new ArrayList<>(),new ArrayList<>())));
    pebbleGame.setWhitebags(new ArrayList<>(Arrays.asList(validBag,validBag,validBag)));

    player.setPebbles(new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10)));
    assertEquals(1, pebbleGame.getNumPlayers());
    assertEquals(10, player.getPebbles().size());
    assertEquals(0, pebbleGame.getBlackbags().get(player.getBlackNumber()).size());
    assertEquals(11, pebbleGame.getWhitebags().get(player.getBlackNumber()).size());

    pebbleGame.drawer(player);

    assertEquals(11, player.getPebbles().size());
    assertEquals(10, pebbleGame.getBlackbags().get(player.getBlackNumber()).size());
    assertEquals(0, pebbleGame.getWhitebags().get(player.getBlackNumber()).size());
  }

  /**
   * This is a unit test of the method readerTest.
   * 
   * The first three tests are done to check that with a valid CSV file
   * and a varying number of players, there will be enough total pebbles
   * in bags for the players.
   * 
   * The second set of tests are done to check that a file with negative values
   * in it will be caught and an NegativeWeightPebbleException will be thrown. 
   * 
   * The third set of test have been done to test that a file with too few pebbles
   * in it (which would result in too few pesbbles for players) will be caught and an TooFewValuesException will be thrown.
   */
  @Test
  public void readerTest(){
    ArrayList<AtomicInteger> pebbles;

    try {
      //Testing using one player, valid csv file.
      pebbleGame.setNumPlayers(1);
      pebbles = pebbleGame.reader(validFile);
      assertTrue(11 <= pebbles.size()); //Checking that the number of pebbles is greater than minimum.

      //Testing using a reasonable number of players
      pebbleGame.setNumPlayers(4);
      pebbles = pebbleGame.reader(validFile);
      assertTrue(44 <= pebbles.size()); //Checking that the number of pebbles is greater than minimum.

      //Testing using hundred players, valid csv file.
      pebbleGame.setNumPlayers(100);
      pebbles = pebbleGame.reader(validFile);
      assertTrue(1100 <= pebbles.size()); //Checking that the number of pebbles is greater than minimum.
    } catch (IOException | NegativeWeightException | TooFewValuesException e) {
      fail("Invalid Exception");
    }

    //Testing using one player, csv file would result in negative pebbles in the bags.
    try {
      pebbleGame.setNumPlayers(1);
      pebbleGame.reader(negativeFile); // A negative value in the file should result in a NegativeWeightPebbleException. 
      fail("No NegativeWeightPebbleException");
    } catch (IOException | TooFewValuesException e){
      fail("Invalid Exception");
    } catch (NegativeWeightException e){}

    //Testing using hundred players, csv file would result in negative pebbles in the bags.
    try {
      pebbleGame.setNumPlayers(100);
      pebbleGame.reader(negativeFile); // A negative value in the file should result in a NegativeWeightPebbleException. 
      fail("No NegativeWeightPebbleException");
    } catch (IOException | TooFewValuesException e){
      fail("Invalid Exception");
    } catch (NegativeWeightException e){}

    //Testing using one player, csv file would result in too few pebbles for the players.
    try {
      pebbleGame.setNumPlayers(1);
      pebbleGame.reader(tooFewFile);
      fail("No TooFewValuesException"); // Too few pebbles in the file should result in a TooFewValuesException.
    } catch (IOException | NegativeWeightException e){
      fail("Invalid Exception");
    } catch (TooFewValuesException e){}

    //Testing using hundred players, csv file would result in too few pebbles for the players.
    try {
      pebbleGame.setNumPlayers(100);
      pebbleGame.reader(tooFewFile);
      fail("No TooFewValuesException"); // Too few pebbles in the file should result in a TooFewValuesException.
    } catch (IOException | NegativeWeightException e){
      fail("Invalid Exception");
    } catch (TooFewValuesException e){}
  }

  /**
   * This a unit test the method discardWriter.
   * This method produces the file when a player discards a pebble.
   * 
   * The first test is to check the file produced is valid a lower bound
   * and the second test is to check the upper bound but also to check
   * proper appending of text in the files.
   */
  @Test
  public void discardWriterTest(){

    //Testing with a choice of 2 and a bag of A
    setupEmpty();
    player.setPebbles(new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10)));
    assertEquals(10, player.getPebbles().size());
    player.setChoice(2);

    pebbleGame.discardWriter(player);

    assertEquals(10, player.getPebbles().size()); //Should have no impact on player's pebbles
    String file = reader(playerFile);
    assertEquals("player1 has discarded a "+player.getChoice()+" to bag Aplayer1 hand is 1, 2, 3, 4, 5, 6, 7, 8, 9, 10", file);

    //Testing that the file has appended correctly and with a choice of 22 and a bag of C.
    setupEmpty();
    player.setPebbles(new ArrayList<>(Arrays.asList(6,2,8,22)));
    player.setChoice(22);
    player.setBlackNumber(2);

    pebbleGame.discardWriter(player);

    assertEquals(4, player.getPebbles().size()); //Should have no impact on player's pebbles
    file = reader(playerFile);
    assertEquals("player1 has discarded a 2 to bag Aplayer1 hand is 1, 2, 3, 4, 5, 6, 7, 8, 9, 10"+
    "player1 has discarded a "+player.getChoice()+" to bag Cplayer1 hand is 6, 2, 8, 22", file);
  }

  /**
   * This is a unit test for the method drawWriter.
   * The method is used to create a file when a player draws a pebble. 
   * 
   * The first test is to check the file produced is valid a lower bound
   * and the second test is to check the upper bound but also to check
   * proper appending of text in the files.
   */
  @Test
  public void drawWriterTest(){

    //Testing with a bag of X
    player.setPebbles(new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10)));
    assertEquals(10, player.getPebbles().size());

    pebbleGame.drawWriter(player);

    assertEquals(10, player.getPebbles().size()); //Should have no impact on player's pebbles
    String file = reader(playerFile);
    assertEquals("player1 has drawn a "+player.getPebbles().get(player.getPebbles().size()-1)+
    " from bag Xplayer1 hand is 1, 2, 3, 4, 5, 6, 7, 8, 9, 10", file);

    //Testing that the file has appended correctly and with a bag of Z.
    setupEmpty();
    player.setPebbles(new ArrayList<>(Arrays.asList(6,2,8,22)));
    player.setBlackNumber(2);

    pebbleGame.drawWriter(player);

    assertEquals(4, player.getPebbles().size()); //Should have no impact on player's pebbles
    file = reader(playerFile);
    assertEquals("player1 has drawn a 10 from bag Xplayer1 hand is 1, 2, 3, 4, 5, 6, 7, 8, 9, 10"+
    "player1 has drawn a "+player.getPebbles().get(player.getPebbles().size()-1)+" from bag Zplayer1 hand is 6, 2, 8, 22", file);
  }
}