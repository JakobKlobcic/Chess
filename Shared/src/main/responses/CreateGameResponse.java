package responses;

/**
 * The CreateGameResponse class represents a specialized HTTP response for creating a game,
 * extending the base Response class.
 */
public class CreateGameResponse extends Response{
	/**
	 * ID of the newly generated game
	 */
	Integer gameID;

	public void setGameID(Integer gameID){
		this.gameID = gameID;
	}

	public Integer getGameID(){
		return gameID;
	}
}
