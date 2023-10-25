package responses;

import models.Game;

import java.util.Set;

/**
 * The ListGamesResponse class represents a specialized HTTP response for listing all games,
 * extending the base Response class.
 */
public class ListGamesResponse extends Response{
    Set<Game> games;

    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }
}
