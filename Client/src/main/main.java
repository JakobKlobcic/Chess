import com.sun.tools.javac.Main;
import models.Game;
import responses.CreateGameResponse;
import responses.ListGamesResponse;
import responses.RegisterResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class main{
	Boolean signedIn = false;
	String auth = null;

	public static void main(String [] args){
		System.out.println("Welcome to 240 chess. Type help to get started.");
		main mainObj = new main();
		mainObj.requestInput();
 	}

	public void requestInput(){
		if(signedIn){
			System.out.print("[LOGGED_IN] >>> ");
		}else{
			System.out.print("[LOGGED_OUT] >>> ");
		}
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(System.in));

		// Reading data using readLine
		String input = null;
		try{
			input = reader.readLine();
			dealWithInput(input.split(" "));
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}

	public void dealWithInput(String [] args){
		if(signedIn){
			if(args[0].equals("create")){
				if(args.length!=2){
					//TODO: Throw exception
				}
				this.createGame(args[1]);
			}else if(args[0].equals("list")){
				if(args.length!=1){
					//TODO: Throw exception
				}
				this.listGames();
			}else if(args[0].equals("join")){

			}else if(args[0].equals("observe")){

			}else if(args[0].equals("logout")){

			}else if(args[0].equals("quit")){

			}else if(args[0].equals("help")){
				this.help();
			}
		}else{
			if(args[0].equals("register")){
				if(args.length!=4){
					//TODO: Throw exception
				}
				this.register(args[1], args[2], args[3]);
			}else if(args[0].equals("login")){

			}else if(args[0].equals("quit")){

			}else if(args[0].equals("help")){
				this.help();
			}
		}
	}

	public void help(){
		if(signedIn){
			System.out.print(
				"create <NAME> - a game\n"+
				"list - games\n"+
				"join <ID> [WHITE|BLACK|<empty] - a game\n"+
				"observe <ID> - a game\n"+
				"logout - when you are done\n"+
				"quit - playing chess\n"+
				"help - with possible commands\n"
			);
		}else{
			System.out.print(
				"register <USERNAME> <PASSWORD> <EMAIL> - to create an account\n"+
				"login <USERNAME> <PASSWORD> - to play chess\n"+
				"quit - playing chess\n"+
				"help - with possible commands\n"
			);
		}
		this.requestInput();
	}

	public void register(String username, String password, String email){
		try{
			RegisterResponse response = ApiCalls.register(username, password, email);
			if(response.getStatus()==200){
				System.out.println("Logged in as "+response.getUsername());
				signedIn=true;
				auth= response.getAuthToken();
			}else{
				//TODO: Throw exception
			}
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		this.requestInput();
	}

	public void createGame(String gameName){
		try{
			CreateGameResponse response = ApiCalls.createGame(gameName, auth);
			if(response.getStatus()==200){
				System.out.println("Created game: "+response.getGameID());
			}else{
				//TODO: Throw exception
			}
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		this.requestInput();
	}

	public void listGames(){
		try{
			ListGamesResponse response = ApiCalls.listGames(auth);
			if(response.getStatus()==200){
				System.out.println("Games:");
				for(Game game : response.getGames()){
					System.out.println("Game ID: "+game.getGameID()+" Game name: "+game.getGameName());
				}
			}else{
				//TODO: Throw exception
			}
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		this.requestInput();
	}

	public  void login(String username, String password){}

	public  void logout(){

	}
	public void quit(){}

}
