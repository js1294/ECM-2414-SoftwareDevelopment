package src;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.*;
import java.util.InputMismatchException;
import java.util.ArrayList;
import src.PebbleGame.Player;

public class PlayerTest {
    
  private static Player player;

  private static ArrayList<Integer> pebbles;

  @BeforeClass
  public static void setUpClass(){
    player = new Player();
    pebbles = new ArrayList<>();
  }

  @Test
  public void playerTest(){
    //Testing initial values.
    assertEquals(pebbles, player.getPebbles());
    assertEquals(0, player.getBlackNumber());
    assertEquals(0,player.getTotalWeight());
    assertEquals(0, player.getChoice());

    //Testing adding a pebble.
    player.addPebble(20);
    pebbles.add(20);
    assertEquals(pebbles, player.getPebbles());
    assertEquals(20, player.getTotalWeight());

    //Testing adding a negative weight pebble.
    try {
      player.addPebble(-20);
      fail("No InputMismatchException");
    } catch (InputMismatchException e) {}
    assertEquals(pebbles, player.getPebbles());
    assertEquals(20, player.getTotalWeight());

    //Testing removing a pebble.
    player.removePebble(20);
    pebbles.remove(0);
    assertEquals(pebbles, player.getPebbles());
    assertEquals(0, player.getTotalWeight());

    //Testing removing from an empty list
    try {
        player.removePebble(20);
        fail("No NullPointerException");
      } catch (NullPointerException e) {}
      assertEquals(pebbles, player.getPebbles());
      assertEquals(0, player.getTotalWeight());

    //Testing removing an item that isn't in the list
    player.addPebble(20);
    pebbles.add(20);
    player.addPebble(43);
    pebbles.add(43);
    try {
        player.removePebble(23);
        fail("No NullPointerException");
      } catch (NullPointerException e) {}
      assertEquals(pebbles, player.getPebbles());
      assertEquals(63, player.getTotalWeight());
    }
}
