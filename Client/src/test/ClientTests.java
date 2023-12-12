
import models.Game;
import requests.*;
import org.junit.jupiter.api.*;
import responses.*;

import java.io.IOException;
import java.util.Set;

@SuppressWarnings("unused")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientTests{
	@BeforeEach
	public void setup(){

	}


	//GAME DAO
	@Test
	@Order(1)
	@DisplayName("Success register")
	public void sRegister(){
		RegisterResponse response;
		try{
			response = ApiCalls.register("username", "password", "email@email.com");
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		Assertions.assertEquals(response.getStatus(), 200, "Response status should have been 200");
	}
	@Test
	@Order(2)
	@DisplayName("Fail register")
	public void fRegister(){
		RegisterResponse response;
		try{
			ApiCalls.register("username1", "password", "email@email.com");
			response = ApiCalls.register("username1", "password", "email@email.com");
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		Assertions.assertEquals(response.getStatus(), 403, "Response status should have been 403");
	}

	@Test
	@Order(3)
	@DisplayName("success login")
	public void sLogin(){
		RegisterResponse response;
		LoginResponse loginResponse;
		try{
			response = ApiCalls.register("username2", "password", "email2@email.com");
			loginResponse = ApiCalls.login("username2", "password");
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		Assertions.assertEquals(loginResponse.getStatus(), 200, "Response status should have been 200");

	}

	@Test
	@Order(4)
	@DisplayName("fail login")
	public void fLogin(){
		RegisterResponse response;
		LoginResponse loginResponse;
		try{
			response = ApiCalls.register("username3", "password", "email3@email.com");
			loginResponse = ApiCalls.login("username3", "wrong-password");
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		Assertions.assertEquals(loginResponse.getStatus(), 401, "Response status should have been 401");

	}

	@Test
	@Order(5)
	@DisplayName("success logout")
	public void sLogout(){
		RegisterResponse response;
		LogoutResponse loginResponse;
		try{
			response = ApiCalls.register("username4", "password", "email4@email.com");
			loginResponse = ApiCalls.logout(response.getAuthToken());
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		Assertions.assertEquals(loginResponse.getStatus(), 200, "Response status should have been 200");

	}

	@Test
	@Order(6)
	@DisplayName("fail logout")
	public void fLogout(){
		RegisterResponse response;
		LogoutResponse logoutResponse;
		try{
			response = ApiCalls.register("username5", "password", "email5@email.com");
			logoutResponse = ApiCalls.logout("");
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		Assertions.assertEquals(logoutResponse.getStatus(), 401, "Response status should have been 401");
	}

	@Test
	@Order(7)
	@DisplayName("success create game")
	public void sCreateGame(){
		RegisterResponse response;
		CreateGameResponse createGameResponse;
		try{
			response = ApiCalls.register("username6", "password", "email6@email.com");
			createGameResponse = ApiCalls.createGame("The_best_game",response.getAuthToken());
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		Assertions.assertEquals(createGameResponse.getStatus(), 200, "Response status should have been 200");
	}

	@Test
	@Order(8)
	@DisplayName("fail create game")
	public void fCreateGame(){
		RegisterResponse response;
		CreateGameResponse createGameResponse;
		try{
			response = ApiCalls.register("username7", "password", "email7@email.com");
			ApiCalls.createGame("The_best_game1",response.getAuthToken());
			createGameResponse = ApiCalls.createGame("The_best_game1",response.getAuthToken());
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		Assertions.assertEquals(createGameResponse.getStatus(), 500, "Response status should have been 500");
	}

	@Test
	@Order(9)
	@DisplayName("success list games")
	public void sListGames(){
		RegisterResponse response;
		ListGamesResponse listGamesResponse;
		try{
			response = ApiCalls.register("username8", "password", "email8@email.com");
			ApiCalls.createGame("The_best_game2",response.getAuthToken());
			listGamesResponse = ApiCalls.listGames(response.getAuthToken());
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		Assertions.assertEquals(listGamesResponse.getStatus(), 200, "Response status should have been 200");
		Assertions.assertTrue(listGamesResponse.getGameCount()>0, "The game count should have been more than 0");
	}

	@Test
	@Order(10)
	@DisplayName("fail list games")
	public void fListGames(){
		RegisterResponse response;
		ListGamesResponse listGamesResponse;
		try{
			response = ApiCalls.register("username9", "password", "email9@email.com");
			ApiCalls.createGame("The_best_game3",response.getAuthToken());
			listGamesResponse = ApiCalls.listGames("");
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		Assertions.assertEquals(listGamesResponse.getStatus(), 401, "Response status should have been 401");
	}

	@Test
	@Order(11)
	@DisplayName("success join game")
	public void sJoin(){
		RegisterResponse response;
		CreateGameResponse createGameResponse;
		JoinGameResponse joinGameResponse;
		try{
			response = ApiCalls.register("username10", "password", "email10@email.com");
			createGameResponse = ApiCalls.createGame("The_best_game4",response.getAuthToken());
			joinGameResponse = ApiCalls.joinGame(response.getAuthToken(),"WHITE", createGameResponse.getGameID());
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		Assertions.assertEquals(joinGameResponse.getStatus(), 200, "Response status should have been 200");
	}

	@Test
	@Order(12)
	@DisplayName("fail join game")
	public void fJoin(){
		RegisterResponse response;
		CreateGameResponse createGameResponse;
		JoinGameResponse joinGameResponse;
		try{
			response = ApiCalls.register("username11", "password", "email11@email.com");
			createGameResponse = ApiCalls.createGame("The_best_game5",response.getAuthToken());
			ApiCalls.joinGame(response.getAuthToken(),"WHITE", createGameResponse.getGameID());
			joinGameResponse = ApiCalls.joinGame(response.getAuthToken(),"WHITE", createGameResponse.getGameID());
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		Assertions.assertEquals(joinGameResponse.getStatus(), 403, "Response status should have been 403");
	}

}
