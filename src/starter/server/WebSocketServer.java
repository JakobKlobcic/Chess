package server;

import chess.*;
import client_socket_message.*;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.GameDAO;
import models.Game;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server_socket_message.LoadGame;
import server_socket_message.Notification;
import spark.Spark;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebSocket
public class WebSocketServer{

	private static WebSocketServer instance;

	public static synchronized WebSocketServer getInstance() {
		if (instance == null) {
			instance = new WebSocketServer();
		}
		return instance;
	}
	Map<String, Session> connectionPool = new HashMap<>();
	GameDAO gameDAO= new GameDAO();
	AuthDAO authDAO = new AuthDAO();

	public void start() {
		Spark.port(8080);
		Spark.webSocket("/connect", WebSocketServer.class);
		Spark.init();
	}

	//@OnClose remove the connection from the connectionPool
	@OnWebSocketConnect
	public void onConnect(Session session) throws Exception {
		System.out.println("Connection made");
		Map<String, List<String>> params = session.getUpgradeRequest().getParameterMap();
		System.out.println(params);
		if (params.containsKey("authtoken")){
			String authtoken = params.get("authtoken").get(0);
			connectionPool.put(authtoken, session);
			System.out.println("Connected with authtoken: " + authtoken);
		}
	}

	@OnWebSocketMessage
	public void onMessage(Session session, String message) throws Exception {
		System.out.println("messsage received");
		Gson gson = new Gson();
		UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
		String[] args = message.split(" ");
		if(args.length==0){
			throw new RuntimeException("bad socket request");
		}
		if(command.getCommandType() == UserGameCommand.CommandType.JOIN_PLAYER){
			join(session, message);
		}else if(command.getCommandType() == UserGameCommand.CommandType.JOIN_OBSERVER){
			observe(session, message);
		}else if(command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE){
			move(session, message);
		}else if(command.getCommandType() == UserGameCommand.CommandType.LEAVE){
			leave(session, message);
		}else if(command.getCommandType() == UserGameCommand.CommandType.RESIGN){
			resign(session, message);
		}
	}
	public Session getConnection(String authtoken){
		return connectionPool.get(authtoken);
	}

	public void join(Session session, String message){
		System.out.println("join message");
		Gson gson = new Gson();
		JoinPlayer response = gson.fromJson(message, JoinPlayer.class);
		try{
			//TODO: exclude the session of the current player
			//Notify relevant players
			Map<String, Session> sessions = getSessions(response.getGameID());
			for(Map.Entry<String,Session> entry : sessions.entrySet()){
				System.out.println(entry.getKey()+" has joined the game as " + response.getPlayerColor());
				entry.getValue().getRemote().sendString(gson.toJson(new Notification(entry.getKey()+" has joined the game as " + response.getPlayerColor())));
			}
			//send LOAD_GAME to user that joined
			System.out.println("Sending LOAD_GAME");
			session.getRemote().sendString(gson.toJson(new LoadGame((ChessGameImplementation)gameDAO.findGame(response.getGameID()).getGame())));
		}catch(IOException e){
			throw new RuntimeException(e);
		}catch(DataAccessException e){
			throw new RuntimeException(e);
		}
	}

	public void observe(Session session, String message){
		System.out.println("observe message");
		Gson gson = new Gson();
		JoinObserver response = gson.fromJson(message, JoinObserver.class);
		try{
			//TODO: exclude the session of the current player
			Map<String, Session> sessions = getSessions(response.getGameID());
			for(Map.Entry<String,Session> entry : sessions.entrySet()){
				System.out.println(entry.getKey()+" has joined the game as an observer");
				entry.getValue().getRemote().sendString(gson.toJson(new Notification(entry.getKey()+" has joined the game as an observer")));
			}

		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}

	public void move(Session session, String message){
		//validate move
		//write to database
		//check for check
		//check for checkmate
		//return the new game object
		//send LOAD_GAME to players
		Gson gson = new Gson();
		MakeMove response = gson.fromJson(message, MakeMove.class);
		System.out.println("Attempt move:"+response.getMove());
		try{
			Map<String, Session> sessions = getSessions(response.getGameID());

			Game game = gameDAO.findGame(response.getGameID());
			ChessGameImplementation gameImplementation = game.getGameI();
			ChessPiece movedPiece = game.getGameI().getBoardI().getPiece(response.getMove().getStartPosition());
			ChessGame.TeamColor colorBeforeMove = gameImplementation.getTeamTurn();
			gameImplementation.setTeamTurn(colorBeforeMove);
			ChessGame.TeamColor colorAfterMove = null;
				if(gameImplementation.isValidMove(response.getMove())){
					gameImplementation.makeMove(response.getMove());
					gameDAO.updateChessBoard(gameImplementation.getBoardI(), game.getGameID());
					colorAfterMove = colorBeforeMove.toString().equals("WHITE")? ChessGame.TeamColor.BLACK: ChessGame.TeamColor.WHITE;
					for(Map.Entry<String, Session> entry : sessions.entrySet()){
						System.out.println(entry.getKey() + " has made a move");
						entry.getValue().getRemote().sendString(gson.toJson(new LoadGame(gameImplementation)));
						entry.getValue().getRemote().sendString(gson.toJson(new Notification(
							entry.getKey() +
							" moved his "+
							movedPiece.getPieceType().toString()+
							" to "+
							response.getMove().getEndPosition().getRow()+
							(char)(response.getMove().getEndPosition().getColumn()+'a')
						)));
					}
					if(gameImplementation.isInCheckmate(colorAfterMove)){
						for(Map.Entry<String, Session> entry : sessions.entrySet()){
							System.out.println(colorAfterMove + " has lost by Checkmate ");
							entry.getValue().getRemote().sendString(gson.toJson(new Notification(
									colorAfterMove + " has lost by Checkmate "
							)));
						}
					}else if(gameImplementation.isInStalemate(colorAfterMove)){
						//notify stalemate
					}else if(gameImplementation.isInCheck(colorAfterMove)){
						//notify who is in check
					}
				}else{
				//error invalid move
			}

		}catch(IOException e){
			throw new RuntimeException(e);
		}catch(DataAccessException e){
			throw new RuntimeException(e);
		}catch(InvalidMoveException e){
			throw new RuntimeException(e);
		}
	}

	public void leave(Session session, String message){
		System.out.println("leave message");
		Gson gson = new Gson();
		Leave response = gson.fromJson(message, Leave.class);
		try{
			//TODO: exclude the session of the current player
			Map<String, Session> sessions = getSessions(response.getGameID());
			for(Map.Entry<String,Session> entry : sessions.entrySet()){
				System.out.println(entry.getKey()+" has left the game!");
				entry.getValue().getRemote().sendString(gson.toJson(new Notification(entry.getKey()+" has left the game!")));
			}

		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}

	public void resign(Session session, String message){
		System.out.println("resign message");
		Gson gson = new Gson();
		Resign response = gson.fromJson(message, Resign.class);
		try{
			//TODO: exclude the session of the current player
			Map<String, Session> sessions = getSessions(response.getGameID());
			for(Map.Entry<String,Session> entry : sessions.entrySet()){
				System.out.println(entry.getKey()+" has resigned the game!");
				entry.getValue().getRemote().sendString(gson.toJson(new Notification(entry.getKey()+" has resigned the game!")));
			}

		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}

	private Map<String, Session> getSessions(int gameID){
		Map<String, Session> sessions = new HashMap<>();
		try{
			for(String username : gameDAO.getSpectators(gameID)){
				String token = authDAO.token(username);
				if(token != null && connectionPool.get(token) != null){
					sessions.put(username, connectionPool.get(token));
				}
			}
			Game game = gameDAO.findGame(gameID);
			System.out.println(game);
			String black = authDAO.token(game.getBlackUsername());
			String white = authDAO.token(game.getWhiteUsername());

			if(white != null && connectionPool.get(white) != null){
				sessions.put(game.getWhiteUsername(), connectionPool.get(white));
			}
			if(black != null && connectionPool.get(black) != null){
				sessions.put(game.getBlackUsername(),connectionPool.get(black));
			}
		}catch(DataAccessException e){
			throw new RuntimeException(e);
		}
		return sessions;
	}

}
