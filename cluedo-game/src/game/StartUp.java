package game;

import game.definition.Definition;
import game.definition.DefinitionException;
import game.definition.DefinitionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class StartUp {

	public static void main( String[] args ) {
		System.out.println( "Welcome to Cluedo." );
		BufferedReader in = new BufferedReader( new InputStreamReader( System.in ) );

		boolean inputValid = false;

		int noPlayers = 0;
		boolean humanParticipation = false;
		boolean printAll = false;

		// No. players
		System.out.println( "Please enter the number of players (3-6) [3]:" );
		while ( !inputValid ) {
			try {
				String input = in.readLine();
				if ( input.isEmpty() ) {
					// Default
					noPlayers = 3;
					break;
				}
				noPlayers = Integer.parseInt( input );
				if ( noPlayers < 3 || 6 < noPlayers ) {
					System.out.println( "Must be 3 to 6." );
				} else {
					inputValid = true;
				}
			} catch ( Exception e ) {
				System.out.println( "Must be 3 to 6." );
			}
		}

		// Human participation
		System.out.println( "Will you be participating ? (y/n) [y]" );
		while ( true ) {
			try {
				String input = in.readLine();
				if ( input.isEmpty() ) {
					// Default
					humanParticipation = true;
					break;
				}
				if ( input.equalsIgnoreCase( "y" ) ) {
					humanParticipation = true;
					break;
				} else if ( input.equalsIgnoreCase( "n" ) ) {
					humanParticipation = false;
					break;
				}
				System.out.println( "Must be 'y' or 'n'." );
			} catch ( IOException e ) {
				System.out.println( "Must be 'y' or 'n'." );
			}
		}

		// Debug
		System.out.println( "Print all debug information ? (y/n) [n]" );
		while ( true ) {
			try {
				String input = in.readLine();
				if ( input.isEmpty() ) {
					// Default
					printAll = false;
					break;
				}
				if ( input.equalsIgnoreCase( "y" ) ) {
					printAll = true;
					break;
				} else if ( input.equalsIgnoreCase( "n" ) ) {
					printAll = false;
					break;
				}
				System.out.println( "Must be 'y' or 'n'." );
			} catch ( IOException e ) {
				System.out.println( "Must be 'y' or 'n'." );
			}
		}

		// Get default Definition
		DefinitionManager definitionManager = new DefinitionManager();
		List< String > definitions = definitionManager.getDefinitionNames();
		Definition definition = null;
		try {
			definition = definitionManager.getDefinition( definitions.get( 0 ) );
		} catch ( DefinitionException e ) {
			e.printStackTrace();
		}

		// Start game
		Game game = new Game( definition, noPlayers, humanParticipation );
		game.prepareGame();
		game.gameLoop();
	}
}
