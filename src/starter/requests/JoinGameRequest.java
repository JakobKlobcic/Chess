package requests;

/**
 * The JoinGameRequest class represents a specialized HTTP request for joining a game,
 * extending the base Request class.
 */
public class JoinGameRequest extends Request{
    String playerColor;
    Integer gameID;

    public String getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }
}
