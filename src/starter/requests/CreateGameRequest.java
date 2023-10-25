package requests;

/**
 * The CreateGameRequest class represents a specialized HTTP request for creating a game,
 * extending the base Request class.
 */
public class CreateGameRequest extends Request{
    String gameName;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
