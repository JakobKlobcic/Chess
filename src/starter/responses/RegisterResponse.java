package responses;

/**
 * The RegisterResponse class represents a specialized HTTP response for user registration,
 * extending the base Response class.
 */
public class RegisterResponse extends Response{
    private String username;
    private String authToken;

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
