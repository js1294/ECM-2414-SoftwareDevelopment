package src;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.*;
import org.junit.rules.TemporaryFolder;


public class PebbleGameTest {

  private static PebbleGame pebbleGame;

  private String validFile;

  private String tooLowFile;

  private String negativeFile;
  
  private String tooFewFile;

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @BeforeClass
  public static void setup(){
    pebbleGame = new PebbleGame();
  }

  @Before
  public void fileSetup(){
    validFile = "valid.csv";
    tooLowFile = "tooLowFile.csv";
    negativeFile = "negativeFile.csv";
    tooFewFile = "tooFewFile.csv";
    writer(validFile, "1,2,3,4,5,6,7,8,9,10,11");
    writer(tooLowFile, "1,1,1,1,1,1,1,1,1,1,1");
    writer(negativeFile, "-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,100");
    writer(tooFewFile, "100,100");
  }

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

    ///Testing using hundred players, csv file which would result in no player being able to win.
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
}
