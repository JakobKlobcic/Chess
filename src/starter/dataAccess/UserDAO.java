package dataAccess;

import models.Authtoken;
import models.User;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.LoginResponse;
import responses.RegisterResponse;

import java.util.HashSet;
import java.util.Set;

/**
 * The UserDAO class is responsible for CRUD operations
 * on the User entities in the database.
 *
 * @author Jakob Klobcic
 */
public class UserDAO {
    Set<User> users = new HashSet<>();
    /**
     * Finds a user by the provided username.
     *
     * @param username the username of the user to be found
     * @return returns the user found or null if no results match
     */
    User findUser(String username){
        return null;
    }

    public Boolean authenticateUser(String username, String password){
        for(User user : users) {
            if(user.getUsername().equals(username) && user.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a new user entity.
     */
    public Boolean createUser(String username, String password, String email){
        //TODO: do i have to validate an email
        for(User user : users) {
            if(user.getUsername().equals(username) || user.getEmail().equals(email)){
                return false;
            }
        }
        users.add(new User(username,password,email));
        return true;
    }

    /**
     * Updates an existing user entity.
     */
    void updateUser(){}

    /**
     * Deletes an existing user entity.
     */
    void deleteUser(){}
}