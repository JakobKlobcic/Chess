package socket_handler;

import org.eclipse.jetty.websocket.api.Session;


public class SocketHandler{
	public static String handleMessage(Session session, String message){
		String[] args = message.split(" ");

		if(args[0]=="JOIN_PLAYER"){


		}else if(args[0]=="JOIN_OBSERVER"){

		}else if(args[0]=="MAKE_MOVE"){

		}else if(args[0]=="LEAVE"){

		}else if(args[0]=="RESIGN"){

		}
		return message;
	}
}
