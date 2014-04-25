package cluedo.common.board.tiles;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a special case of a {@link Tile}.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class Room extends Tile {

	// Other rooms connected to this via secret passages
	private final List< Room > secretPassages = new ArrayList<>();

	public void addSecretPassageTo( Room room ) {
		if ( !this.secretPassages.contains( room ) ) {
			this.secretPassages.add( room );
		}
	}

	@Override
	public void print() {
		System.out.print( "R" );
	}

}
