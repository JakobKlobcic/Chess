package requests;

/**
 * The RegisterRequest class represents a specialized HTTP request for user registration,
 * extending the base Request class.
 */
public class RegisterRequest extends Request{
	/**
	 * Register username
	 */
	private String username;
	/**
	 * Register password
	 */
	private String password;
	/**
	 * Register email
	 */
	private String email;

	public RegisterRequest(){}

	public String getUsername(){
		return username;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getPassword(){
		return password;
	}

	public String getEmail(){
		return email;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public void setEmail(String email){
		this.email = email;
	}
}
