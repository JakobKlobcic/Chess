package dataAccess;

import java.sql.*;

/**
 * The UserDAO class is responsible for CRUD operations
 * on the User entities in the database.
 *
 * @author Jakob Klobcic
 */
public class UserDAO{
	Database db = Database.getInstance();

	public Boolean authenticateUser(String username, String password)throws DataAccessException{
		try (Connection conn = db.getConnection()) {
			String sql = "SELECT * FROM user WHERE username = ? AND password = ?;";
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, username);
				stmt.setString(2, password);

				ResultSet rs = stmt.executeQuery();
				return rs.next(); // If there are any results, the login is successful
			}
		} catch (SQLException e) {
			throw new DataAccessException(e.toString());
		}
	}

	/**
	 * Creates a new user entity.
	 */
	public Integer createUser(String username, String password, String email)throws DataAccessException{
		Connection conn = null;
		try {
			conn = db.getConnection();

			String sql = "INSERT INTO user (email, username, password) VALUES (?, ?, ?);";
			try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
				stmt.setString(1, email);
				stmt.setString(2, username);
				stmt.setString(3, password);
				stmt.executeUpdate();
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					return rs.getInt(1);
				}
				return null;// If rows were affected, the user was successfully created
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println("duplicate");
			return null;
		} catch (SQLException e) {
			System.out.println(e);
			throw new DataAccessException(e.toString());
		} finally {
			if (conn != null) {
				db.returnConnection(conn);
			}
		}
	}

	/*
	public Set<User> getUsers()throws DataAccessException{
		Set<User> result = new HashSet<>();
		Connection conn = null;
		try{
			conn = db.getConnection();
			String sql = "SELECT * FROM user;";
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					User user = new User(
							rs.getString("username"),
							rs.getString("password"),
							rs.getString("email")
					);
					result.add(user);
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException(e.toString());
		}

		return result;
	}*/

	public int clearDb()throws DataAccessException{
		Connection conn = null;
		try {
			conn = db.getConnection();
			Statement stmt = conn.createStatement();

			stmt.execute("SET FOREIGN_KEY_CHECKS=0");
			stmt.executeUpdate("DELETE FROM authtoken");
			stmt.executeUpdate("DELETE FROM board");
			stmt.executeUpdate("DELETE FROM game");
			stmt.executeUpdate("DELETE FROM user");
			stmt.executeUpdate("DELETE FROM spectator");
			stmt.execute("SET FOREIGN_KEY_CHECKS=1");

		} catch (SQLException e) {
			throw new DataAccessException(e.toString());
		} finally {
			if (conn != null) {
				db.returnConnection(conn);
			}
		}
		return 200;
	}
}