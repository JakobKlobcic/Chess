package services;

import chess.ChessGame;
import chess.ChessGameImplementation;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import models.Authtoken;
import models.Game;
import models.User;
import requests.*;
import responses.*;

import java.util.Objects;

public class Services{
	static GameDAO gameData = new GameDAO();
	static AuthDAO authData = new AuthDAO();
	static UserDAO userData = new UserDAO();

	/**
	 * Processes a request to clear application data and returns the corresponding response.
	 *
	 * @param request The {@code ClearApplicationRequest} object containing any parameters or criteria for clearing
     *                   data.
	 * @return A {@code ClearApplicationResponse} object representing the result of the clear application data
     * operation.
	 */
	public static ClearApplicationResponse clearApplication(ClearApplicationRequest request){
		ClearApplicationResponse res = new ClearApplicationResponse();
		gameData = new GameDAO();
		authData = new AuthDAO();
		userData = new UserDAO();

		if(false){//TODO: some other error implementation
			res.setStatus(500);
			res.setMessage("Error: description");
			return res;
		}

		res.setStatus(200);
		return res;
	}

	/**
	 * Processes a user request to create a game and returns the corresponding create game response.
	 *
	 * @param request The {@code CreateGameRequest} object containing information about the game being created.
	 * @return A {@code CreateGameResponse} object representing the result of the create game operation.
	 */
	public static CreateGameResponse createGame(CreateGameRequest request){

		CreateGameResponse res = new CreateGameResponse();

		if(!(request.getHeader("authorization") != null && authData.tokenExists(request.getHeader("authorization")))){
			res.setStatus(401);
			res.setMessage("Error: unauthorized");
			return res;
		}
		if(!(request.getGameName() != null && !request.getGameName().isEmpty())){
			res.setStatus(400);
			res.setMessage("Error: bad request");
			return res;
		}
		if(gameData.nameExists(request.getGameName())){
			res.setStatus(403);
			res.setMessage("Error: already taken");
			return res;
		}

		int id = gameData.createGame(request.getGameName());

		if(false){//TODO: some other error implementation
			res.setStatus(500);
			res.setMessage("Error: description");
			return res;
		}

		res.setGameID(id);
		res.setStatus(200);
		return res;
	}

	/**
	 * Processes a user request to join a game and returns the corresponding join game response.
	 *
	 * @param request The {@code JoinGameRequest} object containing information about the game to join.
	 * @return A {@code JoinGameResponse} object representing the result of the join game operation.
	 */
	/*public static JoinGameResponse joinGame(JoinGameRequest request){
		JoinGameResponse res = new JoinGameResponse();
		res.setSuccess(false);
		if(!(request.getHeader("authorization") != null && authData.tokenExists(request.getHeader("authorization")))){
			res.setStatus(401);
			res.setMessage("Error: unauthorized");
			return res;
		}
		if(!(request.getPlayerColor() != null && !request.getPlayerColor().isEmpty()) || (!Objects.equals(request.getPlayerColor(), "BLACK") && !Objects.equals(request.getPlayerColor(), "WHITE")) || request.getGameID() == null){
			res.setStatus(400);
			res.setMessage("Error: bad request");
			return res;
		}
		Game game = gameData.findGame(request.getGameID());
		if( request.getPlayerColor().equals("WHITE") && game != null && game.getWhiteUsername() !=null ||
			request.getPlayerColor().equals("BLACK") && game != null && game.getBlackUsername() !=null){//TODO: if email or username already exist
			res.setStatus(403);
			res.setMessage("Error: already taken");
			return res;
		}
		if(false){//TODO: some other error implementation
			res.setStatus(500);
			res.setMessage("Error: description");
			return res;
		}

		res.setStatus(200);
		res.setSuccess(true);
		return res;
	}*/

	/**
	 * Processes a request to list available games and returns the corresponding response.
	 *
	 * @param request The {@code ListGamesRequest} object containing any parameters or criteria for listing games.
	 * @return A {@code ListGamesResponse} object representing the result of the list games operation.
	 */
	public static JoinGameResponse joinGame(JoinGameRequest request){
		JoinGameResponse res = new JoinGameResponse();
		res.setSuccess(false);
		if(!(request.getHeader("authorization") != null && authData.tokenExists(request.getHeader("authorization")))){
			res.setStatus(401);
			res.setMessage("Error: unauthorized");
			return res;
		}

		if(request.getGameID() == null) {
			res.setStatus(400);
			res.setMessage("Error: bad request");
			return res;
		} else if((request.getPlayerColor() == null || request.getPlayerColor().isEmpty())&& gameData.findGame(request.getGameID())!=null) {
			gameData.findGame(request.getGameID()).addSpectator(authData.username(request.getHeader("authorization")));

			res.setStatus(200);
			res.setSuccess(true);
			return res;
		}
		Game game = gameData.findGame(request.getGameID());
		if((!Objects.equals(request.getPlayerColor(), "BLACK") && !Objects.equals(request.getPlayerColor(), "WHITE")) || game == null) {
			res.setStatus(400);
			res.setMessage("Error: bad request");
			return res;
		}

		if((request.getPlayerColor().equals("WHITE") && game != null && game.getWhiteUsername() !=null) ||
				(request.getPlayerColor().equals("BLACK") && game != null && game.getBlackUsername() !=null)){
			res.setStatus(403);
			res.setMessage("Error: color already taken");
			return res;
		}

		// TODO: some other error implementation
		if(false){
			res.setStatus(500);
			res.setMessage("Error: description");
			return res;
		}

		if(request.getPlayerColor().equals("WHITE")){
			game.setWhiteUsername(authData.username(request.getHeader("authorization")));
		}else if(request.getPlayerColor().equals("BLACK")){
			game.setBlackUsername(authData.username(request.getHeader("authorization")));
		}
		res.setStatus(200);
		res.setSuccess(true);
		return res;
	}

	public static ListGamesResponse listGames(ListGamesRequest request){
		ListGamesResponse res = new ListGamesResponse();
		if(!(request.getHeader("authorization") != null && authData.tokenExists(request.getHeader("authorization")))){
			res.setStatus(401);
			res.setMessage("Error: unauthorized");
			return res;
		}
		if(false){//TODO: some other error implementation
			res.setStatus(500);
			res.setMessage("Error: description");
			return res;
		}

		res.setGames(gameData.getGames());
		res.setStatus(200);
		return res;
	}

	/**
	 * Processes a user login request and returns the corresponding login response.
	 *
	 * @param request The {@code LoginRequest} object containing user credentials for the login.
	 * @return A {@code LoginResponse} object representing the result of the login operation.
	 */
	public static LoginResponse login(LoginRequest request){
		LoginResponse res = new LoginResponse();
		if(!userData.authenticateUser(request.getUsername(), request.getPassword())){
			res.setStatus(401);
			res.setMessage("Error: unauthorized");
			return res;
		}
		String token = authData.createToken(request.getUsername());
		if(false){//TODO: some other error implementation
			res.setStatus(500);
			res.setMessage("Error: description");
			return res;
		}
		res.setUsername(request.getUsername());
		res.setAuthToken(token);
		res.setStatus(200);
		return res;
	}

	/**
	 * Processes a user logout request and returns the corresponding logout response.
	 *
	 * @param request The {@code LogoutRequest} object containing user information for the logout.
	 * @return A {@code LogoutResponse} object representing the result of the logout operation.
	 */
	public static LogoutResponse logout(LogoutRequest request){

		LogoutResponse res = new LogoutResponse();
		if(!(request.getHeader("authorization") != null && authData.tokenExists(request.getHeader("authorization")))){
			res.setStatus(401);
			res.setMessage("Error: unauthorized");
			return res;
		}
		authData.removeToken(request.getHeader("authorization"));
		if(false){//TODO: some other error implementation
			res.setStatus(500);
			res.setMessage("Error: description");
			return res;
		}

		res.setStatus(200);
		return res;
	}

	/**
	 * Processes a user registration request and returns the corresponding registration response.
	 *
	 * @param request The {@code RegisterRequest} object containing user information for the registration.
	 * @return A {@code RegisterResponse} object representing the result of the registration operation.
	 */
	public static RegisterResponse registerUser(RegisterRequest request){
		RegisterResponse res = new RegisterResponse();

		if( !(request.getUsername() != null && !request.getUsername().isEmpty()) ||
			!(request.getPassword() != null && !request.getPassword().isEmpty()) ||
			!(request.getEmail() != null && !request.getEmail().isEmpty())){
			res.setStatus(400);
			res.setMessage("Error: bad request");
			return res;
		}

		if(!userData.createUser(request.getUsername(), request.getPassword(), request.getEmail())){
			res.setStatus(403);
			res.setMessage("Error: already taken");
			return res;
		}
		String token = authData.createToken(request.getUsername());

		if(false){//TODO: some other error implementation
			res.setStatus(500);
			res.setMessage("Error: description");
			return res;
		}

		res.setUsername(request.getUsername());
		res.setAuthToken(token);
		res.setStatus(200);
		return res;
	}
}
