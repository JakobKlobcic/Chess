package chess;

import chess.Pieces.*;

import java.util.ArrayList;
import java.util.Collection;

public class ChessPieceI implements ChessPiece{
	ChessGame.TeamColor team;
	ChessPiece.PieceType type;

	public ChessPieceI(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type){
		this.team = pieceColor;
		this.type = type;
	}

	@Override
	public ChessGame.TeamColor getTeamColor(){
		return team;
	}

	@Override
	public PieceType getPieceType(){
		return type;
	}

	@Override
	public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
		ChessPieceI piece;
		if(type == PieceType.ROOK){
			piece = new Rook(team, type);
			return piece.pieceMoves(board, myPosition);
		}
		if(type == PieceType.KNIGHT){
			piece = new Knight(team, type);
			return piece.pieceMoves(board, myPosition);
		}
		if(type == PieceType.BISHOP){
			piece = new Bishop(team, type);
			return piece.pieceMoves(board, myPosition);
		}
		if(type == PieceType.QUEEN){
			piece = new Queen(team, type);
			return piece.pieceMoves(board, myPosition);
		}
		if(type == PieceType.KING){
			piece = new King(team, type);
			return piece.pieceMoves(board, myPosition);
		}
		if(type == PieceType.PAWN){
			piece = new Pawn(team, type);
			return piece.pieceMoves(board, myPosition);
		}
		return new ArrayList<>();
	}

	@Override
	public boolean equals(Object obj){
		if(obj == null){
			return false;
		}
		ChessPieceI piece = (ChessPieceI)obj;
		return piece.type == this.type && piece.team == this.team;
	}

	public String toString(){
		return "Piece: " + type.name() + "\nTeam: " + getTeamColor() + "\n";
	}
}
