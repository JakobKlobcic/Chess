package responses;

/**
 * The RegisterResponse class represents a specialized HTTP response for user registration,
 * extending the base Response class.
 */
public class RegisterResponse extends Response{
	/**
	 * Username of the registered user
	 */
	private String username;
	/**
	 * Authtoken of the registered user
	 */
	private String authToken;

	public String getUsername(){
		return username;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public void setAuthToken(String authToken){
		this.authToken = authToken;
	}

	public String getAuthToken(){
		return authToken;
	}
}
