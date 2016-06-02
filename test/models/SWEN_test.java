package models;

/**
 * Created by jeroen on 2-6-2016.
 */
import database.access.GameDAO;
import enumerations.BoardType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class SWEN_test {


    private TurnBuilder turnbuilder;
    private GameDAO gameDAO;

    @Before
    public void setUp() throws Exception {
        turnbuilder = new TurnBuilder();
        gameDAO = new GameDAO();
        turnbuilder.setGameBoard(gameDAO.selectFieldsForBoard(BoardType.STANDARD));

    }

    // Manier van Jeroen
    @Test
    public void fieldsChanged(){

        Tile a = new Tile('A');
        Tile p = new Tile('P');
        turnbuilder.getGameBoard()[7][7].setTile(a);
        turnbuilder.getGameBoard()[7][8].setTile(a);
        turnbuilder.getGameBoard()[7][9].setTile(p);

        for(int i = 0; i < turnbuilder.getGameBoard().length; i ++){
            for(int x = 0; x < turnbuilder.getGameBoard().length; x ++) {
                if(x == (turnbuilder.getGameBoard().length -1)){
                    System.out.println(turnbuilder.getGameBoard()[ i ][ x ].toString());
                } else {
                    System.out.print(turnbuilder.getGameBoard()[ i ][ x ].toString());
                }

            }
        }

        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[8][8], new Tile(0, 1, 'A'));
        // turnbuilder.verifyAndCalculate();
        System.out.println(turnbuilder.getScore());
    }

    // Manier van Christoph
  /*  @Before
    public void setUp() throws Exception {

        user1 = new User("ger");
        user2 = new User("bob");
        game = new Game(0,0,0, user1, user2, GameState.PLAYING, BoardType.STANDARD, Language.NL, ReactionType.ACCEPTED);

        turnbuilder = new TurnBuilder();
        game.setTurnBuilder(turnbuilder);

        gameDAO = new GameDAO();
        turnbuilder.setGameBoard(gameDAO.selectFieldsForBoard(BoardType.STANDARD));
        game.setBoard(turnbuilder.getGameBoard());


        *//*turnbuilder.getGameBoard()[8][1].setTile(new Tile('D'));
        turnbuilder.getGameBoard()[8][2].setTile(new Tile('U'));
        turnbuilder.getGameBoard()[8][3].setTile(new Tile('B'));
        turnbuilder.getGameBoard()[8][4].setTile(new Tile('B'));
        turnbuilder.getGameBoard()[8][5].setTile(new Tile('E'));
        turnbuilder.getGameBoard()[8][6].setTile(new Tile('L'));
        turnbuilder.getGameBoard()[8][7].setTile(new Tile('D'));
        turnbuilder.getGameBoard()[8][8].setTile(new Tile('A'));
        turnbuilder.getGameBoard()[8][9].setTile(new Tile('V'));
        turnbuilder.getGameBoard()[8][10].setTile(new Tile('E'));*//*

        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[8][0],new Tile(0,2,'D'));
        turn = new Turn(0,turnbuilder.getScore(), user1, TurnType.WORD);
        game.addTurn(turn);

        System.out.println(turn.getScore());
    }*/
}
