package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChessGameImplementation implements ChessGame{
	ChessBoardI board;
	TeamColor turn;

	List<ChessMove> whiteMoves;

	List<ChessMove> blackMoves;

	public ChessGameImplementation(){
		board = new ChessBoardI();
		turn = TeamColor.WHITE;
		whiteMoves = board.getTeamMoves(TeamColor.WHITE);
		blackMoves = board.getTeamMoves(TeamColor.BLACK);
	}

	@Override
	public TeamColor getTeamTurn(){
		return turn;
	}

	@Override
	public void setTeamTurn(TeamColor team){
		//System.out.println("Set turn "+team.toString());
		turn = team;
	}

	@Override
	public Collection<ChessMove> validMoves(ChessPosition startPosition){
		ChessBoardI tempboard = new ChessBoardI();
		tempboard.setBoard(board.getBoardCopy());
		TeamColor temp = turn;
		turn = board.getPiece(startPosition).getTeamColor();
		List<ChessMove> moves = (List<ChessMove>)board.getPiece(startPosition).pieceMoves(board, startPosition);
		for(int i = 0; i < moves.size(); i++){
			try{

				makeMove(moves.get(i));

				board.setBoard(tempboard.getBoardCopy());
				whiteMoves.clear();
				blackMoves.clear();
				whiteMoves = board.getTeamMoves(TeamColor.WHITE);
				blackMoves = board.getTeamMoves(TeamColor.BLACK);
				if(turn == TeamColor.WHITE){
					setTeamTurn(TeamColor.BLACK);
				}else{
					setTeamTurn(TeamColor.WHITE);
				}
			}catch(InvalidMoveException e){
				moves.remove(i);
				i--;
			}
		}
		turn = temp;
		//Takes as input a position on the chessboard and returns all moves the piece there can legally make. If there
        // is no piece at that location, this method returns null.
		return moves;
	}

	boolean isValidMove(ChessMove move){
		ChessBoardI tempboard = new ChessBoardI();
		tempboard.setBoard(board.getBoardCopy());

        if(board.getPiece(move.getStartPosition()) == null){
            return false;
        }

		TeamColor movedPieceColor = board.getPiece(move.getStartPosition()).getTeamColor();

        if(movedPieceColor != turn){
            return false;
        }

		if(movedPieceColor == TeamColor.WHITE && !whiteMoves.contains(move)){
			return false;
		}
		if(movedPieceColor == TeamColor.BLACK && !blackMoves.contains(move)){
			return false;
		}

		board.makeMove((ChessMoveI)move);
		recalculateMoves();

		if(movedPieceColor == TeamColor.WHITE && isInCheck(TeamColor.WHITE) || movedPieceColor == TeamColor.BLACK && isInCheck(TeamColor.BLACK)){
			board.makeMove(new ChessMoveI(((ChessMoveI)move).ending, ((ChessMoveI)move).starting));
			recalculateMoves();
			board.setBoard(tempboard.getBoardCopy());
			return false;
		}
		board.setBoard(tempboard.getBoardCopy());


		return true;
	}

	@Override
	public void makeMove(ChessMove move) throws InvalidMoveException{
		if(isValidMove(move)){
			ChessPiece piece = board.getPiece(move.getStartPosition());
			//if(piece.getTeamColor()==TeamColor.WHITE && move.getEndPosition().getRow()==8 || piece.getTeamColor()
            // ==TeamColor.BLACK && move.getEndPosition().getRow()==1){
			//
			//}
			board.makeMove((ChessMoveI)move);
			if(move.getPromotionPiece() != null){
				board.addPiece(((ChessMoveI)move).ending, new ChessPieceI(turn, move.getPromotionPiece()));
			}
			recalculateMoves();
		}else{
			throw new InvalidMoveException();
		}
		if(turn == TeamColor.WHITE){
			setTeamTurn(TeamColor.BLACK);
		}else{
			setTeamTurn(TeamColor.WHITE);
		}
	}

	public void recalculateMoves(){
		whiteMoves.clear();
		blackMoves.clear();
		whiteMoves = board.getTeamMoves(TeamColor.WHITE);
		blackMoves = board.getTeamMoves(TeamColor.BLACK);
	}

	@Override
	public boolean isInCheck(TeamColor teamColor){
		ChessPositionI kingPosition = (ChessPositionI)board.getTeamKing(teamColor);
		TeamColor oppositeTeam = teamColor == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
		List<ChessMove> moves = board.getTeamMoves(oppositeTeam);
		for(ChessMove move : moves){
			ChessPieceI piece = (ChessPieceI)board.getPiece(move.getStartPosition());
			if(!(piece.type == ChessPiece.PieceType.PAWN && move.getStartPosition().getColumn() == move.getEndPosition().getColumn()) && move.getEndPosition().equals(kingPosition)){
				return true;
			}
		}
		//System.out.println(teamColor);
		//System.out.println(kingPosition);
		//if(board.getTeamMoves(teamColor))
		//if(teamColor == TeamColor.WHITE){
		//    for (int i = 0; i <blackMoves.size() ; i++) {
		//        ChessMove move = blackMoves.get(i);
		//        ChessPieceI piece = (ChessPieceI) board.getPiece(move.getStartPosition());
		//        if(!(piece.type == ChessPiece.PieceType.PAWN && move.getStartPosition().getColumn() == move
        //        .getEndPosition().getColumn()) && move.getEndPosition().equals(kingPosition)){
		//            return true;
		//        }
		//    }
		//}else{
		//    //System.out.println(whiteMoves);
		//    for (int i = 0; i < whiteMoves.size() ; i++) {
		//        ChessMove move = whiteMoves.get(i);
		//        ChessPieceI piece = (ChessPieceI) board.getPiece(move.getStartPosition());
		//        if(!(piece.type == ChessPiece.PieceType.PAWN && move.getStartPosition().getColumn() == move
        //        .getEndPosition().getColumn()) && move.getEndPosition().equals(kingPosition)){
		//            return true;
		//        }
		//    }
		//}
		return false;
	}

	public List<ChessMove> getTeamValidMoves(TeamColor teamColor){
		List<ChessMove> moves = new ArrayList<>();
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				ChessPositionI position = new ChessPositionI(i + 1, j + 1);
				if(board.getPiece(position) != null && board.getPiece(position).getTeamColor() == teamColor){
					moves.addAll(validMoves(position));
				}
			}
		}
		return moves;
	}

	@Override
	public boolean isInCheckmate(TeamColor teamColor){
		//TeamColor oppositeTeam = teamColor == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
		System.out.println(board);
		System.out.println(whiteMoves);
		//System.out.println(getTeamValidMoves(teamColor));
		//System.out.println(isInCheck(teamColor));
		//Returns true if the given team has no way to protect their king from being captured.
		return getTeamValidMoves(teamColor).isEmpty() && isInCheck(teamColor);
	}

	@Override
	public boolean isInStalemate(TeamColor teamColor){
		return validMoves(board.getTeamKing(teamColor)).isEmpty() && !isInCheck(teamColor);
	}

	@Override
	public void setBoard(ChessBoard board){
		this.board.setBoard(((ChessBoardI)board).getBoard());
		whiteMoves = this.board.getTeamMoves(TeamColor.WHITE);
		blackMoves = this.board.getTeamMoves(TeamColor.BLACK);
	}

	@Override
	public ChessBoard getBoard(){
		return board;
	}
}
