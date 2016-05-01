package database.access;

import models.Tile;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameDAOTest {

    @Test
    public void buildPlacedTiles() {
        String woorddeel = "A,B,C";
        String xCords = "1,1,1";
        String yCords = "10,11,12";
        ArrayList<Tile> tiles = GameDAO.buildPlacedTiles(woorddeel,xCords,yCords);
        assertEquals("first tile should have char A",
                tiles.get(0).getCharacter(),
                new Character('A'));
        assertEquals("second tile should have x 1",
                tiles.get(1).getX(),
                1);
        assertEquals("third tile should have y 12",
                tiles.get(2).getY(),
                12);
    }

    @Test
    public void buildRack() {
        String chars = "F,O,O,B,A,R,Z";
        assertEquals("it should have 7 tile objects",
                GameDAO.buildRack(chars).size(),
                7);
    }
}