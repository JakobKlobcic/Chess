package server_socket_message;

import chess.ChessGame;
import chess.ChessGameImplementation;
import models.Game;
import webSocketMessages.serverMessages.ServerMessage;

public class LoadGame extends ServerMessage{
	ChessGameImplementation game;
	//ChessGame.TeamColor color;
	public LoadGame(ChessGameImplementation game/*,ChessGame.TeamColor color*/){
		super(ServerMessageType.LOAD_GAME);
		this.game=game;
		//this.color=color;
	}

	public ChessGameImplementation getGame(){
		return game;
	}

	//public ChessGame.TeamColor getColor(){
	//	return color;
	//}
}
