import chess.*;
import client_socket_message.JoinObserver;
import client_socket_message.JoinPlayer;
import client_socket_message.MakeMove;
import com.google.gson.Gson;
import models.Game;
import responses.*;
import server_socket_message.Error;
import server_socket_message.LoadGame;
import server_socket_message.Notification;
import ui.EscapeSequences;
import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.MessageHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class main{
	Boolean signedIn = false;
	Boolean inGame = false;
	String auth = null;
	WebSocketClient ws = null;
	static main mainObj = null;

	ChessBoardI currentBoard=null;
	ChessGame.TeamColor currentColor=null;

	Integer currentGameID=null;


	public static void main(String [] args){
		System.out.println("Welcome to 240 chess. Type help to get started.");
		main mainObj = new main();
		//mainObj.socketConnection();
		mainObj.requestInput();
	}

	public void requestInput(){
		if(inGame){
			System.out.print("[IN_GAME] >>> ");
		}else if(signedIn){
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
		//todo: add move, resign leave
		if(inGame){
			if(args[0].equals("move")){
				if(args.length!=3){
					this.wrongInput("Please structure your input like this: move <piece_coordinate> <move_coordinate>");
					return;
				}
				this.move(args[1], args[2]);
			}else if(args[0].equals("legal")){
				if(args.length!=2){
					this.wrongInput("Please structure your input like this: legal <piece_coordinate>");
					return;
				}
				this.legal(args[1]);
			}else if(args[0].equals("resign")){
				this.resign();
			}else if(args[0].equals("leave")){
				this.leave();
			}else if(args[0].equals("quit")){
				this.quit();
			}else if(args[0].equals("help")){
				this.help();
			}
		}else if(signedIn){
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
				this.join(args[2], Integer.parseInt(args[1]));
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
				drawBoard(board, ChessGame.TeamColor.WHITE, new ChessPositionI(1,2));
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
		if(inGame){
			System.out.print(
					"move <piece_coordinate> <move_coordinate> - move piece\n"+
					"legal <piece_coordinate> - shows possible moves for piece\n"+
					"resign - resign game\n"+
					"leave - leave game\n"+
					"quit - playing chess\n"+
					"help - with possible commands\n"
			);
		}else if(signedIn){
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
				try{
					currentBoard=getBoard(gameID);
					currentColor= color.equals("WHITE") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
					currentGameID=gameID;

					socketConnection();
					inGame=true;
					Gson gson = new Gson();
					ws.send(gson.toJson(new JoinPlayer(auth, gameID, ChessGame.TeamColor.valueOf(color))));
				}catch(Exception e){
					throw 	new RuntimeException(e);
				}
				//this.drawBoard(currentBoard, currentColor);
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
				currentBoard=getBoard(gameID);
				currentColor= ChessGame.TeamColor.WHITE;
				currentGameID=gameID;
				socketConnection();
				inGame=true;
				Gson gson = new Gson();
				ws.send(gson.toJson(new JoinObserver(auth, gameID)));
				//this.drawBoard(currentBoard, currentColor);
			}else{
				System.out.println(response.getMessage());
			}
		}catch(IOException e){
			throw new RuntimeException(e);
		}catch(Exception e){
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

	public void login(String username, String password){
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

	public void logout(){
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

	public void move(String starting, String ending){
		if(starting.length()==2 && ending.length()==2){
			if(Character.isDigit(starting.charAt(0)) && Character.isLetter(starting.charAt(1)) && Character.isDigit(ending.charAt(0)) && Character.isLetter(ending.charAt(1))){
				ChessPositionI s = new ChessPositionI(Character.getNumericValue(starting.charAt(0)), starting.charAt(1)-'a'+1);
				ChessPositionI e = new ChessPositionI(Character.getNumericValue(ending.charAt(0)), ending.charAt(1)-'a'+1);
				//System.out.println("Starting: "+ s + "; Ending:"+e);
				Gson gson = new Gson();
				try{
					ws.send(gson.toJson(new MakeMove(auth,currentGameID,new ChessMoveI(s,e))));
				}catch(Exception ex){
					throw new RuntimeException(ex);
				}
				//drawBoard(currentBoard,currentColor, position);

			}
		}else{
			//problem
		}
		requestInput();
	}

	public void legal(String pieceLocation){
		if(pieceLocation.length()==2){
			if(Character.isDigit(pieceLocation.charAt(0)) && Character.isLetter(pieceLocation.charAt(1))){
				ChessPositionI position = new ChessPositionI(Character.getNumericValue(pieceLocation.charAt(0)), pieceLocation.charAt(1)-'a'+1);
				drawBoard(currentBoard,currentColor, position);
				requestInput();
			}
		}else{
			//problem
		}
	}

	public void leave(){
		inGame=false;
		currentGameID=null;
		currentColor=null;
		currentBoard=null;
		requestInput();
	}

	public void resign(){
		inGame=false;
		requestInput();
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

	public void drawBoard(ChessBoardI chessBoard, ChessGame.TeamColor teamColor, ChessPosition... position){
		if( chessBoard == null){
			System.out.println("There was a problem finding the board");
			return;
		}
		System.out.println();
		ChessPieceI[][] board = chessBoard.getBoard();
		StringBuilder sb = new StringBuilder();
		List<ChessPositionI> legalmoves=new ArrayList<>();
		if(position.length>0){
			for(ChessMoveI move : chessBoard.getTeamMoves(chessBoard.getPiece(position[0]).getTeamColor())){
				if(move.getStartPosition().getRow()==position[0].getRow() && move.getStartPosition().getColumn()==position[0].getColumn()){
					legalmoves.add((ChessPositionI)move.getEndPosition());
				}
			}
			//System.out.println("lergalMoves.size() = "+legalmoves.size());
		}
		if(teamColor.toString()== "BLACK"){
			sb.append(EscapeSequences.SET_BG_COLOR_DARK_GREY + "    h   g   f  e   d  c   b   a " + EscapeSequences.RESET_BG_COLOR + "\n");
			for(int i = 0; i < 8; i++){

				sb.append(EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.SET_BG_COLOR_DARK_GREY + " "+(i+1) + " " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
				for(int j = 7; j >= 0; j--){

					ChessPieceI piece = board[i][j];
					//ChessPositionI p = position.length!=0 ? (ChessPositionI)position[0] : null;
					//|| p!=null && p.getRow()!=i+1 && p.getColumn()!=j+1
					if(position.length == 0 || !legalmoves.contains(new ChessPositionI(i+1,j+1)) ){
						if(i % 2 == j % 2){
							sb.append(EscapeSequences.SET_BG_COLOR_BLACK);
						}else{
							sb.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
						}
					}else{
						if(i % 2 == j % 2){
							sb.append(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
						}else{
							sb.append(EscapeSequences.SET_BG_COLOR_GREEN);
						}
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
		}else{

			sb.append(EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.SET_BG_COLOR_DARK_GREY + "    a   b   c  d   e  f   g   h " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n");
			for(int i = 7; i >= 0; i--){

				sb.append(EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.SET_BG_COLOR_DARK_GREY + " "+(i+1) + " " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
				for(int j = 0; j < 8; j++){
					ChessPieceI piece = board[i][j];
					if(position.length == 0 || !legalmoves.contains(new ChessPositionI(i+1,j+1)) ){
						if(i % 2 == j % 2){
							sb.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
						}else{
							sb.append(EscapeSequences.SET_BG_COLOR_BLACK);
						}
					}else{
						if(i % 2 == j % 2){
							sb.append(EscapeSequences.SET_BG_COLOR_GREEN);
						}else{
							sb.append(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
						}
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
		}
		System.out.println(sb);
	}

	public void socketConnection(){
		try{
			if(ws==null){
				ws = new WebSocketClient(auth, currentGameID);
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

}
