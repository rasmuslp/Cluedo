package cluedo.common.board;

/**
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class Position {

	private final int row;
	private final int column;

	public Position( int row, int column ) {
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return this.row;
	}

	public int getColumn() {
		return this.column;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.column;
		result = prime * result + this.row;
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj ) {
			return true;
		}

		if ( obj == null ) {
			return false;
		}

		if ( !( obj instanceof Position ) ) {
			return false;
		}

		Position other = (Position) obj;

		if ( this.column != other.column ) {
			return false;
		}

		if ( this.row != other.row ) {
			return false;
		}

		return true;
	}

}
