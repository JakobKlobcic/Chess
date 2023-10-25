package dataAccess;

import chess.ChessGame;
import chess.ChessGameImplementation;
import models.Game;
import models.User;

import java.util.*;

/**
 * The GameDAO class is responsible for CRUD operations
 * on the Game entities in the database.
 *
 * @author Jakob Klobcic
 */
public class GameDAO {
    /**
     * Finds a game by the provided ID.
     *
     * @param id the ID of the game to be found
     * @return returns Game or null if not found
     */
    Set<Game> games = new HashSet<>();
    public Game findGame(int id){
        for (Game game : games) {
            if (game.getGameID() == id) {
                return game;
            }
        }
        return null;
    }

    /**
     * Creates a new game entity.
     */
    public int createGame(String name){
        int id = generateId();
        games.add(new Game(id,null,null,name, new ChessGameImplementation()));
        return id;
    }

    public Set<Game> getGames(){
        return games;
    }

    public boolean nameExists(String name){
        for (Game game : games) {
            if (game.getGameName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Updates an existing game entity.
     */
    void updateGame(int id){}

    /**
     * Deletes an existing game entity.
     */
    void deleteGame(int id){}

    int generateId() {
        Random rand = new Random();
        int id = rand.nextInt((99999 - 10000) + 1) + 10000;

        for (Game game : games) {
            if (game.getGameID() == id) {
                return generateId();
            }
        }
        return id;
    }
}