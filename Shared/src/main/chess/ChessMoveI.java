package chess;

public class ChessMoveI implements ChessMove{
	ChessPositionI starting;
	ChessPositionI ending;

	ChessPiece.PieceType promotionPiece;


	public ChessMoveI(ChessPositionI starting, ChessPositionI ending, ChessPiece.PieceType promotionPiece){
		this.starting = starting;
		this.ending = ending;
		this.promotionPiece = promotionPiece;
	}

	public ChessMoveI(ChessPositionI starting, ChessPositionI ending){
		this.starting = starting;
		this.ending = ending;
	}

	@Override
	public ChessPosition getStartPosition(){
		return starting;
	}

	@Override
	public ChessPosition getEndPosition(){
		return ending;
	}

	@Override
	public ChessPiece.PieceType getPromotionPiece(){
		return promotionPiece;
	}

	@Override
	public boolean equals(Object obj){

		if(obj == null){
			return false;
		}
		ChessMoveI move = (ChessMoveI)obj;
		return move.starting.equals(this.starting) && move.ending.equals(this.ending);
	}

	@Override
	public int hashCode(){
		return starting.hashCode() * ending.hashCode();
	}

	public String toString(){
		return "starting: " + starting.toString() + "; ending: " + ending.toString() + "\n";
	}
}
