package dataAccess;

import models.Authtoken;
import models.User;

import java.util.*;

/**
 * The AuthDAO class is responsible for CRUD operations
 * on the Authorization tokens in the database.
 *
 * @author Jakob Klobcic
 */
public class AuthDAO {

    Set<Authtoken> tokens = new HashSet<>();
    /**
     * Retrieves an Authorization token for some user.
     *
     * @param user the user for whom the token is to be retrieved
     * @return currently returns null because of non-implementation
     */
    public Authtoken getToken(User user){
        for(Authtoken token : tokens) {
            if(token.getUsername().equals(user.getUsername())){
                return token;
            }
        }
        return null;
    }

    public void removeToken(String t){
        for(Authtoken token : tokens) {
            if(token.getAuthToken().equals(t)){
                tokens.remove(token);
                return;
            }
        }
    }

    public boolean tokenExists(String t){
        if(t.equals("")){
            return false;
        }
        for(Authtoken token : tokens) {
            if(token.getAuthToken().equals(t)){
                return true;
            }
        }
        return false;
    }

    public String username(String t){
        for(Authtoken token : tokens) {
            if(Objects.equals(token.getAuthToken(), t)){
                return token.getUsername();
            }
        }
        return null;
    }

    /**
     * Creates a new Authorization token.
     */
    public String createToken(String username){
        String id = UUID.randomUUID().toString();
        tokens.add(new Authtoken(username, id));
        return id;
    }

    /**
     * Updates an existing Authorization token.
     */
    void updateToken(){}

    /**
     * Deletes an existing Authorization token.
     */
    void deleteToken(){}
}
