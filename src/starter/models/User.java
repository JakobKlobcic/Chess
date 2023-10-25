package models;

/**
 * The User class represents a user in the system with basic information
 */
public class User {
    public User(String username, String password, String email){
        this.email=email;
        this.username=username;
        this.password=password;
    }

    /**
     * The unique identifier for the user.
     */
    String username;

    /**
     * The password associated with the user account.
     */
    String password;

    /**
     * The email address associated with the user account.
     */
    String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}