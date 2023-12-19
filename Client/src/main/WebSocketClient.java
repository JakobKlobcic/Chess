import chess.*;
import client_socket_message.MakeMove;
import com.google.gson.Gson;
import server_socket_message.Error;
import server_socket_message.LoadGame;
import server_socket_message.Notification;
import ui.EscapeSequences;
import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class WebSocketClient extends Endpoint{
	private Session session;
	private main obj;

	Integer currentGameID = null;
	ChessBoardI currentBoard=null;
	ChessGame.TeamColor currentColor=null;
	String auth;
	public WebSocketClient(String authtoken, Integer gameID) throws Exception {
		URI uri = new URI("ws://localhost:8080/connect?authtoken="+authtoken);
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		this.session = container.connectToServer(this, uri);
		this.session.setMaxIdleTimeout(600000);
		this.auth=authtoken;
		this.obj=obj;
		this.session.addMessageHandler(new MessageHandler.Whole<String>() {
			public void onMessage(String message) {
				Gson gson = new Gson();
				ServerMessage sm = gson.fromJson(message, ServerMessage.class);
				switch (sm.getServerMessageType()) {
					case LOAD_GAME:
						//System.out.println(message);
						LoadGame loadGame = gson.fromJson(message, LoadGame.class);
						currentColor=loadGame.getColor();
						currentBoard=loadGame.getGame().getBoardI();
						currentGameID=loadGame.getGameID();
						drawBoard(loadGame.getGame().getBoardI(), loadGame.getColor() );
						//requestInput();
						break;

					case NOTIFICATION:
						Notification notification = gson.fromJson(message, Notification.class);
						System.out.println("\033[1A"+notification.getMessage()+"\n");
						//post notification
						//requestInput();
						break;

					case ERROR:
						Error error = gson.fromJson(message, Error.class);
						System.out.println(error.getErrorMessage());
						//requestInput();
						break;
				}
			}
		});
	}
/*
	public void requestInput(){

		System.out.print("[IN_GAME] >>> ");

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

	}

	public void wrongInput(String message){
		System.out.println(message);
		this.requestInput();
	}

	public void move(String starting, String ending){
		if(starting.length()==2 && ending.length()==2){
			if(Character.isDigit(starting.charAt(0)) && Character.isLetter(starting.charAt(1)) && Character.isDigit(ending.charAt(0)) && Character.isLetter(ending.charAt(1))){
				ChessPositionI s = new ChessPositionI(Character.getNumericValue(starting.charAt(0)), starting.charAt(1)-'a'+1);
				ChessPositionI e = new ChessPositionI(Character.getNumericValue(ending.charAt(0)), ending.charAt(1)-'a'+1);
				System.out.println("Starting: "+ s + "; Ending:"+e);
				Gson gson = new Gson();
				try{
					send(gson.toJson(new MakeMove(auth, currentGameID, new ChessMoveI(s, e))));
				}catch(Exception ex){
					throw new RuntimeException(ex);
				}
				//drawBoard(currentBoard,currentColor, position);
				requestInput();
			}
		}else{
			//problem
		}
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
		requestInput();
	}

	public void resign(){
		requestInput();
	}

	public void help(){
		System.out.print(
			"move <piece_coordinate> <move_coordinate> - move piece\n"+
					"legal <piece_coordinate> - shows possible moves for piece\n"+
					"resign - resign game\n"+
					"leave - leave game\n"+
					"quit - playing chess\n"+
					"help - with possible commands\n"
		);

		this.requestInput();
	}

	public void quit(){}
*/
	public void drawBoard(ChessBoardI chessBoard, ChessGame.TeamColor teamColor, ChessPosition... position){
		if( chessBoard == null){
			System.out.println("There was a problem finding the board");
			return;
		}

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


	public void send(String msg) throws Exception {this.session.getBasicRemote().sendText(msg);}

	@OnOpen
	public void onOpen(Session session, EndpointConfig endpointConfig) {

	}
}
