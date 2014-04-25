package cluedo.common.definition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import cluedo.common.board.Board;
import cluedo.common.board.Position;
import cluedo.common.board.tiles.Hallway;
import cluedo.common.board.tiles.Room;
import cluedo.common.board.tiles.Tile;
import cluedo.common.board.tiles.Wall;
import cluedo.common.cards.Card;
import cluedo.common.cards.Card.CardType;
import cluedo.common.cards.DefaultCard;
import crossnet.log.Log;

/**
 * Utility class that contains all the necessary methods for parsing text and constructing a {@link Definition}.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class DefinitionParser {

	/**
	 * Eats a text and produces a Definition if possible.
	 * 
	 * @param name
	 *            The name of the Definition.
	 * @param text
	 *            The text from which to parse the Definition.
	 * @return A Definition from the parsed text.
	 * @throws DefinitionException
	 *             If for any reason an error occurred that prevented the construction of a meaningful Definition.
	 */
	public static Definition parseDefinitionFromText( final String name, final List< String > text ) throws DefinitionException {
		ListIterator< String > iterator = text.listIterator();

		// Read minimum and maximum number of players.
		int[] noPlayers = DefinitionParser.readNoPlayers( iterator );

		// Construct the Cards of the game
		Map< String, Card > characterCards = DefinitionParser.parseCards( CardType.CHARACTER, "C", iterator );
		Map< String, Card > roomCards = DefinitionParser.parseCards( CardType.ROOM, "R", iterator );
		Map< String, Card > weaponCards = DefinitionParser.parseCards( CardType.WEAPON, "W", iterator );

		// Read and do validation of Secret Passages
		Map< String, String[] > secretPassages = DefinitionParser.readSecretPassages( iterator );
		for ( Map.Entry< String, String[] > entry : secretPassages.entrySet() ) {
			if ( !roomCards.containsKey( entry.getValue()[0] ) ) {
				throw new DefinitionException( "Secret passage '" + entry.getKey() + "' reference unknown room ID: " + entry.getValue()[0] );
			}
			if ( !roomCards.containsKey( entry.getValue()[1] ) ) {
				throw new DefinitionException( "Secret passage '" + entry.getKey() + "' reference unknown room ID: " + entry.getValue()[1] );
			}
		}

		// Read and parse Board
		String[][] readBoard = DefinitionParser.readBoard( iterator );
		Board parsedBoard = DefinitionParser.parseBoard( readBoard, characterCards.keySet(), roomCards.keySet(), secretPassages.values() );

		List< Card > characterCardList = new ArrayList<>( characterCards.values() );
		List< Card > roomCardList = new ArrayList<>( roomCards.values() );
		List< Card > weaponCardList = new ArrayList<>( weaponCards.values() );

		return new Definition( name, noPlayers[0], noPlayers[1], characterCardList, roomCardList, weaponCardList, parsedBoard );
	}

	/**
	 * Reads a line that denotes the minimum and maximum number of players.
	 * 
	 * @param iterator
	 *            For the line.
	 * @return Array of length two. First element is the minimum- and second element if the maximum- number of players.
	 * @throws DefinitionException
	 *             If for any reason an error occurred that prevented the determination of min/max number of players.
	 */
	public static int[] readNoPlayers( final ListIterator< String > iterator ) throws DefinitionException {
		String line = iterator.next();
		String[] lineSplit = DefinitionParser.splitLine( line, "-", 2 );
		int[] noPlayers = new int[2];
		try {
			noPlayers[0] = Integer.parseInt( lineSplit[0] );
			noPlayers[1] = Integer.parseInt( lineSplit[1] );
		} catch ( NumberFormatException e ) {
			throw new DefinitionException( "Could not parse number of players: " + e.getMessage() );
		}
		return noPlayers;
	}

	/**
	 * Reads consecutive text lines starting with {@code startsWith} and constructs a Map of {@link Card}s.
	 * 
	 * E.g. for Characters, this would look like:
	 * C1:Name
	 * C2:Name
	 * 
	 * NB: IDs must be unique
	 * 
	 * @param type
	 *            The type of card to construct.
	 * @param startsWith
	 *            The substring the lines should start with to be considered of the given {@link CardType}.
	 * @param iterator
	 *            For the lines.
	 * @return A mapping from ID to Card.
	 * @throws DefinitionException
	 *             If for any reason an error occurred that prevented the construction of a meaningful Map.
	 */
	public static Map< String, Card > parseCards( final CardType type, final String startsWith, final ListIterator< String > iterator ) throws DefinitionException {
		// Mapping of ID to Card
		Map< String, Card > cards = new HashMap<>();

		// Read and parse cards
		List< String[] > lines = DefinitionParser.parseLines( startsWith, iterator );
		for ( String[] line : lines ) {
			String ID = line[0];
			String title = line[1];

			Card card = new DefaultCard( type, ID, title );
			if ( cards.containsKey( ID ) ) {
				throw new DefinitionException( "Error parsing card. Card ID '" + ID + "' already used. Line was: " + line );
			}
			cards.put( ID, card );
		}

		return cards;
	}

	/**
	 * Reads consecutive text lines that should constitute Secret Passages between Rooms
	 * 
	 * @param iterator
	 *            For the lines.
	 * @return A mapping from ID to pairs of Rooms.
	 * @throws DefinitionException
	 *             If for any reason an error occurred that prevented the construction of a meaningful Map.
	 */
	public static Map< String, String[] > readSecretPassages( final ListIterator< String > iterator ) throws DefinitionException {
		// Mapping of Secret Passage ID to Room IDs
		Map< String, String[] > secretPassages = new HashMap<>();

		// Read secret passages
		List< String[] > lines = DefinitionParser.parseLines( "S", iterator );
		for ( String[] line : lines ) {
			String ID = line[0];
			String[] rooms = DefinitionParser.splitLine( line[1], "-", 2 );
			if ( secretPassages.containsKey( ID ) ) {
				throw new DefinitionException( "Error parsing secret passage. ID '" + ID + "' already used. Line was: " + line );
			}
			secretPassages.put( ID, rooms );
		}

		return secretPassages;
	}

	/**
	 * Reads consecutive text lines starting with {@code startsWith}.
	 * 
	 * NB: The lines must be of the form 'ID1:Rest'
	 * 
	 * @param startsWith
	 *            The substring the lines should start with to be considered part of the sought information.
	 * @param iterator
	 *            For the lines.
	 * @return A list of pairs, where a pair is an ID and the rest.
	 * @throws DefinitionException
	 *             If a line couldn't be parsed.
	 */
	public static List< String[] > parseLines( final String startsWith, final ListIterator< String > iterator ) throws DefinitionException {
		List< String[] > lines = new ArrayList<>();
		List< Integer > indicies = new ArrayList<>();

		// Read cards
		while ( iterator.hasNext() ) {
			String line = iterator.next().trim();
			if ( line.startsWith( startsWith ) ) {
				String[] split = DefinitionParser.splitLine( line, ":", 2 );
				String ID = split[0];
				indicies.add( Integer.parseInt( ID.substring( startsWith.length() ) ) );
				lines.add( split );
			} else {
				// No more lines startsWith
				iterator.previous();
				break;
			}
		}

		// Check indices
		int lastIndex = 0;
		for ( int i = 0; i < indicies.size(); i++ ) {
			if ( ( lastIndex + 1 ) != indicies.get( i ) ) {
				Log.warn( "Cluedo-common", "Indicies not consecutive for type '" + startsWith + "'. Expected " + ( lastIndex + 1 ) + " but got " + indicies.get( i ) );
			}
			lastIndex++;
		}

		return lines;
	}

	/**
	 * Splits a line.
	 * 
	 * @param line
	 *            The line to split.
	 * @param splitRegex
	 *            The String to split around.
	 * @param splitLimit
	 *            The minimum number of splits sought.
	 * @return A split line.
	 * @throws DefinitionException
	 *             If the minimum number of sought splits wan't reached.
	 */
	public static String[] splitLine( final String line, final String splitRegex, final int splitLimit ) throws DefinitionException {
		String[] split = line.split( splitRegex, splitLimit );
		if ( split.length != splitLimit ) {
			throw new DefinitionException( "Error parsing line. Expected " + splitLimit + " splits but got " + split.length + ". Line was: " + line );
		}

		return split;
	}

	/**
	 * Reads the board from a text. This only reads the text into a 2D array of Strings and performs minimal
	 * verification that the data is sound.
	 * 
	 * @param iterator
	 *            For the lines.
	 * @return A 2D array of the board as Strings.
	 * @throws DefinitionException
	 *             If for any reason an error occurred that prevented the construction of a meaningful board.
	 */
	public static String[][] readBoard( final ListIterator< String > iterator ) throws DefinitionException {
		String startMarker = "BOARD";
		String endMarker = "EOF";

		// Find start marker
		if ( !( iterator.hasNext() && iterator.next().trim().equals( startMarker ) ) ) {
			throw new DefinitionException( "Error reading board. Start marker '" + startMarker + "' not found." );
		}

		// Read board lines
		List< String[] > lines = new ArrayList<>();
		while ( iterator.hasNext() ) {
			String line = iterator.next().trim();
			if ( line.equals( endMarker ) ) {
				// End marker found, break
				iterator.previous();
				break;
			}

			String[] split = line.split( "\\s+" );
			lines.add( split );
		}

		// Determine size 
		if ( !( lines.size() > 0 && lines.get( 0 ).length > 1 ) ) {
			throw new DefinitionException( "Error reading board. Board is empty." );
		}
		int rows = lines.size();
		int cols = lines.get( 0 ).length;

		// Create board
		String[][] board = new String[rows][cols];
		for ( int row = 0; row < rows; row++ ) {
			String[] line = lines.get( row );
			if ( line.length != cols ) {
				throw new DefinitionException( "Error reading board. Expected " + cols + " but got " + line.length );
			}
			for ( int col = 0; col < line.length; col++ ) {
				board[row][col] = line[col];
			}
		}

		// Find end marker
		if ( !( iterator.hasNext() && iterator.next().trim().equals( endMarker ) ) ) {
			throw new DefinitionException( "Error reading board. End marker '" + endMarker + "' not found." );
		}

		return board;
	}

	/**
	 * Parses a 2D array of Strings and constructs a {@link Board}.
	 * 
	 * @param readBoard
	 *            The 2D array to base the construction on.
	 * @param characterIDs
	 *            IDs of the Characters. Used for validation of the Board and to map starting positions.
	 * @param roomIDs
	 *            IDs of the Rooms. Used for validation of the Board.
	 * @param secretPassages
	 *            Pairs of Rooms.
	 * @return A well defined Board.
	 * @throws DefinitionException
	 *             If for any reason an error occurred that prevented the construction of a meaningful Board.
	 */
	public static Board parseBoard( final String[][] readBoard, final Set< String > characterIDs, final Set< String > roomIDs, final Collection< String[] > secretPassages ) throws DefinitionException {
		// Board dimensions
		int rows = readBoard.length;
		int cols = readBoard[0].length;

		/// Validate IDs

		// All known IDs (that could be used on a Board)
		Set< String > allIDs = new HashSet<>();
		allIDs.addAll( characterIDs );
		allIDs.addAll( roomIDs );

		// Find all IDs
		Set< String > seenIDs = new HashSet<>();
		for ( int row = 0; row < rows; row++ ) {
			for ( int col = 0; col < cols; col++ ) {
				char c = readBoard[row][col].toCharArray()[0];
				switch ( c ) {
					case '#':
					case '*':
					case 'e':
						continue;
					case 'E':
						seenIDs.add( "R" + readBoard[row][col].substring( 1 ) );
						break;
					default:
						seenIDs.add( readBoard[row][col] );
				}
			}
		}

		// Check for unused IDs
		Set< String > unusedIDs = new HashSet<>( allIDs );
		unusedIDs.removeAll( seenIDs );
		if ( unusedIDs.size() > 0 ) {
			String message = "Board didn't use all specified IDs:";
			for ( String s : unusedIDs ) {
				message += " " + s;
			}
			throw new DefinitionException( message );
		}

		// Check for unknown IDs
		Set< String > unknownIDs = new HashSet<>( seenIDs );
		unknownIDs.removeAll( allIDs );
		if ( unknownIDs.size() > 0 ) {
			String message = "Board used unknown IDs:";
			for ( String s : unknownIDs ) {
				message += " " + s;
			}
			throw new DefinitionException( message );
		}

		// Build Rooms
		Map< String, Room > rooms = new HashMap<>();
		for ( String roomID : roomIDs ) {
			rooms.put( roomID, new Room() );
		}

		// Add Secret Passages to rooms
		for ( String[] split : secretPassages ) {
			Room room0 = rooms.get( split[0] );
			Room room1 = rooms.get( split[1] );
			room0.addSecretPassageTo( room1 );
			room1.addSecretPassageTo( room0 );
		}

		// Build Board
		Map< String, Position > startPositions = new HashMap<>();
		Map< Position, Room > entrances = new HashMap<>();
		Tile[][] board = new Tile[rows][cols];
		for ( int row = 0; row < rows; row++ ) {
			for ( int col = 0; col < cols; col++ ) {
				char c = readBoard[row][col].toCharArray()[0];
				switch ( c ) {
					case '#':
						// Wall
						board[row][col] = new Wall();
						break;
					case 'C':
						// Character start position (also a Hallway Tile)
						startPositions.put( readBoard[row][col], new Position( row, col ) );
						//$FALL-THROUGH$
					case '*':
						// Hallway
						board[row][col] = new Hallway();
						break;
					case 'e':
						// Pre-Entrance
						board[row][col] = new Hallway( true );
						break;
					case 'E':
						// Entrance (a Room in disguise)
						Room room = rooms.get( "R" + readBoard[row][col].substring( 1 ) );
						entrances.put( new Position( row, col ), room );
						board[row][col] = room;
						break;
					case 'R':
						// Room 
						board[row][col] = rooms.get( readBoard[row][col] );
						break;
					default:
						// This should not be reachable
						throw new DefinitionException( "Board used unknown ID: '" + c + "' at row " + row + " column " + col );
				}
			}
		}

		// Link Tiles
		for ( int row = 0; row < rows; row++ ) {
			for ( int col = 0; col < cols; col++ ) {
				Tile tile = board[row][col];
				List< Position > neighbours = DefinitionParser.findNeighbours( readBoard, row, col, "" );

				if ( tile instanceof Hallway ) {
					for ( Position position : neighbours ) {
						Tile other = board[position.getRow()][position.getColumn()];
						if ( other instanceof Hallway ) {
							// Hallway to Hallway link
							tile.addAccessibleNeighbour( other );
						} else if ( other instanceof Room && ( (Hallway) tile ).isPreEntrance() ) {
							// Possible Room to Hallway link
							if ( entrances.containsKey( new Position( position.getRow(), position.getColumn() ) ) ) {
								// Indeed, this position in the Room is an Entrance, link awaaaaaay...
								tile.addAccessibleNeighbour( other );
								other.addAccessibleNeighbour( tile );
								entrances.remove( new Position( position.getRow(), position.getColumn() ) );
							}
						}

					}
				}
			}
		}

		// Verify that all Entrances were used
		if ( entrances.size() > 0 ) {
			String message = "Board has unused Entrance. Is the pre-entrance missing?";
			for ( Position position : entrances.keySet() ) {
				message += " at row " + position.getRow() + " column " + position.getColumn();
			}
			throw new DefinitionException( message );
		}

		// Verify that all pre-entrances were used
		for ( int row = 0; row < rows; row++ ) {
			for ( int col = 0; col < cols; col++ ) {
				Tile tile = board[row][col];
				if ( tile instanceof Hallway && ( (Hallway) tile ).isPreEntrance() ) {
					Set< Tile > neighbours = tile.getAccessibleNeighbours();
					boolean roomSeen = false;
					for ( Tile neighbour : neighbours ) {
						if ( neighbour instanceof Room ) {
							roomSeen = true;
							break;
						}
					}
					if ( !roomSeen ) {
						throw new DefinitionException( "Board has pre-entrance that is not linked to any Entrance. At row " + row + " column " + col );
					}
				}

			}
		}

		return new Board( board, startPositions );
	}

	/**
	 * Given a 2D String representation of a board and a position, it determines the neighbours that {@link startsWith}.
	 * 
	 * @param board
	 *            A 2D array of the board as Strings.
	 * @param startRow
	 *            The row of the position.
	 * @param startCol
	 *            The column of the position.
	 * @param startsWith
	 *            The substring the neighbours should start with to be considered part of the sought information.
	 * @return A list of Points that surrounds the given position.
	 */
	public static List< Position > findNeighbours( final String[][] board, final int startRow, final int startCol, final String startsWith ) {
		List< Position > neigbours = new ArrayList<>();

		int rows = board.length;
		int cols = board[0].length;

		if ( 0 < startRow ) {
			// North
			if ( board[startRow - 1][startCol].startsWith( startsWith ) ) {
				neigbours.add( new Position( startRow - 1, startCol ) );
			}
		}

		if ( startCol < cols - 1 ) {
			// East
			if ( board[startRow][startCol + 1].startsWith( startsWith ) ) {
				neigbours.add( new Position( startRow, startCol + 1 ) );
			}
		}

		if ( startRow < rows - 1 ) {
			// South
			if ( board[startRow + 1][startCol].startsWith( startsWith ) ) {
				neigbours.add( new Position( startRow + 1, startCol ) );
			}
		}

		if ( 0 < startCol ) {
			// West
			if ( board[startRow][startCol - 1].startsWith( startsWith ) ) {
				neigbours.add( new Position( startRow, startCol - 1 ) );
			}
		}

		return neigbours;
	}

}
