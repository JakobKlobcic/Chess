package chess.Pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class King extends ChessPieceI {
    ChessPositionI position;
    public King(ChessGame.TeamColor pieceColor, PieceType type) {
        super(pieceColor, type);
    }

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return super.getTeamColor();
    }

    @Override
    public PieceType getPieceType() {
        return super.getPieceType();
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> moves = new ArrayList<>();
        int r = myPosition.getRow();
        int c = myPosition.getColumn();

        if(r+1 <= 8) {
            ChessPositionI position = new ChessPositionI(r+1, c);
            if(board.getPiece(position) == null || board.getPiece(position) != null && board.getPiece(position).getTeamColor() != getTeamColor()){
                moves.add(new ChessMoveI((ChessPositionI) myPosition, position));
            }
        }

        if(c+1 <= 8) {
            ChessPositionI position = new ChessPositionI(r, c+1);
            if(board.getPiece(position) == null || board.getPiece(position) != null && board.getPiece(position).getTeamColor() != getTeamColor()){
                moves.add(new ChessMoveI((ChessPositionI) myPosition, position));
            }
        }

        if(r+1 <= 8 && c+1 <= 8) {
            ChessPositionI position = new ChessPositionI(r+1, c+1);
            if(board.getPiece(position) == null || board.getPiece(position) != null && board.getPiece(position).getTeamColor() != getTeamColor()){
                moves.add(new ChessMoveI((ChessPositionI) myPosition, position));
            }
        }

        if(r-1 >= 1 && c-1 >= 1) {
            ChessPositionI position = new ChessPositionI(r-1, c-1);
            if(board.getPiece(position) == null || board.getPiece(position) != null && board.getPiece(position).getTeamColor() != getTeamColor()){
                moves.add(new ChessMoveI((ChessPositionI) myPosition, position));
            }
        }

        if(c-1 >= 1) {
            ChessPositionI position = new ChessPositionI(r, c-1);
            if(board.getPiece(position) == null || board.getPiece(position) != null && board.getPiece(position).getTeamColor() != getTeamColor()){
                moves.add(new ChessMoveI((ChessPositionI) myPosition, position));
            }
        }

        if(r-1 >= 1) {
            ChessPositionI position = new ChessPositionI(r-1, c);
            if(board.getPiece(position) == null || board.getPiece(position) != null && board.getPiece(position).getTeamColor() != getTeamColor()){
                moves.add(new ChessMoveI((ChessPositionI) myPosition, position));
            }
        }

        if(r-1 >= 1 && c+1 <= 8) {
            ChessPositionI position = new ChessPositionI(r-1, c+1);
            if(board.getPiece(position) == null || board.getPiece(position) != null && board.getPiece(position).getTeamColor() != getTeamColor()){
                moves.add(new ChessMoveI((ChessPositionI) myPosition, position));
            }
        }

        if(r+1 <= 8 && c-1 >= 1) {
            ChessPositionI position = new ChessPositionI(r+1, c-1);
            if(board.getPiece(position) == null || board.getPiece(position) != null && board.getPiece(position).getTeamColor() != getTeamColor()){
                moves.add(new ChessMoveI((ChessPositionI) myPosition, position));
            }
        }
        return moves;
    }
}
