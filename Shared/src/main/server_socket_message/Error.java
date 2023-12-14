package server_socket_message;

import webSocketMessages.serverMessages.ServerMessage;

public class Error extends ServerMessage{
	String errorMessage;
	public Error(String errorMessage){
		super(ServerMessageType.ERROR);
		this.errorMessage=errorMessage;
	}

	public String getErrorMessage(){
		return errorMessage;
	}
}
