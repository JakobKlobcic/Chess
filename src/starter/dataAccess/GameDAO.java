package dataAccess;

import chess.*;
import models.Game;

import java.sql.*;
import java.util.*;

/**
 * The GameDAO class is responsible for CRUD operations
 * on the Game entities in the database.
 *
 * @author Jakob Klobcic
 */
public class GameDAO{
	/**
	 * Finds a game by the provided ID.
	 *
	 * @param id the ID of the game to be found
	 * @return returns Game or null if not found
	 */
	Map<String, Integer> whitePieces = new HashMap<>(){{
		put("PAWN",1);
		put("KNIGHT",2);
		put("BISHOP",3);
		put("ROOK",4);
		put("QUEEN",5);
		put("KING",6);
	}};
	Map<String, Integer> blackPieces = new HashMap<>(){{
		put("PAWN",7);
		put("KNIGHT",8);
		put("BISHOP",9);
		put("ROOK",10);
		put("QUEEN",11);
		put("KING",12);
	}};
	Database db = Database.getInstance();
	public Game findGame(int id)throws DataAccessException{
		Game game = null;
		Connection conn = null;
		try{
			conn = db.getConnection();
			String sql = "SELECT game.game_id, \n" +
					"       white_user.username AS white_username, \n" +
					"       black_user.username AS black_username, \n" +
					"       game.game_name, \n" +
					"       game.board_id, \n" +
					"       board.* \n" +
					"FROM game \n" +
					"LEFT JOIN user AS white_user ON game.white_user_id = white_user.id \n" +
					"LEFT JOIN user AS black_user ON game.black_user_id = black_user.id \n" +
					"INNER JOIN board ON game.board_id = board.id \n" +
					"WHERE game.game_id = ?;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				String whiteUser = null;
				String blackUser = null;
				if(rs.getString("white_username")!=null){
					whiteUser=rs.getString("white_username");
				}
				if(rs.getString("black_username")!=null){
					blackUser=rs.getString("black_username");
				}
				ChessGameImplementation chess = new ChessGameImplementation();
				chess.setBoard(getChessBoard(rs));
				game = new Game(rs.getInt("game_id"), whiteUser, blackUser, rs.getString("game_name"), chess);
			}
			if(game==null){
				return null;
			}
			game.setSpectators(getSpectators(id));
			return game;

		} catch (SQLException e) {
			throw new DataAccessException(e.toString());
		} finally {
			if (conn != null) {
				db.returnConnection(conn);
			}
		}
	}
	public int createGame(String name)throws DataAccessException{
		System.out.println("creating game");
		ChessGameImplementation game = new ChessGameImplementation();
		game.getBoard().resetBoard();
		int boardId = writeChessBoard((ChessBoardI)game.getBoard());
		//System.out.println(boardId);
		Connection conn = null;
		try {
			conn = db.getConnection();

			String sql = "INSERT INTO game (game_name, board_id) VALUES (?, ?);";
			PreparedStatement stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, name);
			stmt.setInt(2, boardId);

			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				return rs.getInt(1);
			}
			throw new DataAccessException("no game id returned");
		} catch (SQLException e) {
			throw new DataAccessException(e.toString());
		} finally {
			if (conn != null) {
				db.returnConnection(conn);
			}
		}
	}
	private static int getUserId(Connection conn, String username) throws SQLException{
		String sql = "SELECT id FROM user WHERE username = ?";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, username);
			try(ResultSet rs = pstmt.executeQuery()){
				if(rs.next()){
					return rs.getInt("id");
				}else{
					throw new SQLException("User not found");
				}
			}
		}
	}
	public Set<Game> getGames()throws DataAccessException{
		List<Integer> gameIds = new ArrayList<>();
		String sql = "SELECT game_id FROM game";

		Connection conn = null;
		try{
			conn = db.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				gameIds.add(rs.getInt("game_id"));
			}

		} catch (SQLException e) {
			throw new DataAccessException(e.toString());
		} finally {
			if (conn != null) {
				db.returnConnection(conn);
			}
		}
		Set<Game> result = new HashSet<>();
		for(int id : gameIds){
			result.add(findGame(id));
		}
		return result;


	}

	//public boolean nameExists(String name)throws DataAccessException{
	//	for(Game game : games){
	//		if(game.getGameName().equals(name)){
	//			return true;
	//		}
	//	}
	//	return false;
	//}

	int writeChessBoard(ChessBoardI chessBoard) throws DataAccessException {
		System.out.println("createing board");
		Connection conn = null;
		try {
			conn = db.getConnection();
			String sql = "INSERT INTO board (" +
				"1_1, 1_2, 1_3, 1_4, 1_5, 1_6, 1_7, 1_8, " +
				"2_1, 2_2, 2_3, 2_4, 2_5, 2_6, 2_7, 2_8, " +
				"3_1, 3_2, 3_3, 3_4, 3_5, 3_6, 3_7, 3_8, " +
				"4_1, 4_2, 4_3, 4_4, 4_5, 4_6, 4_7, 4_8, " +
				"5_1, 5_2, 5_3, 5_4, 5_5, 5_6, 5_7, 5_8, " +
				"6_1, 6_2, 6_3, 6_4, 6_5, 6_6, 6_7, 6_8, " +
				"7_1, 7_2, 7_3, 7_4, 7_5, 7_6, 7_7, 7_8, " +
				"8_1, 8_2, 8_3, 8_4, 8_5, 8_6, 8_7, 8_8) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?," +
				       " ?, ?, ?, ?, ?, ?, ?, ?," +
				       " ?, ?, ?, ?, ?, ?, ?, ?," +
				       " ?, ?, ?, ?, ?, ?, ?, ?," +
				       " ?, ?, ?, ?, ?, ?, ?, ?," +
				       " ?, ?, ?, ?, ?, ?, ?, ?," +
				       " ?, ?, ?, ?, ?, ?, ?, ?," +
				       " ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			int cellIndex = 1;
			for (int i = 1; i <= 8; i++) {

				for (int j = 1; j <= 8; j++) {
					ChessPiece piece = chessBoard.getPiece(new ChessPositionI(i,j));
					int pieceId = 0;
					if(piece != null){
						if(piece.getTeamColor().toString()=="WHITE"){
							pieceId=whitePieces.get(piece.getPieceType().toString());
						}else{
							pieceId=blackPieces.get(piece.getPieceType().toString());
						}
					}

					statement.setInt(cellIndex++, pieceId);
				}
				//System.out.println(cellIndex);
			}
			statement.executeUpdate();
			ResultSet rs = statement.getGeneratedKeys();
			if (rs.next()) {
				return rs.getInt(1);//System.out.println(rs.getInt(1));
			}
			throw new DataAccessException("No id returned");

		} catch (SQLException e) {
			throw new DataAccessException(e.toString());
		} finally {
			if (conn != null) {
				db.returnConnection(conn);
			}
		}
	}

	public void updateChessBoard(ChessBoardI chessBoard, int gameId) throws DataAccessException {
		System.out.println("Updating board");
		Connection conn = null;
		try {
			conn = db.getConnection();
			String sql = "UPDATE board B JOIN game G ON B.id = G.board_id SET " +
					"1_1=?, 1_2=?, 1_3=?, 1_4=?, 1_5=?, 1_6=?, 1_7=?, 1_8=?," +
					"2_1=?, 2_2=?, 2_3=?, 2_4=?, 2_5=?, 2_6=?, 2_7=?, 2_8=?," +
					"3_1=?, 3_2=?, 3_3=?, 3_4=?, 3_5=?, 3_6=?, 3_7=?, 3_8=?," +
					"4_1=?, 4_2=?, 4_3=?, 4_4=?, 4_5=?, 4_6=?, 4_7=?, 4_8=?," +
					"5_1=?, 5_2=?, 5_3=?, 5_4=?, 5_5=?, 5_6=?, 5_7=?, 5_8=?," +
					"6_1=?, 6_2=?, 6_3=?, 6_4=?, 6_5=?, 6_6=?, 6_7=?, 6_8=?," +
					"7_1=?, 7_2=?, 7_3=?, 7_4=?, 7_5=?, 7_6=?, 7_7=?, 7_8=?," +
					"8_1=?, 8_2=?, 8_3=?, 8_4=?, 8_5=?, 8_6=?, 8_7=?, 8_8=?" +
					" WHERE G.game_id=?";

			PreparedStatement statement = conn.prepareStatement(sql);
			int cellIndex = 1;
			for (int i = 1; i <= 8; i++) {

				for (int j = 1; j <= 8; j++) {
					ChessPiece piece = chessBoard.getPiece(new ChessPositionI(i, j));
					int pieceId = 0;
					if(piece != null){
						if(piece.getTeamColor().toString()=="WHITE"){
							pieceId=whitePieces.get(piece.getPieceType().toString());
						}else{
							pieceId=blackPieces.get(piece.getPieceType().toString());
						}
					}

					statement.setInt(cellIndex++, pieceId);
				}
			}
			statement.setInt(cellIndex, gameId);
			statement.executeUpdate();

		} catch (SQLException e) {
			throw new DataAccessException(e.toString());
		} finally {
			if (conn != null) {
				db.returnConnection(conn);
			}
		}
	}
	ChessBoard getChessBoard(ResultSet rs) throws SQLException {
		ChessBoardI chessBoard = new ChessBoardI();
		for (int row = 1; row <= 8; row++) {
			for (int col = 1; col <= 8; col++) {
				String columnName = row + "_" + col;
				int pieceInt = rs.getInt(columnName);
				//System.out.println(columnName + " -- " + pieceInt);
				ChessPiece piece = getPieceFromId(pieceInt);

				chessBoard.addPiece(new ChessPositionI(row, col), piece);
			}
		}
		//System.out.println(chessBoard);
		return chessBoard;
	}
	String getKey(Map<String, Integer> map, int value) {
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			if (entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		return null;
	}
	ChessPiece getPieceFromId(int i){
		if(i==0){
			return null;
		}
		if(i<7){
			return new ChessPieceI(ChessGame.TeamColor.WHITE, getType(getKey(whitePieces, i)));
		}else{
			return new ChessPieceI(ChessGame.TeamColor.BLACK, getType(getKey(blackPieces, i)));
		}
	}
	ChessPiece.PieceType getType(String typeText){
		for(ChessPiece.PieceType type : ChessPiece.PieceType.values()){
			if(type.toString().equals(typeText)){
				return type;
			}
		}
		return null;
	}
	public void setWhiteUser(String username, int game_id)throws DataAccessException{
		Connection conn = null;
		try {
			conn = db.getConnection();
			String sql = "UPDATE game SET white_user_id = (SELECT id FROM user WHERE username = ?) WHERE game_id=?;";
			PreparedStatement createStmt = conn.prepareStatement(sql);
			createStmt.setString(1, username);
			createStmt.setInt(2, game_id);

			createStmt.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException(e.toString());
		}finally {
			if (conn != null) {
				db.returnConnection(conn);
			}
		}
	}
	public void setBlackUser(String username, int game_id) throws DataAccessException{
		Connection conn = null;
		try {
			conn = db.getConnection();
			String sql = "UPDATE game SET black_user_id = (SELECT id FROM user WHERE username = ?) WHERE game_id=?;";
			PreparedStatement createStmt = conn.prepareStatement(sql);
			createStmt.setString(1, username);
			createStmt.setInt(2, game_id);
			createStmt.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException(e.toString());
		}finally {
			if (conn != null) {
				db.returnConnection(conn);
			}
		}
	}
	public void addSpectator(String username, int gameID)throws DataAccessException{
		Connection conn = null;
		try {
			conn = db.getConnection();
			String sql = "INSERT INTO spectator (user_id, game_id) VALUES ((SELECT id FROM user WHERE username = ?),?);";
			PreparedStatement createStmt = conn.prepareStatement(sql);
			createStmt.setString(1, username);
			createStmt.setInt(2, gameID);
			createStmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
			throw new DataAccessException(e.toString());
		}finally {
			if (conn != null) {
				db.returnConnection(conn);
			}
		}
	}
	public Set<String> getSpectators(int gameId)throws DataAccessException{
		Set<String> result = new HashSet<>();

		Connection conn = null;
		String sql = "SELECT user.username " +
				"FROM user " +
				"JOIN spectator ON user.id = spectator.user_id " +
				"WHERE spectator.game_id = ?;";
		try{
			conn=db.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, gameId);
			try(ResultSet rs = pstmt.executeQuery()){
				while(rs.next()){
					result.add(rs.getString("username"));
				}
			}
		}catch(SQLException e){
			throw new DataAccessException(e.toString());
		}finally {
			if (conn != null) {
				db.returnConnection(conn);
			}
		}
		return result;
	}
}