package models;

import enumerations.GameState;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameTest {
    private Game game;
    private User user1, user2;

    public GameTest() {
    }

    @Before
    public void setUp() throws Exception {
        user1 = new User("current");
        user2 = new User("other");

        List<String> playerNames = new ArrayList<>();
        playerNames.add(user1.getName());
        playerNames.add(user2.getName());

        game = new Game(1, playerNames, "playing");

    }

    @Test
    public void getID() throws Exception {
        assertEquals("returns game ID",
                game.getID(), 1);
    }

    @Test
    public void getGameState() throws Exception {
        assertEquals("returns game state",
                game.getGameState(), GameState.PLAYING);
    }

    @Test
    public void hasPlayer() throws Exception {
        assertTrue("returns true if game contains given player",
                game.hasPlayer(user1));
        assertTrue("returns true if game contains player with equal name",
                game.hasPlayer(new User(user1.getName())));
    }

    @Test
    public void getOpponent() throws Exception {
        assertEquals("returns the other player",
                game.getOpponent(user1), user2);
    }

    @Test
    public void getPlayers() throws Exception {
        assertTrue("returns all players",
                game.getPlayers().containsAll(Arrays.asList(user1, user2)));
    }
}