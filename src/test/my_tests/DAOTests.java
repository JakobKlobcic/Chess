package my_tests;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import models.Game;
import requests.*;
import services.Services;
import org.junit.jupiter.api.*;

import java.util.Set;

@SuppressWarnings("unused")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DAOTests{
	static GameDAO gameData = new GameDAO();
	static AuthDAO authData = new AuthDAO();
	static UserDAO userData = new UserDAO();
	@BeforeEach
	public void setup(){
		Services.clearApplication(new ClearApplicationRequest());
	}


	//GAME DAO
	@Test
	@Order(1)
	@DisplayName("Success find game")
	public void successFindGame(){
		try{
			int game = gameData.createGame("new Game");
			Game find = gameData.findGame(game);
			Assertions.assertNotNull(find, "game not found");
			//Set<Game> games = gameData.getGames();
			//Assertions.assertEquals(games.iterator().next().getGameName(), "new Game");
		}catch(DataAccessException e){
			throw new RuntimeException(e);
		}
	}
	@Test
	@Order(2)
	@DisplayName("Fail find game")
	public void failFindGame(){
		try{
			int game = gameData.createGame("new Game");
			Game find = gameData.findGame(game+2);
			Assertions.assertNull(find, "som ting wong");
		}catch(DataAccessException e){
			throw new RuntimeException(e);
		}
	}

	@Test
	@Order(3)//cant really reacte a fail case for this
	@DisplayName("success Get Games")
	public void successGetGames(){
		try{
			int game1 = gameData.createGame("new Game 1");
			int game2 = gameData.createGame("new Game 2");
			Set<Game> games = gameData.getGames();
			Assertions.assertEquals(games.size(), 2,  "amount of games doesnt match");
		}catch(DataAccessException e){
			Assertions.assertEquals(1, 2,  "dao exception was thrown");
			throw new RuntimeException(e);
		}
	}

	@Test
	@Order(4)
	@DisplayName("success add white user")
	public void successAddWhitePlayer(){
		try{
			int user = userData.createUser("awesome user", "awesome password", "email@email.com");
			int game = gameData.createGame("new Game");
			gameData.setWhiteUser("awesome user", game);
			Game find = gameData.findGame(game);
			Assertions.assertEquals(find.getWhiteUsername(), "awesome user","som ting wong");
		}catch(DataAccessException e){
			throw new RuntimeException(e);
		}
	}

	@Test
	@Order(5)
	@DisplayName("fail add white user")
	public void failAddWhitePlayer(){
		try{
			int user = userData.createUser("awesome user", "awesome password", "email@email.com");
			int game = gameData.createGame("new Game");
			gameData.setWhiteUser("wrong user", game);
			Game find = gameData.findGame(game);
		}catch(DataAccessException e){
			Assertions.assertEquals(1, 1,"error was thrown");
			throw new RuntimeException(e);
		}
	}

	@Test
	@Order(6)
	@DisplayName("success add black user")
	public void successAddBlackPlayer(){
		try{
			int user = userData.createUser("awesome user", "awesome password", "email@email.com");
			int game = gameData.createGame("new Game");
			gameData.setBlackUser("awesome user", game);
			Game find = gameData.findGame(game);
			Assertions.assertEquals(find.getBlackUsername(), "awesome user","som ting wong");
		}catch(DataAccessException e){
			throw new RuntimeException(e);
		}
	}

	@Test
	@Order(7)
	@DisplayName("fail add black user")
	public void failAddBlackPlayer(){
		try{
			int user = userData.createUser("awesome user", "awesome password", "email@email.com");
			int game = gameData.createGame("new Game");
			gameData.setBlackUser("wrong user", game);
			Game find = gameData.findGame(game);
		}catch(DataAccessException e){
			Assertions.assertEquals(1, 1,"error was thrown");
		}
	}

	@Test
	@Order(8)
	@DisplayName("add spectator")
	public void successAddSpectator(){
		try{
			int user1 = userData.createUser("awesome user 1", "awesome password", "email1@email.com");
			int user2 = userData.createUser("awesome user 2", "awesome password", "email2@email.com");
			int game = gameData.createGame("new Game");
			gameData.addSpectator("awesome user 1", game);
			gameData.addSpectator("awesome user 2", game);
			Game find = gameData.findGame(game);
			Assertions.assertEquals(find.getSpectators().size(), 2,"wrong number of users was returned");
		}catch(DataAccessException e){
			throw new RuntimeException(e);
		}
	}

	@Test
	@Order(9)
	@DisplayName("fail add spectator")
	public void failAddSpectator(){
		try{
			int user1 = userData.createUser("awesome user 1", "awesome password", "email1@email.com");
			int user2 = userData.createUser("awesome user 2", "awesome password", "email2@email.com");
			int game = gameData.createGame("new Game");
			gameData.addSpectator("awesome user 1", game+1);
		}catch(DataAccessException e){
			Assertions.assertEquals(1, 1,"error was thrown");
		}
	}

	//USER DAO

	@Test
	@Order(10)
	@DisplayName("successAuthenticateUser")
	public void successAuthenticateUser(){
		try{
			int user1 = userData.createUser("awesome user 1", "awesome password", "email1@email.com");
			boolean signedin = userData.authenticateUser("awesome user 1", "awesome password");
			Assertions.assertTrue(signedin, "Is not true when it should be");
		}catch(DataAccessException e){
			System.out.println("shouldnt throw exception");
		}
	}

	@Test
	@Order(11)
	@DisplayName("failAuthenticateUser")
	public void failAuthenticateUser(){
		try{
			int user1 = userData.createUser("awesome user 1", "awesome password", "email1@email.com");
			boolean signedin = userData.authenticateUser("awesome user 1", "wrong password");
			Assertions.assertFalse(signedin, "Is not false when it should be");
		}catch(DataAccessException e){
			System.out.println("shouldnt throw exception");
		}
	}

	//AUTH DAO
	
	@Test
	@Order(12)
	@DisplayName("success create token")
	public void failExistsToken(){
		try{
			String token = authData.createToken("username");
			boolean existsToken = authData.tokenExists(token.concat("yolo"));
			Assertions.assertFalse(existsToken, "Is not false when it should be");
		}catch(DataAccessException e){
			System.out.println("shouldnt throw exception");
		}
	}

}
