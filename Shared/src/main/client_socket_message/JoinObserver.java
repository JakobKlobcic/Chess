package client_socket_message;

import webSocketMessages.userCommands.UserGameCommand;

public class JoinObserver extends UserGameCommand{
	Integer gameID;
	public JoinObserver(String authToken,Integer gameID){
		super(authToken);
		this.gameID=gameID;
		this.commandType=CommandType.JOIN_OBSERVER;
	}

	public Integer getGameID(){
		return gameID;
	}

	public String getAuthToken(){
		return super.getAuthString();
	}
}
