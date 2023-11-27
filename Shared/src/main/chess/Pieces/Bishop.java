package chess.Pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Bishop extends ChessPieceI{
	public Bishop(ChessGame.TeamColor pieceColor, PieceType type){
		super(pieceColor, type);
	}

	@Override
	public ChessGame.TeamColor getTeamColor(){
		return super.getTeamColor();
	}

	@Override
	public PieceType getPieceType(){
		return super.getPieceType();
	}

	@Override
	public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
		//System.out.println("Bishop position: "+myPosition.toString());
		List<ChessMove> moves = new ArrayList<>();
		int r = myPosition.getRow() - 1;
		int c = myPosition.getColumn() - 1;

		//System.out.println("Moving to upper right:");
		for(int i = 1; i < 8; i++){
			if(r + i >= 8 || c + i >= 8){
				break;
			}
			ChessPositionI position = new ChessPositionI(r + i + 1, c + i + 1);
			if(board.getPiece(position) != null && board.getPiece(position).getTeamColor() != getTeamColor()){
				moves.add(new ChessMoveI((ChessPositionI)myPosition, position));
				break;
			}
			if(board.getPiece(position) != null){
				break;
			}
			//System.out.println(position.toString());
			moves.add(new ChessMoveI((ChessPositionI)myPosition, position));
		}

		//System.out.println("Moving to upper left:");
		for(int i = 1; i < 8; i++){
			if(r + i >= 8 || c - i < 0){
				break;
			}
			ChessPositionI position = new ChessPositionI(r + i + 1, c - i + 1);
			if(board.getPiece(position) != null && board.getPiece(position).getTeamColor() != getTeamColor()){
				moves.add(new ChessMoveI((ChessPositionI)myPosition, position));
				break;
			}
			if(board.getPiece(position) != null){
				break;
			}
			//System.out.println(position.toString());
			moves.add(new ChessMoveI((ChessPositionI)myPosition, position));
		}

		//System.out.println("Moving to lower right:");
		for(int i = 1; i < 8; i++){
			if(r - i < 0 || c + i >= 8){
				break;
			}
			ChessPositionI position = new ChessPositionI(r - i + 1, c + i + 1);
			if(board.getPiece(position) != null && board.getPiece(position).getTeamColor() != getTeamColor()){
				//System.out.println(board.getPiece(position).toString());
				moves.add(new ChessMoveI((ChessPositionI)myPosition, position));
				break;
			}
			if(board.getPiece(position) != null){
				break;
			}
			//System.out.println(position.toString());
			moves.add(new ChessMoveI((ChessPositionI)myPosition, position));
		}

		//System.out.println("Moving to lower left:");
		for(int i = 1; i < 8; i++){
			if(r - i < 0 || c - i < 0){
				break;
			}
			ChessPositionI position = new ChessPositionI(r - i + 1, c - i + 1);
			if(board.getPiece(position) != null && board.getPiece(position).getTeamColor() != getTeamColor()){
				moves.add(new ChessMoveI((ChessPositionI)myPosition, position));
				break;
			}
			if(board.getPiece(position) != null){
				break;
			}
			//System.out.println(position.toString());
			moves.add(new ChessMoveI((ChessPositionI)myPosition, position));
		}
		//System.out.println("moves return size: "+moves.size());
		return moves;
	}
}
