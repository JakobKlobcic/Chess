package chess;

public class ChessPositionI implements ChessPosition{
	int row;
	int column;

	public ChessPositionI(int row, int column){
		this.row = row;
		this.column = column;
	}

	@Override
	public int getRow(){
		return row;
	}

	@Override
	public int getColumn(){
		return column;
	}

	public String toString(){
		return "(" + row + "," + column + ")";
	}

	@Override
	public int hashCode(){
		return row * column;
	}

	@Override
	public boolean equals(Object obj){
		if(obj == null){
			return false;
		}

		if(this == obj){
			return true;
		}

		ChessPositionI position = (ChessPositionI)obj;
		return position.row == this.row && position.column == this.column;
	}
}
