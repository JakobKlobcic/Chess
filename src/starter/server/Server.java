package server;

import handlers.Handlers;

import static spark.Spark.*;

public class Server{
	public void start(){
		WebSocketServer webSocket = WebSocketServer.getInstance();
		System.out.println("Strating server...");
		port(8080);
		webSocket.start();
		delete("/db", Handlers::handleClearApplication);
		delete("/session", Handlers::handleLogout);
		post("/session", Handlers::handleLogin);
		post("/user", Handlers::handleRegister);
		post("/game", Handlers::handleCreateGame);
		get("/game", Handlers::handleListGames);
		put("/game", Handlers::handleJoinGame);
		System.out.println("Server ready!");
	}
}