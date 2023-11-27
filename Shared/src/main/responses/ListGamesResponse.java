package responses;

import models.Game;

import java.util.Set;

/**
 * The ListGamesResponse class represents a specialized HTTP response for listing all games,
 * extending the base Response class.
 */
public class ListGamesResponse extends Response{
	/**
	 * List of all the games
	 */
	Set<Game> games;

	public void setGames(Set<Game> games){
		this.games = games;
	}

	public Set<Game> getGames(){
		return games;
	}
}
