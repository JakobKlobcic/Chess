package chess.Pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Knight extends ChessPieceI {
    public Knight(ChessGame.TeamColor pieceColor, PieceType type) {
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
        //System.out.println("Knight position: "+myPosition.toString());
        List<ChessMove> moves = new ArrayList<>();
        int r = myPosition.getRow()-1;
        int c = myPosition.getColumn()-1;

        // Top-right move
        if(r - 2 >= 0 && c + 1 < 8){
            ChessPositionI newPosition = new ChessPositionI(r - 2 + 1, c + 1 + 1);
            if (!(board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() == getTeamColor())){
                moves.add(new ChessMoveI((ChessPositionI) myPosition, newPosition));
            }
        }

        // Top-left move
        if(r - 2 >= 0 && c - 1 >= 0){
            ChessPositionI newPosition = new ChessPositionI(r - 2 + 1, c - 1 + 1);
            if (!(board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() == getTeamColor())){
                moves.add(new ChessMoveI((ChessPositionI) myPosition, newPosition));
            }
        }

        // Bottom-right move
        if(r + 2 < 8 && c + 1 < 8){
            ChessPositionI newPosition = new ChessPositionI(r + 2 + 1, c + 1 + 1);
            if (!(board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() == getTeamColor())){
                moves.add(new ChessMoveI((ChessPositionI) myPosition, newPosition));
            }
        }

        // Bottom-left move
        if(r + 2 < 8 && c - 1 >= 0){
            ChessPositionI newPosition = new ChessPositionI(r + 2 + 1, c - 1 + 1);
            if (!(board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() == getTeamColor())){
                moves.add(new ChessMoveI((ChessPositionI) myPosition, newPosition));
            }
        }

        // Right-top move
        if(r - 1 >= 0 && c + 2 < 8){
            ChessPositionI newPosition = new ChessPositionI(r - 1 + 1, c + 2 + 1);
            if (!(board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() == getTeamColor())){
                moves.add(new ChessMoveI((ChessPositionI) myPosition, newPosition));
            }
        }

        // Right-bottom move
        if(r + 1 < 8 && c + 2 < 8){
            ChessPositionI newPosition = new ChessPositionI(r + 1 + 1, c + 2 + 1);
            if (!(board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() == getTeamColor())){
                moves.add(new ChessMoveI((ChessPositionI) myPosition, newPosition));
            }
        }

        // Left-top move
        if(r - 1 >= 0 && c - 2 >= 0){
            ChessPositionI newPosition = new ChessPositionI(r - 1 + 1, c - 2 + 1);
            if (!(board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() == getTeamColor())){
                moves.add(new ChessMoveI((ChessPositionI) myPosition, newPosition));
            }
        }

        // Left-bottom move
        if(r + 1 < 8 && c - 2 >= 0){
            ChessPositionI newPosition = new ChessPositionI(r + 1 + 1, c - 2 + 1);
            if (!(board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() == getTeamColor())){
                moves.add(new ChessMoveI((ChessPositionI) myPosition, newPosition));
            }
        }
        return moves;
    }
}
