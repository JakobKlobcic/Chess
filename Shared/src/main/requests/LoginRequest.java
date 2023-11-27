package requests;

/**
 * The LoginRequest class represents a specialized HTTP request for user login,
 * extending the base Request class.
 */
public class LoginRequest extends Request{
	/**
	 * Login username
	 */
	private String username;
	/**
	 * Login password
	 */
	private String password;

	public String getUsername(){
		return username;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getPassword(){
		return password;
	}

	public void setPassword(String password){
		this.password = password;
	}
}
