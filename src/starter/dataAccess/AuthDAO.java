package dataAccess;

import java.sql.*;
import java.util.*;

/**
 * The AuthDAO class is responsible for CRUD operations
 * on the Authorization tokens in the database.
 *
 * @author Jakob Klobcic
 */
public class AuthDAO{
	Database db = Database.getInstance();

	public void removeToken(String t) throws DataAccessException{
		Connection conn = null;
		try {
			conn = db.getConnection();
			String sql = "DELETE FROM authtoken WHERE token = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, t);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException(e.toString());
		} finally {
			if (conn != null) {
				db.returnConnection(conn);
			}
		}
	}

	public boolean tokenExists(String t)throws DataAccessException{
		Connection conn = null;

		String sql = "SELECT * FROM authtoken WHERE token = ?";
		try {
			conn = db.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, t);
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			System.out.println(e);
			throw new DataAccessException(e.toString());
		}
	}

	public String username(String t)throws DataAccessException{
		Connection conn = null;

		conn = db.getConnection();
		String sql = "SELECT u.username FROM user u INNER JOIN authtoken a ON u.id = a.user_id WHERE a.token = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, t);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getString("username");
			} else {
				// If no entry is found, you might want to return null or handle it accordingly
				return null;
			}
		} catch (SQLException e) {
			throw new DataAccessException(e.toString());
		}
	}

	public String token(String u)throws DataAccessException{
		Connection conn = null;

		conn = db.getConnection();
		String sql = "SELECT a.token FROM authtoken a INNER JOIN user u ON u.id = a.user_id WHERE u.username = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, u);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getString("token");
			} else {
				// If no entry is found, you might want to return null or handle it accordingly
				return null;
			}
		} catch (SQLException e) {
			throw new DataAccessException(e.toString());
		}
	}


	/**
	 * Creates a new Authorization token.
	 */
	//public String createToken(String username)throws DataAccessException{
	//
	//	String token = UUID.randomUUID().toString();
	//	Connection conn = null;
	//	try {
	//		conn = db.getConnection();
	//		String sql = "INSERT INTO authtoken (token, user_id)\n SELECT ?, id FROM user WHERE username = ?;";
	//		PreparedStatement createStmt = conn.prepareStatement(sql);
	//		createStmt.setString(1, token);
	//		createStmt.setString(2, username);
	//		createStmt.executeUpdate();
	//	} catch (SQLException e) {
	//		throw new DataAccessException(e.toString());
	//	} finally {
	//		if (conn != null) {
	//			db.returnConnection(conn);
	//		}
	//	}
	//	return token;
	//}

	public String createToken(String username) throws DataAccessException {

		String token = UUID.randomUUID().toString();
		Connection conn = null;
		try {
			conn = db.getConnection();
			String sql = "INSERT INTO authtoken (token, user_id)\n" +
					"SELECT ?, id FROM user WHERE username = ?\n" +
					"ON DUPLICATE KEY UPDATE token = VALUES(token);";
			PreparedStatement createStmt = conn.prepareStatement(sql);
			createStmt.setString(1, token);
			createStmt.setString(2, username);
			createStmt.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException(e.toString());
		} finally {
			if (conn != null) {
				db.returnConnection(conn);
			}
		}
		return token;
	}
}
