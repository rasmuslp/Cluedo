package cluedo.client.human;

import java.io.IOException;
import java.net.UnknownHostException;

import cluedo.client.CluedoPlayer;

public class StartUp extends cluedo.client.StartUp {

	@Override
	protected CluedoPlayer getCluedoPlayer() {
		return new HumanCluedoPlayer( this.name );
	}

	public static void main( String[] args ) throws UnknownHostException, IOException {
		//Log.set( LogLevel.DEBUG );
		new StartUp().start( args );
	}
}
