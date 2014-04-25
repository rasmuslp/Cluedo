package cluedo.common.board.tiles;

/**
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class Wall extends Tile {

	@Override
	public void print() {
		System.out.print( "#" );
	}

}
