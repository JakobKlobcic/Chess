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
import server_socket_message.Error;
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
		System.out.println();
		System.out.println("Connection made");
		Map<String, List<String>> params = session.getUpgradeRequest().getParameterMap();
		//System.out.println(params);
		if (params.containsKey("authtoken")){
			String authtoken = params.get("authtoken").get(0);
			connectionPool.put(authtoken, session);
			System.out.println("Connected with authtoken: " + authtoken);
		}
	}

	@OnWebSocketMessage
	public void onMessage(Session session, String message) throws Exception {
		//System.out.println("messsage received");
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
		System.out.println();
		System.out.println("received JOIN_PLAYER");
		System.out.println(message);
		Gson gson = new Gson();
		JoinPlayer response = gson.fromJson(message, JoinPlayer.class);
		try{
			if(response.getGameID()==null){
				System.out.println("ERROR: gameID is null");
				session.getRemote().sendString(gson.toJson(new Error("ERROR: gameID is null")));
				return;
			}
			Game game = gameDAO.findGame(response.getGameID());
			if(game==null){
				System.out.println("ERROR: game does not exist");
				session.getRemote().sendString(gson.toJson(new Error("ERROR: game does not exist")));
				return;
			}
			//System.out.println("Black: "+game.getBlackUsername() + "; White: " + game.getWhiteUsername());
			String username =authDAO.username(response.getAuthToken());
			if(response.getPlayerColor() == ChessGame.TeamColor.WHITE && game.getWhiteUsername() != null && !game.getWhiteUsername().equals(authDAO.username(response.getAuthToken()))){
				System.out.println("ERROR: white is already taken");
				session.getRemote().sendString(gson.toJson(new Error("ERROR: white is already taken")));
				return;
			}

			if(response.getPlayerColor() == ChessGame.TeamColor.BLACK && game.getBlackUsername() != null && !game.getBlackUsername().equals(authDAO.username(response.getAuthToken()))){
				System.out.println("ERROR: black is already taken");
				session.getRemote().sendString(gson.toJson(new Error("ERROR: black is already taken")));
				return;
			}
			if(connectionPool.get(response.getAuthToken()) == null){
				connectionPool.put(response.getAuthToken(), session);
			}

			//Notify relevant players
			Map<String, Session> sessions = getSessions(response.getGameID(), session);
			for(Map.Entry<String,Session> entry : sessions.entrySet()){
				//entry.getKey() IS THE OWNER OF THE SESSION NOT THE USER WHO JOINED
				System.out.println("sending NOTIFICATION: "+username+" has joined the game as " + response.getPlayerColor());
				entry.getValue().getRemote().sendString(gson.toJson(new Notification(username+" has joined the game as " + response.getPlayerColor())));
			}
			//send LOAD_GAME to user that joined
			System.out.println("Sending LOAD_GAME to player");
			session.getRemote().sendString(gson.toJson(new LoadGame((ChessGameImplementation)gameDAO.findGame(response.getGameID()).getGame(), response.getPlayerColor(), response.getGameID())));
		}catch(IOException e){
			throw new RuntimeException(e);
		}catch(DataAccessException e){
			throw new RuntimeException(e);
		}
	}

	public void observe(Session session, String message){
		System.out.println();
		System.out.println("received JOIN_OBSERVER");
		System.out.println(message);
		Gson gson = new Gson();
		JoinObserver response = gson.fromJson(message, JoinObserver.class);
		if(connectionPool.get(response.getAuthToken()) == null){
			connectionPool.put(response.getAuthToken(), session);
		}

		try{
			String username =authDAO.username(response.getAuthToken());
			Game game = gameDAO.findGame(response.getGameID());
			if(game==null){
				System.out.println("ERROR: game does not exist");
				session.getRemote().sendString(gson.toJson(new Error("ERROR: game does not exist")));
				return;
			}
			Map<String, Session> sessions = getSessions(response.getGameID(), session);
			for(Map.Entry<String,Session> entry : sessions.entrySet()){
				System.out.println("NOTIFICATION:"+username+" has joined the game as an observer");
				entry.getValue().getRemote().sendString(gson.toJson(new Notification(username+" has joined the game as an observer")));
			}
			System.out.println("Sending LOAD_GAME to observer");
			session.getRemote().sendString(gson.toJson(new LoadGame((ChessGameImplementation)gameDAO.findGame(response.getGameID()).getGame(), ChessGame.TeamColor.WHITE, response.getGameID())));
		}catch(IOException e){
			throw new RuntimeException(e);
		}catch(DataAccessException e){
			throw new RuntimeException(e);
		}
	}

	public void move(Session session, String message){
		//validate move - users can only move their own figures
		//write to database
		//check for check
		//check for checkmate
		//return the new game object
		//send LOAD_GAME to players
		Gson gson = new Gson();
		MakeMove response = gson.fromJson(message, MakeMove.class);
		System.out.println();
		System.out.println("Attempt move:"+response.getMove());
		try{
			Map<String, Session> sessions = getSessions(response.getGameID(), session);

			Game game = gameDAO.findGame(response.getGameID());
			ChessGameImplementation gameImplementation = game.getGameI();
			ChessPiece movedPiece = game.getGameI().getBoardI().getPiece(response.getMove().getStartPosition());
			ChessGame.TeamColor userMakingMove = gameImplementation.getTeamTurn();
			gameImplementation.setTeamTurn(userMakingMove);
			ChessGame.TeamColor otherColor = userMakingMove == ChessGame.TeamColor.WHITE? ChessGame.TeamColor.BLACK: ChessGame.TeamColor.WHITE;

				if(gameImplementation.isValidMove(response.getMove())){
					gameImplementation.makeMove(response.getMove());
					gameDAO.updateChessBoard(gameImplementation.getBoardI(), game.getGameID());
					for(Map.Entry<String, Session> entry : sessions.entrySet()){
						System.out.println("LOAD_GAME+NOTIFICATION: "+entry.getKey() + " has made a move");
						entry.getValue().getRemote().sendString(gson.toJson(new LoadGame(gameImplementation, userMakingMove, response.getGameID())));

						entry.getValue().getRemote().sendString(gson.toJson(new Notification(
							entry.getKey() +
							" moved his "+
							movedPiece.getPieceType().toString()+
							" to "+
							response.getMove().getEndPosition().getRow()+
							(char)(response.getMove().getEndPosition().getColumn()+'a')
						)));
					}
					if(gameImplementation.isInCheckmate(otherColor)){
						for(Map.Entry<String, Session> entry : sessions.entrySet()){
							System.out.println(otherColor + " has lost by Checkmate ");
							entry.getValue().getRemote().sendString(gson.toJson(new Notification(
									otherColor + " has lost by Checkmate "
							)));
						}
					}else if(gameImplementation.isInCheck(otherColor)){
						for(Map.Entry<String, Session> entry : sessions.entrySet()){
							System.out.println("check!");
							entry.getValue().getRemote().sendString(gson.toJson(new Notification("Check!")));
						}
					}else if(gameImplementation.isInStalemate(otherColor)){
						for(Map.Entry<String, Session> entry : sessions.entrySet()){
							System.out.println("Stalemate!");
							entry.getValue().getRemote().sendString(gson.toJson(new Notification("Stalemate!")));
						}
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
			Map<String, Session> sessions = getSessions(response.getGameID(), session);
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
			Map<String, Session> sessions = getSessions(response.getGameID(), session);
			for(Map.Entry<String,Session> entry : sessions.entrySet()){
				System.out.println(entry.getKey()+" has resigned the game!");
				entry.getValue().getRemote().sendString(gson.toJson(new Notification(entry.getKey()+" has resigned the game!")));
			}

		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}

	private Map<String, Session> getSessions(int gameID, Session currentSession){
		Map<String, Session> sessions = new HashMap<>();
		try{
			for(String username : gameDAO.getSpectators(gameID)){
				String token = authDAO.token(username);
				if(token != null && connectionPool.get(token) != null){
					if(currentSession == connectionPool.get(token))//the session could not be equal
						continue;
					sessions.put(username, connectionPool.get(token));
				}
			}
			Game game = gameDAO.findGame(gameID);
			//System.out.println(game);
			String black = authDAO.token(game.getBlackUsername());
			String white = authDAO.token(game.getWhiteUsername());

			if(white != null && connectionPool.get(white) != null && connectionPool.get(white)!=currentSession){
				sessions.put(game.getWhiteUsername(), connectionPool.get(white));
			}
			if(black != null && connectionPool.get(black) != null && connectionPool.get(black)!=currentSession){
				sessions.put(game.getBlackUsername(),connectionPool.get(black));
			}
		}catch(DataAccessException e){
			throw new RuntimeException(e);
		}
		return sessions;
	}

}
