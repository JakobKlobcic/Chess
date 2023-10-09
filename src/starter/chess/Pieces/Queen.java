package chess.Pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Queen extends ChessPieceI {
    public Queen(ChessGame.TeamColor pieceColor, PieceType type) {
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
        //System.out.println("Bishop position: "+myPosition.toString());
        List<ChessMove> moves = new ArrayList<>();
        int r = myPosition.getRow()-1;
        int c = myPosition.getColumn()-1;

        //System.out.println("Moving to upper right:");
        for(int i = 1; i < 8; i++){
            if(r+i >= 8 || c+i >= 8) break;
            ChessPositionI position = new ChessPositionI(r+i+1, c+i+1);
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != getTeamColor()){
                moves.add(new ChessMoveI((ChessPositionI) myPosition, position));
                break;
            }
            if(board.getPiece(position) != null){
                break;
            }
            //System.out.println(position.toString());
            moves.add(new ChessMoveI((ChessPositionI) myPosition, position));
        }

        //System.out.println("Moving to upper left:");
        for(int i = 1; i < 8; i++){
            if(r+i >= 8 || c-i < 0) break;
            ChessPositionI position = new ChessPositionI(r+i+1, c-i+1);
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != getTeamColor()){
                moves.add(new ChessMoveI((ChessPositionI) myPosition, position));
                break;
            }
            if(board.getPiece(position) != null){
                break;
            }
            //System.out.println(position.toString());
            moves.add(new ChessMoveI((ChessPositionI) myPosition, position));
        }

        //System.out.println("Moving to lower right:");
        for(int i = 1; i < 8; i++){
            if(r-i < 0 || c+i >= 8) break;
            ChessPositionI position = new ChessPositionI(r-i+1, c+i+1);
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != getTeamColor()){
                //System.out.println(board.getPiece(position).toString());
                moves.add(new ChessMoveI((ChessPositionI) myPosition, position));
                break;
            }
            if(board.getPiece(position) != null){
                break;
            }
            //System.out.println(position.toString());
            moves.add(new ChessMoveI((ChessPositionI) myPosition, position));
        }

        //System.out.println("Moving to lower left:");
        for(int i = 1; i < 8; i++){
            if(r-i < 0 || c-i < 0) break;
            ChessPositionI position = new ChessPositionI(r-i+1, c-i+1);
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != getTeamColor()){
                moves.add(new ChessMoveI((ChessPositionI) myPosition, position));
                break;
            }
            if(board.getPiece(position) != null){
                break;
            }
            //System.out.println(position.toString());
            moves.add(new ChessMoveI((ChessPositionI) myPosition, position));
        }
        // Move along the row to the right
        //System.out.println("Moving right:");
        for(int i = c + 1; i < 8 ; i++){
            ChessPositionI position = new ChessPositionI(r+1, i+1);
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != super.getTeamColor()){
                moves.add(new ChessMoveI((ChessPositionI) myPosition, position));
                break;
            }
            if(board.getPiece(position) != null){
                break;
            }
            //System.out.println(position.toString());
            moves.add(new ChessMoveI((ChessPositionI) myPosition, position));
        }

        //System.out.println("Moving left:");
        // Move along the row to the left
        for(int i = c - 1; i >= 0; i--){
            ChessPositionI position = new ChessPositionI(r+1, i+1);
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != getTeamColor()){
                moves.add(new ChessMoveI((ChessPositionI) myPosition, position));
                break;
            }
            if(board.getPiece(position) != null){
                break;
            }
            //System.out.println(position.toString());
            moves.add(new ChessMoveI((ChessPositionI) myPosition, position));
        }

        //System.out.println("Moving up:");
        // Move along the column up
        for(int i = r - 1; i >= 0; i--){
            ChessPositionI position = new ChessPositionI(i+1, c+1);
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != getTeamColor()){
                //System.out.println(board.getPiece(position).toString());
                moves.add(new ChessMoveI((ChessPositionI) myPosition, position));
                break;
            }
            if(board.getPiece(position) != null){
                break;
            }
            //System.out.println(position.toString());
            moves.add(new ChessMoveI((ChessPositionI) myPosition, position));
        }

        //System.out.println("Moving down:");
        // Move along the column down
        for(int i = r + 1; i < 8 && i > 0; i++){
            ChessPositionI position = new ChessPositionI(i+1, c+1);
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != getTeamColor()){
                moves.add(new ChessMoveI((ChessPositionI) myPosition, position));
                break;
            }
            if(board.getPiece(position) != null){
                break;
            }
            //System.out.println(position.toString());
            moves.add(new ChessMoveI((ChessPositionI) myPosition, position));
        }
        //System.out.println("moves return size: "+moves.size());
        return moves;
    }
}
