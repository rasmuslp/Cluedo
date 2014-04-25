package cluedo.common.board.tiles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public abstract class Tile {

	protected final List< Tile > accessibleNeighbours = new ArrayList<>();

	/**
	 * Add an accessible Tile north, east, south, or west of this one.
	 * 
	 * @param other
	 *            The neighbour to add.
	 */
	public void addAccessibleNeighbour( Tile other ) {
		if ( !this.accessibleNeighbours.contains( other ) ) {
			this.accessibleNeighbours.add( other );
		}
	}

	public Set< Tile > getAccessibleNeighbours() {
		return new HashSet<>( this.accessibleNeighbours );
	}

	public int getNumberOfAccessibleNeighbours() {
		return this.accessibleNeighbours.size();
	}

	public abstract void print();

}
