package client_socket_message;

import webSocketMessages.userCommands.UserGameCommand;

public class Resign extends UserGameCommand{
	Integer gameID;
	public Resign(String authToken, Integer gameID){
		super(authToken);
		this.gameID=gameID;
		this.commandType=CommandType.RESIGN;
	}

	public Integer getGameID(){
		return gameID;
	}
}
