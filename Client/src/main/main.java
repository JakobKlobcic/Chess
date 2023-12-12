import chess.*;
import com.sun.tools.javac.Main;
import models.Game;
import responses.*;
import ui.EscapeSequences;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static ui.EscapeSequences.WHITE_KING;

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
		String input;
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
					this.wrongInput("Please structure your input like this: create <NAME>");
					return;
				}
				this.createGame(args[1]);
			}else if(args[0].equals("list")){
				if(args.length!=1){
					this.wrongInput("Please structure your input like this: list");
					return;
				}
				this.listGames();
			}else if(args[0].equals("join")){
				if(args.length!=3){
					this.wrongInput("Please structure your input like this: join <ID> [WHITE|BLACK|<empty]");
					return;
				}
				this.join(args[1], Integer.parseInt(args[2]));
			}else if(args[0].equals("observe")){
				if(args.length!=2){
					this.wrongInput("Please structure your input like this: observe <ID>");
					return;
				}
				this.observe(Integer.parseInt(args[1]));
			}else if(args[0].equals("logout")){
				this.logout();
			}else if(args[0].equals("quit")){
				if(args.length!=1){
					this.wrongInput("Please structure your input like this: logout");
					return;
				}
				this.quit();
			}else if(args[0].equals("help")){
				if(args.length!=1){
					this.wrongInput("Please structure your input like this: help");
					return;
				}
				this.help();
			}else{
				this.wrongInput(args[0]+" is not a valid command type 'help' for valid commands");
			}
		}else{
			if(args[0].equals("register")){
				if(args.length!=4){
					this.wrongInput("Please structure your input like this: register <USERNAME> <PASSWORD> <EMAIL>");
					return;
				}
				this.register(args[1], args[2], args[3]);
			}else if(args[0].equals("login")){
				if(args.length!=3){
					this.wrongInput("Please structure your input like this: login <USERNAME> <PASSWORD>");
					return;
				}
				this.login(args[1], args[2]);
			}else if(args[0].equals("quit")){
				this.quit();
			}else if(args[0].equals("help")){
				this.help();
			}else if(args[0].equals("test")){
				ChessBoardI board= new ChessBoardI();
				board.resetBoard();
				board.makeMove(new ChessMoveI(new ChessPositionI(7,4), new ChessPositionI(5,4)));
				drawBoard(board);
			}else{
				this.wrongInput(args[0]+" is not a valid command type 'help' for valid commands");
			}
		}
	}

	public void wrongInput(String message){
		System.out.println(message);
		this.requestInput();
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

	public void join(String color, int gameID){
		try{
			JoinGameResponse response = ApiCalls.joinGame(auth, color, gameID);
			if(response.getStatus()==200){
				this.drawBoard(getBoard(gameID));
			}else{
				System.out.println(response.getMessage());
			}
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		this.requestInput();
	}

	public void observe( int gameID){
		try{
			JoinGameResponse response = ApiCalls.joinGame(auth, null, gameID);
			if(response.getStatus()==200){
				this.drawBoard(getBoard(gameID));
			}else{
				System.out.println(response.getMessage());
			}
		}catch(IOException e){
			throw new RuntimeException(e);
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
				System.out.println(response.getMessage());
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
			}else {
				System.out.println(response.getMessage());
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
				System.out.println(EscapeSequences.SET_TEXT_UNDERLINE+"|\tID\t|\tName\t\t\t"+EscapeSequences.RESET_TEXT_UNDERLINE);
				for(Game game : response.getGames()){
					System.out.println("|\t"+game.getGameID()+"\t|\t"+game.getGameName());
				}
			}else{
				System.out.println(response.getMessage());
			}
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		this.requestInput();
	}

	public  void login(String username, String password){
		try{
			LoginResponse response = ApiCalls.login(username, password);
			if(response.getStatus()==200){
				signedIn = true;
				auth = response.getAuthToken();
				System.out.println("Logged in as: "+response.getUsername());
			}else{
				System.out.println(response.getMessage());
			}
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		this.requestInput();
	}

	public  void logout(){
		try{
			LogoutResponse response = ApiCalls.logout(auth);
			if(response.getStatus()==200){
				signedIn = false;
				auth = null;
				System.out.println("Successfully Logged out!");
			}else{
				System.out.println(response.getMessage());
			}
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		this.requestInput();
	}

	public void quit(){}

	ChessBoardI getBoard(int id){
		ChessGameImplementation game = null;
		try{

			ListGamesResponse response = ApiCalls.listGames(auth);
			if(response.getStatus()==200){
				for(Game g :response.getGames()){
					if(g.getGameID()==id){
						game=g.getGameI();
						break;
					}
				}
			}else{
				System.out.println(response.getMessage());
			}
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		if (game==null)
			return null;
		if(game.getBoardI()==null)
			return null;
		return game.getBoardI();
	}

	public void drawBoard(ChessBoardI chessBoard){

		if( chessBoard == null){
			System.out.println("There was a problem finding the board");
			return;
		}
		ChessPieceI[][] board = chessBoard.getBoard();
		StringBuilder sb = new StringBuilder();
		sb.append(EscapeSequences.SET_BG_COLOR_DARK_GREY + "    a   b   c  d   e  f   g   h " + EscapeSequences.RESET_BG_COLOR + "\n");
		for(int i = 0; i < 8; i++){

			sb.append(EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.SET_BG_COLOR_DARK_GREY + " "+(i+1) + " " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
			for(int j = 7; j >= 0; j--){

				ChessPieceI piece = board[i][j];
				if(i%2==j%2){
					sb.append(EscapeSequences.SET_BG_COLOR_BLACK);
				}else{
					sb.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
				}
				String type="";
				String color="";
				if(piece == null){
					type = EscapeSequences.EMPTY;
				}else{

					if(piece.getTeamColor().toString() == "WHITE"){
						color = EscapeSequences.SET_TEXT_COLOR_BLUE;
					}else{
						color = EscapeSequences.SET_TEXT_COLOR_RED;
					}

					if(piece.getPieceType().toString() == "KING"){
						type = (piece.getTeamColor().toString() == "WHITE") ? EscapeSequences.WHITE_KING : EscapeSequences.BLACK_KING;
					}
					else if(piece.getPieceType().toString() == "QUEEN"){
						type = (piece.getTeamColor().toString() == "WHITE") ? EscapeSequences.WHITE_QUEEN : EscapeSequences.BLACK_QUEEN;
					}
					else if(piece.getPieceType().toString() == "KNIGHT"){
						type = (piece.getTeamColor().toString() == "WHITE") ? EscapeSequences.WHITE_KNIGHT : EscapeSequences.BLACK_KNIGHT;
					}
					else if(piece.getPieceType().toString() == "BISHOP"){
						type = (piece.getTeamColor().toString() == "WHITE") ? EscapeSequences.WHITE_BISHOP : EscapeSequences.BLACK_BISHOP;
					}
					else if(piece.getPieceType().toString() == "ROOK"){
						type = (piece.getTeamColor().toString() == "WHITE") ? EscapeSequences.WHITE_ROOK : EscapeSequences.BLACK_ROOK;
					}
					else if(piece.getPieceType().toString() == "PAWN"){
						type = (piece.getPieceType().toString() == "WHITE") ? EscapeSequences.WHITE_PAWN : EscapeSequences.BLACK_PAWN;
					}


				}
				sb.append(color + type + EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_BG_COLOR);
				//sb.append("\t");
			}
			sb.append("\n"+"\033[0m");
		}
		sb.append("\033[0m");

		sb.append("\n\n");

		sb.append(EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.SET_BG_COLOR_DARK_GREY + "    h   g   f  e   d  c   b   a " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n");
		for(int i = 7; i >= 0; i--){

			sb.append(EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.SET_BG_COLOR_DARK_GREY + " "+(i+1) + " " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
			for(int j = 0; j < 8; j++){
				ChessPieceI piece = board[i][j];
				if(i%2==j%2){
					sb.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
				}else{
					sb.append(EscapeSequences.SET_BG_COLOR_BLACK);
				}
				String type="";
				String color="";
				if(piece == null){
					type = EscapeSequences.EMPTY;
				}else{

					if(piece.getTeamColor().toString() == "WHITE"){
						color = EscapeSequences.SET_TEXT_COLOR_BLUE;
					}else{
						color = EscapeSequences.SET_TEXT_COLOR_RED;
					}

					if(piece.getPieceType().toString() == "KING"){
						type = (piece.getTeamColor().toString() == "WHITE") ? EscapeSequences.WHITE_KING : EscapeSequences.BLACK_KING;
					}
					else if(piece.getPieceType().toString() == "QUEEN"){
						type = (piece.getTeamColor().toString() == "WHITE") ? EscapeSequences.WHITE_QUEEN : EscapeSequences.BLACK_QUEEN;
					}
					else if(piece.getPieceType().toString() == "KNIGHT"){
						type = (piece.getTeamColor().toString() == "WHITE") ? EscapeSequences.WHITE_KNIGHT : EscapeSequences.BLACK_KNIGHT;
					}
					else if(piece.getPieceType().toString() == "BISHOP"){
						type = (piece.getTeamColor().toString() == "WHITE") ? EscapeSequences.WHITE_BISHOP : EscapeSequences.BLACK_BISHOP;
					}
					else if(piece.getPieceType().toString() == "ROOK"){
						type = (piece.getTeamColor().toString() == "WHITE") ? EscapeSequences.WHITE_ROOK : EscapeSequences.BLACK_ROOK;
					}
					else if(piece.getPieceType().toString() == "PAWN"){
						type = (piece.getTeamColor().toString() == "WHITE") ? EscapeSequences.WHITE_PAWN : EscapeSequences.BLACK_PAWN;
					}


				}
				sb.append(color + type + EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_BG_COLOR);
				//sb.append("\t");
			}
			sb.append("\n"+"\033[0m");
		}
		sb.append("\033[0m");
		System.out.println(sb);
	}

}
