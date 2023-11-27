package models;

import chess.ChessGame;
import chess.ChessGameImplementation;

import java.util.HashSet;
import java.util.Set;

/**
 * The Game class represents a chess game with associated data and players.
 */
public class Game{

	/**
	 * The unique identifier for the chess game.
	 */
	int gameID;

	/**
	 * The username of the player using the white chess pieces.
	 */
	String whiteUsername;

	/**
	 * The username of the player using the black chess pieces.
	 */
	String blackUsername;

	/**
	 * The name or title of the chess game.
	 */
	String gameName;

	/**
	 * The instance of the chess game, representing the current state of the chessboard.
	 */
	ChessGameImplementation game;

	Set<String> spectators = new HashSet<>();

	public Game(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGameImplementation game){
		this.gameID = gameID;
		this.whiteUsername = whiteUsername;
		this.blackUsername = blackUsername;
		this.gameName = gameName;
		this.game = game;
	}

	@Override
	public boolean equals(Object obj){
        if(obj == null){
            return false;
        }
		Game g = (Game)obj;
		return g.gameID == this.gameID;
	}

	public int getGameID(){
		return gameID;
	}

	public String getWhiteUsername(){
		return whiteUsername;
	}

	public void setWhiteUsername(String whiteUsername){
		this.whiteUsername = whiteUsername;
	}

	public String getBlackUsername(){
		return blackUsername;
	}

	public void setBlackUsername(String blackUsername){
		this.blackUsername = blackUsername;
	}

	public String getGameName(){
		return gameName;
	}

	public ChessGame getGame(){
		return game;
	}

	public void setGame(ChessGame game){
		this.game =(ChessGameImplementation) game;
	}

	public void addSpectator(String username){
		spectators.add(username);
	}

	public void removeSpectator(String username){
		spectators.remove(username);
	}

	public Set<String> getSpectators(){
		return spectators;
	}

	public void setSpectators(Set<String> spectators){
		this.spectators=spectators;
	}


}
