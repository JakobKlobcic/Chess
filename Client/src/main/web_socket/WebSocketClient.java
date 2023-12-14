package web_socket;

import javax.websocket.*;
import java.net.URI;

public class WebSocketClient extends Endpoint{
	private Session session;

	public WebSocketClient(String authtoken) throws Exception {
		URI uri = new URI("ws://localhost:8080/connect?authtoken="+authtoken);
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		this.session = container.connectToServer(this, uri);
		this.session.setMaxIdleTimeout(600000);
	}

	public Session getSession(){
		return session;
	}
	public void send(String msg) throws Exception {this.session.getBasicRemote().sendText(msg);}

	@OnOpen
	public void onOpen(Session session, EndpointConfig endpointConfig) {

	}
}
