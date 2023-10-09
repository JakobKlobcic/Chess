import chess.*;
import chess.Pieces.Rook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class main {
    public static void main(String [] args){
        ChessGameImplementation game = new ChessGameImplementation();
        ChessBoardI board = (ChessBoardI)game.getBoard();
        Rook rook = (Rook) board.getPiece(new ChessPositionI(1,1));
        List<ChessMove> moves = new ArrayList<ChessMove>(rook.pieceMoves(board, new ChessPositionI(1,1)));
        for (int i = 0; i < moves.size(); i++) {
            System.out.println(moves.get(i).toString());
        }

        System.out.println(board.toString());
    }
}
