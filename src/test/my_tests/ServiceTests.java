package my_tests;

import requests.*;
import responses.*;
import services.Services;
import org.junit.jupiter.api.*;

@SuppressWarnings("unused")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceTests{
	@BeforeEach
	public void setup(){
		Services.clearApplication(new ClearApplicationRequest());
	}

	@Test
	@Order(1)
	@DisplayName("Register success")
	public void successRegister(){
		RegisterRequest request = new RegisterRequest();
		request.setUsername("my.username");
		request.setEmail("email.email@email.com");
		request.setPassword("superstrongpassword");

		RegisterResponse response = Services.registerUser(request);

		Assertions.assertEquals(200, response.getStatus(), "Status code was not 200");
		Assertions.assertNotNull(response.getAuthToken(), "Authtoken was null when it shouldn't have been");
		Assertions.assertNotNull(response.getUsername(), "username was null when it shouldn't have been");

	}

	@Test
	@Order(2)
	@DisplayName("Register failure")
	public void failureRegister(){
		RegisterRequest request1 = new RegisterRequest();
		request1.setUsername("my.username");
		request1.setEmail("email.email@email.com");
		request1.setPassword("superstrongpassword");
		RegisterResponse response1 = Services.registerUser(request1);

		RegisterRequest request2 = new RegisterRequest();
		request2.setUsername("not.my.username");
		request2.setEmail("email.email@email.com");
		request2.setPassword("strongpassword");
		RegisterResponse response2 = Services.registerUser(request2);

		Assertions.assertEquals(403, response2.getStatus(), "Status code was not 403");

		RegisterRequest request3 = new RegisterRequest();
		request3.setUsername("my.username");
		request3.setEmail("email@email.com");
		request3.setPassword("superstrongpassword");
		RegisterResponse response3 = Services.registerUser(request3);

		Assertions.assertEquals(403, response3.getStatus(), "Status code was not 403");

		RegisterRequest request4 = new RegisterRequest();
		request4.setUsername("not.my.username");
		request4.setEmail("email@email.com");
		request4.setPassword("superstrongpassword");
		RegisterResponse response4 = Services.registerUser(request4);

		Assertions.assertEquals(200, response4.getStatus(), "Status code was not 200");
	}

	@Test
	@Order(3)
	@DisplayName("Login success")
	public void successLogin(){
		RegisterRequest request = new RegisterRequest();
		request.setUsername("my.username");
		request.setEmail("email.email@email.com");
		request.setPassword("superstrongpassword");

		RegisterResponse response = Services.registerUser(request);

		LoginRequest request1 = new LoginRequest();
		request1.setUsername("my.username");
		request1.setPassword("superstrongpassword");

		LoginResponse response1 = Services.login(request1);

		Assertions.assertEquals(200, response1.getStatus(), "Status code was not 200");
		Assertions.assertNotNull(response1.getAuthToken(), "Authtoken was null when it shouldn't have been");
		Assertions.assertNotNull(response1.getUsername(), "username was null when it shouldn't have been");

	}

	@Test
	@Order(4)
	@DisplayName("Login failure")
	public void failureLogin(){
		RegisterRequest request = new RegisterRequest();
		request.setUsername("my.username");
		request.setEmail("email.email@email.com");
		request.setPassword("superstrongpassword");

		RegisterResponse response = Services.registerUser(request);

		LoginRequest request1 = new LoginRequest();
		request1.setUsername("my.username");
		request1.setPassword("password");

		LoginResponse response1 = Services.login(request1);

		Assertions.assertEquals(401, response1.getStatus(), "Status code was not 401");

		LoginRequest request2 = new LoginRequest();
		request2.setUsername("username");
		request2.setPassword("superstrongpassword");

		LoginResponse response2 = Services.login(request2);

		Assertions.assertEquals(401, response2.getStatus(), "Status code was not 401");
	}

	@Test
	@Order(5)
	@DisplayName("Logout success")
	public void successLogout(){
		RegisterRequest request = new RegisterRequest();
		request.setUsername("my.username");
		request.setEmail("email.email@email.com");
		request.setPassword("superstrongpassword");

		RegisterResponse response = Services.registerUser(request);

		LogoutRequest request1 = new LogoutRequest();
		request1.addHeader("authorization", response.getAuthToken());
		LogoutResponse response1 = Services.logout(request1);

		Assertions.assertEquals(200, response1.getStatus(), "Status code was not 200");
	}

	@Test
	@Order(6)
	@DisplayName("Logout failure")
	public void failureLogout(){
		RegisterRequest request = new RegisterRequest();
		request.setUsername("my.username");
		request.setEmail("email.email@email.com");
		request.setPassword("superstrongpassword");

		RegisterResponse response = Services.registerUser(request);

		LogoutRequest request1 = new LogoutRequest();
		String adjustedToken = response.getAuthToken().replace("-", "+");
		request1.addHeader("authorization", adjustedToken);
		LogoutResponse response1 = Services.logout(request1);

		Assertions.assertEquals(401, response1.getStatus(), "Status code was not 401");
	}

	@Test
	@Order(7)
	@DisplayName("Create game success")
	public void successCreateGame(){
		RegisterRequest request = new RegisterRequest();
		request.setUsername("my.username");
		request.setEmail("email.email@email.com");
		request.setPassword("superstrongpassword");

		RegisterResponse response = Services.registerUser(request);

		CreateGameRequest request1 = new CreateGameRequest();
		request1.setGameName("The greatest game");
		request1.addHeader("authorization", response.getAuthToken());
		CreateGameResponse response1 = Services.createGame(request1);

		Assertions.assertEquals(200, response1.getStatus(), "Status code was not 200");
		Assertions.assertNotNull(response1.getGameID(), "gameID was null when it shouldn't have been");
	}

	@Test
	@Order(8)
	@DisplayName("Create game failure")
	public void failureCreateGame(){
		RegisterRequest request = new RegisterRequest();
		request.setUsername("my.username");
		request.setEmail("email.email@email.com");
		request.setPassword("superstrongpassword");

		RegisterResponse response = Services.registerUser(request);

		CreateGameRequest request1 = new CreateGameRequest();
		request1.setGameName("The greatest game");
		CreateGameResponse response1 = Services.createGame(request1);

		Assertions.assertEquals(401, response1.getStatus(), "Status code was not 401");
	}

	@Test
	@Order(9)
	@DisplayName("Join game success")
	public void successJoinGame(){
		RegisterRequest request = new RegisterRequest();
		request.setUsername("first.user");
		request.setEmail("first.user@email.com");
		request.setPassword("superstrongpassword");

		RegisterResponse response = Services.registerUser(request);

		RegisterRequest request1 = new RegisterRequest();
		request1.setUsername("second.user");
		request1.setEmail("second.user@email.com");
		request1.setPassword("superstrongpassword");

		RegisterResponse response1 = Services.registerUser(request1);

		CreateGameRequest request2 = new CreateGameRequest();
		request2.setGameName("The greatest game");
		request2.addHeader("authorization", response.getAuthToken());
		CreateGameResponse response2 = Services.createGame(request2);

		JoinGameRequest request3 = new JoinGameRequest();
		request3.addHeader("authorization", response.getAuthToken());
		request3.setPlayerColor("WHITE");
		request3.setGameID(response2.getGameID());

		JoinGameResponse response3 = Services.joinGame(request3);

		Assertions.assertEquals(200, response3.getStatus(), "Status code was not --200");

		JoinGameRequest request4 = new JoinGameRequest();
		request4.addHeader("authorization", response1.getAuthToken());
		request4.setPlayerColor("BLACK");
		request4.setGameID(response2.getGameID());

		JoinGameResponse response4 = Services.joinGame(request4);

		Assertions.assertEquals(200, response4.getStatus(), "Status code was not 200");
	}

	@Test
	@Order(10)
	@DisplayName("Join game failure")
	public void failureJoineGame(){
		RegisterRequest request = new RegisterRequest();
		request.setUsername("first.user");
		request.setEmail("first.user@email.com");
		request.setPassword("superstrongpassword");

		RegisterResponse response = Services.registerUser(request);

		RegisterRequest request1 = new RegisterRequest();
		request1.setUsername("second.user");
		request1.setEmail("second.user@email.com");
		request1.setPassword("superstrongpassword");

		RegisterResponse response1 = Services.registerUser(request1);

		CreateGameRequest request2 = new CreateGameRequest();
		request2.setGameName("The greatest game");
		request2.addHeader("authorization", response.getAuthToken());
		CreateGameResponse response2 = Services.createGame(request2);

		JoinGameRequest request3 = new JoinGameRequest();
		request3.addHeader("authorization", response.getAuthToken());
		request3.setPlayerColor("WHITE");
		request3.setGameID(response2.getGameID());

		JoinGameResponse response3 = Services.joinGame(request3);

		Assertions.assertEquals(200, response3.getStatus(), "Status code was not 200");

		JoinGameRequest request4 = new JoinGameRequest();
		request4.addHeader("authorization", response1.getAuthToken());
		request4.setPlayerColor("WHITE");
		request4.setGameID(response2.getGameID());

		JoinGameResponse response4 = Services.joinGame(request4);

		Assertions.assertEquals(403, response4.getStatus(), "Status code was not 403");
	}

	@Test
	@Order(11)
	@DisplayName("List games success")
	public void successListGames(){
		RegisterRequest request = new RegisterRequest();
		request.setUsername("my.username");
		request.setEmail("email.email@email.com");
		request.setPassword("superstrongpassword");

		RegisterResponse response = Services.registerUser(request);

		CreateGameRequest request1 = new CreateGameRequest();
		request1.setGameName("The greatest game1");
		request1.addHeader("authorization", response.getAuthToken());
		CreateGameResponse response1 = Services.createGame(request1);

		CreateGameRequest request2 = new CreateGameRequest();
		request2.setGameName("The greatest game2");
		request2.addHeader("authorization", response.getAuthToken());
		CreateGameResponse response2 = Services.createGame(request2);

		CreateGameRequest request3 = new CreateGameRequest();
		request3.setGameName("The greatest game3");
		request3.addHeader("authorization", response.getAuthToken());
		CreateGameResponse response3 = Services.createGame(request3);

		ListGamesRequest request4 = new ListGamesRequest();
		String adjustedToken = response.getAuthToken().replace("-", "+");
		request1.addHeader("authorization", adjustedToken);
		ListGamesResponse response4 = Services.listGames(request4);

		Assertions.assertEquals(401, response4.getStatus(), "Status code was not 401");
	}

	@Test
	@Order(12)
	@DisplayName("List games failure")
	public void failureListGames(){
		RegisterRequest request = new RegisterRequest();
		request.setUsername("my.username");
		request.setEmail("email.email@email.com");
		request.setPassword("superstrongpassword");

		RegisterResponse response = Services.registerUser(request);

		CreateGameRequest request1 = new CreateGameRequest();
		request1.setGameName("The greatest game1");
		request1.addHeader("authorization", response.getAuthToken());
		CreateGameResponse response1 = Services.createGame(request1);

		CreateGameRequest request2 = new CreateGameRequest();
		request2.setGameName("The greatest game2");
		request2.addHeader("authorization", response.getAuthToken());
		CreateGameResponse response2 = Services.createGame(request2);

		CreateGameRequest request3 = new CreateGameRequest();
		request3.setGameName("The greatest game3");
		request3.addHeader("authorization", response.getAuthToken());
		CreateGameResponse response3 = Services.createGame(request3);

		ListGamesRequest request4 = new ListGamesRequest();
		request4.addHeader("authorization", response.getAuthToken());
		ListGamesResponse response4 = Services.listGames(request4);

		Assertions.assertEquals(3, response4.getGames().size(), "Status code was not 401");
	}

	@Test
	@Order(12)
	@DisplayName("Clear app success")
	public void successClearApp(){
		RegisterRequest request = new RegisterRequest();
		request.setUsername("my.username");
		request.setEmail("email.email@email.com");
		request.setPassword("superstrongpassword");

		RegisterResponse response = Services.registerUser(request);

		CreateGameRequest request1 = new CreateGameRequest();
		request1.setGameName("The greatest game1");
		request1.addHeader("authorization", response.getAuthToken());
		CreateGameResponse response1 = Services.createGame(request1);

		ClearApplicationRequest request2 = new ClearApplicationRequest();
		ClearApplicationResponse response2 = Services.clearApplication(request2);

		//Assertions.assertEquals(0, Services.getAuthData().getTokens().size(), "Not all tokens were removed");
		//Assertions.assertEquals(0, Services.getGameData().getGames().size(), "Not all games were removed");
		//try{
		//	Assertions.assertEquals(0, Services.getUserData().getUsers().size(), "Not all users were removed");
		//}catch(DataAccessException e){
		//	throw new RuntimeException(e);
		//}
	}


}
