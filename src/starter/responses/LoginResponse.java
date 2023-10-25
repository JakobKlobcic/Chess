package responses;

/**
 * The LoginResponse class represents a specialized HTTP response for user login,
 * extending the base Response class.
 */
public class LoginResponse extends Response{
    String username;
    String authToken;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
