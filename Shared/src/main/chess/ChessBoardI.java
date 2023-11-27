package chess;

import chess.Pieces.*;

import java.util.ArrayList;
import java.util.List;

public class ChessBoardI implements ChessBoard{
	ChessPieceI[][] board = new ChessPieceI[8][8];

	public ChessBoardI(){
		//this.resetBoard();
	}

	@Override
	public void addPiece(ChessPosition position, ChessPiece piece){
		if(piece==null){
			board[position.getRow() - 1][position.getColumn() - 1] = null;
		}else{
			board[position.getRow() - 1][position.getColumn() - 1] = (ChessPieceI)piece;
		}
	}

	@Override
	public ChessPiece getPiece(ChessPosition position){
		ChessPieceI result;
		try{
			result = board[position.getRow() - 1][position.getColumn() - 1];
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
		return result;
	}

	public void makeMove(ChessMoveI move){
		board[move.ending.row - 1][move.ending.column - 1] = board[move.starting.row - 1][move.starting.column - 1];
		board[move.starting.row - 1][move.starting.column - 1] = null;
	}

	public List<ChessMove> getTeamMoves(ChessGame.TeamColor color){
		List<ChessMove> result = new ArrayList<>();
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(board[i][j] != null && board[i][j].team == color){
					result.addAll(board[i][j].pieceMoves(this, new ChessPositionI(i + 1, j + 1)));
				}
			}
		}
		return result;
	}

	public ChessPosition getTeamKing(ChessGame.TeamColor color){
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(board[i][j] != null && board[i][j].getPieceType().toString() == "KING" && board[i][j].team == color){
					System.out.println(i);
					System.out.println(j);
					return new ChessPositionI(i + 1, j + 1);
				}
			}
		}
		return null;
	}

	public void setBoard(ChessPieceI[][] board){
		this.board = board;
	}

	public ChessPieceI[][] getBoard(){
		return board;
	}

	public ChessPieceI[][] getBoardCopy(){
		ChessPieceI[][] result = new ChessPieceI[8][8];
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				result[j][i] = board[j][i];
			}
		}
		return result;
	}

	@Override
	public void resetBoard(){
		System.out.println("reseting board");
		board[0][0] = new Rook(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
		board[0][1] = new Knight(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
		board[0][2] = new Bishop(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
		board[0][3] = new Queen(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
		board[0][4] = new King(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
		board[0][5] = new Bishop(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
		board[0][6] = new Knight(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
		board[0][7] = new Rook(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
		for(int i = 0; i < 8; i++){// for(int i = 0; i < 8; i++)
			board[1][i] = new Pawn(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
		}

		//black pieces
		board[7][0] = new Rook(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
		board[7][1] = new Knight(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
		board[7][2] = new Bishop(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
		board[7][3] = new Queen(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
		board[7][4] = new King(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
		board[7][5] = new Bishop(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
		board[7][6] = new Knight(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
		board[7][7] = new Rook(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
		for(int i = 0; i < 8; i++){
			board[6][i] = new Pawn(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
		}

		for(int i = 2; i < 6; i++){
			for(int j = 0; j < 8; j++){
				board[i][j] = null;
			}
		}
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();

		sb.append("  1  2  3  4  5  6  7  8\n");
		for(int i = 0; i < board.length; i++){
			sb.append(i + 1);
			sb.append(" ");
			for(int j = 0; j < board[i].length; j++){
				ChessPieceI piece = board[i][j];

				if(piece == null){
					sb.append("..");
				}else{
					String color;
					if(piece.getTeamColor().toString() == "WHITE"){
						color = "w";
					}else{
						color = "b";
					}
					String type;
					if(piece.getPieceType().toString() == "KING"){
						type = "K";
					}else{
						type = piece.getPieceType().toString().substring(0, 1).toLowerCase();
					}


					sb.append(color + type);
				}
				sb.append(" ");
			}
			sb.append("\n");
		}

		return sb.toString();
	}
}
