package game.board;

import game.board.tiles.Hallway;
import game.board.tiles.Room;
import game.board.tiles.Tile;

import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class Board {

	private final Tile[][] board;
	private final Map< String, Position > startPositions;

	public Board( final Tile[][] board, final Map< String, Position > startPositions ) {
		this.board = board;
		this.startPositions = startPositions;
	}

	public Tile[][] getBoard() {
		return this.board;
	}

	public Map< String, Position > getStartPositions() {
		return this.startPositions;
	}

	public static void printBoard( Tile[][] board ) {
		int rows = board.length;
		int cols = board[0].length;

		for ( int row = 0; row < rows; row++ ) {
			for ( int col = 0; col < cols; col++ ) {
				board[row][col].print();
				System.out.print( " " );
			}
			System.out.println();
		}
	}

	public static void printNoOfNeighbours( Tile[][] board ) {
		int rows = board.length;
		int cols = board[0].length;

		for ( int row = 0; row < rows; row++ ) {
			for ( int col = 0; col < cols; col++ ) {
				Tile tile = board[row][col];
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

	public static void printNoOfAssociatedRooms( Tile[][] board ) {
		int rows = board.length;
		int cols = board[0].length;

		for ( int row = 0; row < rows; row++ ) {
			for ( int col = 0; col < cols; col++ ) {
				Set< Tile > neighbours = board[row][col].getAccessibleNeighbours();
				int roomCount = 0;
				for ( Tile tile : neighbours ) {
					if ( tile instanceof Room ) {
						roomCount++;
					}
				}

				if ( roomCount != 0 ) {
					System.out.print( roomCount );
				} else {
					board[row][col].print();
				}

				System.out.print( " " );
			}
			System.out.println();
		}
	}

}
