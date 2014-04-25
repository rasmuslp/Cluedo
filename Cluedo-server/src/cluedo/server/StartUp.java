package cluedo.server;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import crossnet.log.Log;

/**
 * The entry point for the Cluedo-server.
 * <p>
 * Parses command line arguments.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class StartUp {

	public static void main( String[] args ) {
		int port = 0;
		int noPlayers = 0;
		Path definition = null;
		if ( args.length > 0 ) {
			Log.info( "Cluedo-server", "Parsing command line arguments" );
			boolean argParseError = false;
			if ( args.length > 3 ) {
				Log.warn( "Cluedo-server", "Too many command line arguments." );
				argParseError = true;
			}

			for ( String arg : args ) {
				// Try parsing numbers
				try {
					int no = Integer.parseInt( arg );
					if ( no > 1000 ) {
						// Port
						port = no;
					} else {
						// Number of players
						noPlayers = no;
					}
					continue;
				} catch ( NumberFormatException e ) {
					// This was not a number.
				}

				// Validate path of Definition file
				definition = Paths.get( arg );
				if ( !Files.exists( definition ) ) {
					argParseError = true;
					definition = null;
					Log.warn( "Cluedo-server", "Definition path not valid: " + arg );
				}
			}

			if ( argParseError ) {
				Log.info( "Cluedo-server", "Usage: Optional arguments are [port] [number of players] [definition]." );
				Log.info( "Cluedo-server", "[port]: Must be above 1000." );
				Log.info( "Cluedo-server", "[number of players]: Must be below 1000." );
				Log.info( "Cluedo-server", "[definition]: Must be a path to a Cluedo definition." );
				Log.info( "Cluedo-server", "Shutting down..." );
				System.exit( -1 );
			}
		}

		if ( port == 0 ) {
			port = 55100;
			Log.info( "Cluedo-server", "Port not specified. Using default: " + port );
		} else {
			Log.info( "Cluedo-server", "Port: " + port );
		}

		if ( noPlayers == 0 ) {
			noPlayers = 3;
			Log.info( "Cluedo-server", "Number of players not specified. Using default: " + noPlayers );
		} else {
			Log.info( "Cluedo-server", "Number of players: " + noPlayers );
		}

		if ( definition == null ) {
			Log.info( "Cluedo-server", "Definition not specified. Will use default." );
		} else {
			Log.info( "Cluedo-server", "Definition: " + definition.getFileName() );
		}

		CluedoServer cluedoServer = new CluedoServer( port, noPlayers, definition );
	}
}
