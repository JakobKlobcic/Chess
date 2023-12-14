package client_socket_message;

import chess.ChessGame;
import webSocketMessages.userCommands.UserGameCommand;

public class JoinPlayer extends UserGameCommand{
	Integer gameID;
	ChessGame.TeamColor playerColor;

	public JoinPlayer(String authToken, Integer gameID,ChessGame.TeamColor playerColor){
		super(authToken);
		this.gameID=gameID;
		this.playerColor=playerColor;
		this.commandType=CommandType.JOIN_PLAYER;
	}

	public Integer getGameID(){
		return gameID;
	}

	public ChessGame.TeamColor getPlayerColor(){
		return playerColor;
	}
}
