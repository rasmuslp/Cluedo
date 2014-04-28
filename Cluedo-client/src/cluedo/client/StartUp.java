package cluedo.client;

import java.io.IOException;
import java.net.UnknownHostException;

import crossnet.log.Log;
import crossnet.log.LogLevel;

/**
 * The entry point for the Cluedo-client.
 * <p>
 * Parses command line arguments.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class StartUp {

	public static void main( String[] args ) throws UnknownHostException, IOException {
		Log.set( LogLevel.DEBUG );

		int port = 0;
		String host = null;
		String name = null;
		if ( args.length > 0 ) {
			Log.info( "Cluedo-client", "Parsing command line arguments" );
			boolean argParseError = false;
			if ( args.length > 3 ) {
				Log.warn( "Cluedo-client", "Too many command line arguments." );
				argParseError = true;
			}

			for ( String arg : args ) {
				// Try parsing numbers
				try {
					int no = Integer.parseInt( arg );
					if ( no > 1000 ) {
						// Port
						port = no;
					}
					continue;
				} catch ( NumberFormatException e ) {
					// This was not a number.
				}

				if ( host == null ) {
					host = arg;
				}

				name = arg;
			}

			if ( argParseError ) {
				Log.info( "Cluedo-client", "Usage: Optional arguments are [port] [host] [name]." );
				Log.info( "Cluedo-client", "[port]: Must be above 1000." );
				Log.info( "Cluedo-client", "[host]: Hostname or IP address." );
				Log.info( "Cluedo-client", "[name]: Any name without white space." );
				Log.info( "Cluedo-client", "Shutting down..." );
				System.exit( -1 );
			}
		}

		if ( port == 0 ) {
			port = 55100;
			Log.info( "Cluedo-client", "Port not specified. Using default: " + port );
		} else {
			Log.info( "Cluedo-client", "Port: " + port );
		}

		if ( host == null ) {
			host = "localhost";
			Log.info( "Cluedo-client", "Host not specified. Using default: " + host );
		} else {
			Log.info( "Cluedo-client", "Host: " + host );
		}

		if ( name == null ) {
			name = "EagerPlayer";
			Log.info( "Cluedo-client", "Name not specified. Using default: " + name );
		} else {
			Log.info( "Cluedo-client", "Name: " + name );
		}

		CluedoClient cluedoClient = new CluedoClient( port, host, name );
		cluedoClient.loop();
	}
}
