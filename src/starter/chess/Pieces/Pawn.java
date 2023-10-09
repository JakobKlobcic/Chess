package chess.Pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Pawn extends ChessPieceI {
    public Pawn(ChessGame.TeamColor pieceColor, PieceType type) {
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
        //System.out.println("Pawn position: " + myPosition.toString());
        List<ChessMove> moves = new ArrayList<>();

        int r = myPosition.getRow();
        int c = myPosition.getColumn();

        // Advance forward if position is unblocked.
        if(getTeamColor().toString().equals("WHITE")) {
            if(r==2){
                ChessPositionI forward1 = new ChessPositionI(r+1, c);
                ChessPositionI forward2 = new ChessPositionI(r+2, c);
                if(board.getPiece(forward1) == null){
                    moves.add(new ChessMoveI((ChessPositionI) myPosition, forward1));
                    if(board.getPiece(forward2) == null){
                        moves.add(new ChessMoveI((ChessPositionI) myPosition, forward2));
                    }
                }

            }else if(r==8){

            }else{
                ChessPositionI forward1 = new ChessPositionI(r+1, c);
                if(board.getPiece(forward1) == null){
                    moves.add(new ChessMoveI((ChessPositionI) myPosition, forward1));
                }
            }

            ChessPositionI forwardLeft = new ChessPositionI(r+1, c-1);
            ChessPositionI forwardRight = new ChessPositionI(r+1, c+1);
            if(board.getPiece(forwardLeft) != null && board.getPiece(forwardLeft).getTeamColor() != getTeamColor()){
                moves.add(new ChessMoveI((ChessPositionI) myPosition, forwardLeft));
            }
            if(board.getPiece(forwardRight) != null && board.getPiece(forwardRight).getTeamColor() != getTeamColor()){
                moves.add(new ChessMoveI((ChessPositionI) myPosition, forwardRight));
            }



        }else{
            if(r==7){
                ChessPositionI forward1 = new ChessPositionI(r-1, c);
                ChessPositionI forward2 = new ChessPositionI(r-2, c);
                if(board.getPiece(forward1) == null){
                    moves.add(new ChessMoveI((ChessPositionI) myPosition, forward1));
                    if(board.getPiece(forward2) == null){
                        moves.add(new ChessMoveI((ChessPositionI) myPosition, forward2));
                    }
                }


            }else if(r==1){

            }else{
                ChessPositionI forward1 = new ChessPositionI(r-1, c);
                if(board.getPiece(forward1) == null){
                    moves.add(new ChessMoveI((ChessPositionI) myPosition, forward1));
                }
            }

            ChessPositionI forwardLeft = new ChessPositionI(r-1, c-1);
            ChessPositionI forwardRight = new ChessPositionI(r-1, c+1);
            if(board.getPiece(forwardLeft) != null && board.getPiece(forwardLeft).getTeamColor() != getTeamColor()){
                moves.add(new ChessMoveI((ChessPositionI) myPosition, forwardLeft));
            }
            if(board.getPiece(forwardRight) != null && board.getPiece(forwardRight).getTeamColor() != getTeamColor()){
                moves.add(new ChessMoveI((ChessPositionI) myPosition, forwardRight));
            }
        }

        //System.out.println("moves return size: " + moves.size());
        return moves;
    }
}
