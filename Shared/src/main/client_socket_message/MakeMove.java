package client_socket_message;

import chess.ChessMove;
import chess.ChessMoveI;
import webSocketMessages.userCommands.UserGameCommand;

public class MakeMove extends UserGameCommand{
	Integer gameID;
	ChessMoveI move;
	public MakeMove(String authToken, Integer gameID, ChessMoveI move){
		super(authToken);
		this.gameID=gameID;
		this.move=move;
		this.commandType=CommandType.MAKE_MOVE;
	}

	public Integer getGameID(){
		return gameID;
	}

	public ChessMoveI getMove(){
		return move;
	}
}
