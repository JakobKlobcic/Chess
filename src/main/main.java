
import dataAccess.DataAccessException;
import dataAccess.Database;
import server.Server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class main {
    public static void main(String [] args){
        Server server = new Server();
        server.start();
    }
}
