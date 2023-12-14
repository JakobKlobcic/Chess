package client_socket_message;

import webSocketMessages.userCommands.UserGameCommand;

public class Leave extends UserGameCommand{
	Integer gameID;
	public Leave(String authToken, Integer gameID){
		super(authToken);
		this.gameID=gameID;
		this.commandType=CommandType.LEAVE;
	}

	public Integer getGameID(){
		return gameID;
	}
}
