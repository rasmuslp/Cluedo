package cluedo.client;

import java.io.IOException;
import java.net.UnknownHostException;

import crossnet.log.Log;

/**
 * The entry point for the Cluedo-client.
 * <p>
 * Parses command line arguments.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class StartUp {

	private int port = 0;
	private String host;
	protected String name;

	public void start( String[] args ) throws UnknownHostException, IOException {
		this.parseArguments( args );
		this.defaultArguments();
		this.gogogo();
	}

	private void parseArguments( String[] args ) {
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
						this.port = no;
					}
					continue;
				} catch ( NumberFormatException e ) {
					// This was not a number.
				}

				if ( this.host == null ) {
					this.host = arg;
				}

				this.name = arg;
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
	}

	private void defaultArguments() {
		if ( this.port == 0 ) {
			this.port = 55100;
			Log.info( "Cluedo-client", "Port not specified. Using default: " + this.port );
		} else {
			Log.info( "Cluedo-client", "Port: " + this.port );
		}

		if ( this.host == null ) {
			this.host = "localhost";
			Log.info( "Cluedo-client", "Host not specified. Using default: " + this.host );
		} else {
			Log.info( "Cluedo-client", "Host: " + this.host );
		}

		if ( this.name == null ) {
			this.name = "EagerPlayer";
			Log.info( "Cluedo-client", "Name not specified. Using default: " + this.name );
		} else {
			Log.info( "Cluedo-client", "Name: " + this.name );
		}
	}

	protected CluedoPlayer getCluedoPlayer() {
		// Override this to supply an implementation of CluedoPlayer
		return null;
	}

	public void gogogo() throws UnknownHostException, IOException {
		CluedoClient cluedoClient = new CluedoClient( this.port, this.host, this.getCluedoPlayer() );
		cluedoClient.loop();
	}

}
