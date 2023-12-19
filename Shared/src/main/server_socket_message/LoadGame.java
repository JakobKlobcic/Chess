package server_socket_message;

import chess.ChessGame;
import chess.ChessGameImplementation;
import models.Game;
import webSocketMessages.serverMessages.ServerMessage;

public class LoadGame extends ServerMessage{
	ChessGameImplementation game;
	ChessGame.TeamColor color;
	Integer gameID;
	public LoadGame(ChessGameImplementation game,ChessGame.TeamColor color, Integer gameID){
		super(ServerMessageType.LOAD_GAME);
		this.game=game;
		this.color=color;
	}

	public Integer getGameID(){
		return gameID;
	}

	public ChessGameImplementation getGame(){
		return game;
	}

	public ChessGame.TeamColor getColor(){
		return color;
	}
}
