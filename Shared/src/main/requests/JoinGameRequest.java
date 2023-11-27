package requests;

/**
 * The JoinGameRequest class represents a specialized HTTP request for joining a game,
 * extending the base Request class.
 */
public class JoinGameRequest extends Request{
	/**
	 * Color the player wishes to be
	 */
	String playerColor;
	/**
	 * The id of the game the user wishes to join
	 */
	Integer gameID;

	public String getPlayerColor(){
		return playerColor;
	}

	public Integer getGameID(){
		return gameID;
	}

	public void setGameID(Integer gameID){
		this.gameID = gameID;
	}

	public void setPlayerColor(String playerColor){
		this.playerColor = playerColor;
	}
}
