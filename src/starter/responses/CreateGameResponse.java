package responses;

/**
 * The CreateGameResponse class represents a specialized HTTP response for creating a game,
 * extending the base Response class.
 */
public class CreateGameResponse extends Response{
    Integer gameID;

    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }
}
