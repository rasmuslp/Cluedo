package cluedo.common.board;

import java.util.Map;
import java.util.Set;

import cluedo.common.board.tiles.Hallway;
import cluedo.common.board.tiles.Room;
import cluedo.common.board.tiles.Tile;

/**
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class Board {

	private final Type type;
	private final Tile[][] tiles;
	private final Map< String, Position > startPositions;

	public Board( final Type type, final Tile[][] tiles, final Map< String, Position > startPositions ) {
		this.type = type;
		this.tiles = tiles;
		this.startPositions = startPositions;
	}

	public Type getType() {
		return this.type;
	}

	public Tile[][] getTiles() {
		return this.tiles;
	}

	public Map< String, Position > getStartPositions() {
		return this.startPositions;
	}

	public static void printBoard( Board board ) {
		Tile[][] tiles = board.tiles;
		int rows = tiles.length;
		int cols = tiles[0].length;

		for ( int row = 0; row < rows; row++ ) {
			for ( int col = 0; col < cols; col++ ) {
				tiles[row][col].print();
				System.out.print( " " );
			}
			System.out.println();
		}
	}

	public static void printNoOfNeighbours( Board board ) {
		Tile[][] tiles = board.tiles;
		int rows = tiles.length;
		int cols = tiles[0].length;

		for ( int row = 0; row < rows; row++ ) {
			for ( int col = 0; col < cols; col++ ) {
				Tile tile = tiles[row][col];
				if ( tile instanceof Hallway && ( (Hallway) tile ).isPreEntrance() ) {
					System.out.print( tile.getNumberOfAccessibleNeighbours() );
				} else if ( tile instanceof Room ) {
					System.out.print( tile.getNumberOfAccessibleNeighbours() );
				} else {
					tile.print();
				}
				System.out.print( " " );
			}
			System.out.println();
		}
	}

	public static void printNoOfAssociatedRooms( Board board ) {
		Tile[][] tiles = board.tiles;
		int rows = tiles.length;
		int cols = tiles[0].length;

		for ( int row = 0; row < rows; row++ ) {
			for ( int col = 0; col < cols; col++ ) {
				Set< Tile > neighbours = tiles[row][col].getAccessibleNeighbours();
				int roomCount = 0;
				for ( Tile tile : neighbours ) {
					if ( tile instanceof Room ) {
						roomCount++;
					}
				}

				if ( roomCount != 0 ) {
					System.out.print( roomCount );
				} else {
					tiles[row][col].print();
				}

				System.out.print( " " );
			}
			System.out.println();
		}
	}

	public enum Type {
		NORMAL, NONE, CIRCLE
	}

}
