package requests;

/**
 * The CreateGameRequest class represents a specialized HTTP request for creating a game,
 * extending the base Request class.
 */
public class CreateGameRequest extends Request{
	/**
	 * Name of the game to be created
	 */
	String gameName;

	public String getGameName(){
		return gameName;
	}

	public void setGameName(String gameName){
		this.gameName = gameName;
	}
}
