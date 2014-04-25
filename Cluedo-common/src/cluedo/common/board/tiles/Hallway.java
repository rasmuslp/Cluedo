package cluedo.common.board.tiles;

/**
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class Hallway extends Tile {

	private final boolean isPreEntrance;

	public Hallway() {
		this( false );
	}

	public Hallway( final boolean isPreEntrance ) {
		this.isPreEntrance = isPreEntrance;

	}

	public boolean isPreEntrance() {
		return this.isPreEntrance;
	}

	@Override
	public void print() {
		if ( this.isPreEntrance ) {
			System.out.print( "e" );
		} else {
			System.out.print( "*" );
		}

	}

}
