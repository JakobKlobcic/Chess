package models;

/**
 * The class represents an authentication token along with the associated username.
 * This class is used to store information related to user authentication.
 */
public class Authtoken {

    /**
     * The authentication token associated with a user.
     */
    String authToken;

    /**
     * The username associated with the authentication token.
     */
    String username;

    public Authtoken(String username, String token){
        this.username=username;
        this.authToken=token;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        Authtoken t = (Authtoken) obj;
        return t.authToken == this.authToken && t.username==this.username;
    }
}