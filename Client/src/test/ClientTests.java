
import models.Game;
import requests.*;
import org.junit.jupiter.api.*;

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
	@DisplayName("Success find game")
	public void successFindGame(){
	}
	@Test
	@Order(2)
	@DisplayName("Fail find game")
	public void failFindGame(){
	}

	@Test
	@Order(3)//cant really reacte a fail case for this
	@DisplayName("success Get Games")
	public void successGetGames(){
	}

	@Test
	@Order(4)
	@DisplayName("success add white user")
	public void successAddWhitePlayer(){
	}

	@Test
	@Order(5)
	@DisplayName("fail add white user")
	public void failAddWhitePlayer(){
	}

	@Test
	@Order(6)
	@DisplayName("success add black user")
	public void successAddBlackPlayer(){
	}

	@Test
	@Order(7)
	@DisplayName("fail add black user")
	public void failAddBlackPlayer(){
	}

	@Test
	@Order(8)
	@DisplayName("add spectator")
	public void successAddSpectator(){
	}

	@Test
	@Order(9)
	@DisplayName("fail add spectator")
	public void failAddSpectator(){
	}

	//USER DAO

	@Test
	@Order(10)
	@DisplayName("successAuthenticateUser")
	public void successAuthenticateUser(){
	}

	@Test
	@Order(11)
	@DisplayName("failAuthenticateUser")
	public void failAuthenticateUser(){
	}

	//AUTH DAO

	@Test
	@Order(12)
	@DisplayName("success create token")
	public void failExistsToken(){
	}

}
