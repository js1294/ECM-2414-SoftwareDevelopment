package src;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.*;

import src.PebbleGame.Player;

public class PebbleGameTest {
  @Test
  public void playerTest(){
    Player player = new Player();
    ArrayList<Integer> pebbles = new ArrayList<>();

    //Testing initial values
    assertEquals(pebbles, player.getPebbles());
    assertEquals(0, player.getBlackNumber());
    assertEquals(0,player.getTotalWeight());
    assertEquals(0, player.getChoice());

    //Testing adding a value
    player.addPebble(20);
    pebbles.add(20);
    assertEquals(pebbles, player.getPebbles());
    assertEquals(20, player.getTotalWeight());
  }
}

