package responses;

/**
 * The LoginResponse class represents a specialized HTTP response for user login,
 * extending the base Response class.
 */
public class LoginResponse extends Response{
	/**
	 * username of the User that logged in
	 */
	String username;
	/**
	 * Authtoken the user receives
	 */
	String authToken;

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
