package cluedo.server;

import glhf.server.GlhfConnection;
import glhf.server.Server;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import cluedo.common.definition.Definition;
import cluedo.common.definition.DefinitionException;
import cluedo.common.message.server.DefinitionMessage;
import cluedo.server.definition.DefinitionManager;
import crossnet.Connection;
import crossnet.listener.ConnectionListenerAdapter;
import crossnet.log.Log;
import crossnet.message.Message;

public class CluedoServer {

	private final Server server = new Server();

	private List< String > definitionText;
	private Definition definition;

	public CluedoServer( final int port, final int noPlayers, final Path definitionPath ) throws IOException {
		if ( definitionPath == null ) {
			// Load default Definition
			DefinitionManager definitionManager = new DefinitionManager();
			String defaultDefinition = "Default";

			try {
				this.definitionText = definitionManager.getDefinitionText( defaultDefinition );
				this.definition = definitionManager.getDefinition( defaultDefinition );
			} catch ( DefinitionException e ) {
				Log.error( "Cluedo-server", "Could not load default definition '" + defaultDefinition + "'", e );
			}
		} else {
			String name = DefinitionManager.pathToDefinitionName( definitionPath );
			try {
				this.definitionText = DefinitionManager.readDefinitionText( definitionPath );
				this.definition = DefinitionManager.parseDefinitionFromText( name, this.definitionText );
			} catch ( DefinitionException e ) {
				Log.error( "Cluedo-server", "Could not load definition '" + name + "'", e );
			}
		}

		if ( this.definition == null ) {
			Log.error( "Cluedo-server", "No defintion, no play" );
			//TODO DIE by Exception.
			System.exit( -1 );
		}

		this.server.getMessageParser().setTieredMessageParser( new ServerMessageParser() );

		this.server.addConnectionListener( new ConnectionListenerAdapter() {

			@Override
			public void connected( Connection connection ) {
				GlhfConnection glhfConnection = (GlhfConnection) connection;
				glhfConnection.send( new DefinitionMessage( CluedoServer.this.definitionText ) );
			}

			@Override
			public void disconnected( Connection connection ) {
				// Override this if necessary.
			}

			@Override
			public void received( Connection connection, Message message ) {
				// Override this if necessary.
			}
		} );

		this.server.bind( port );
	}

	public void loop() {
		while ( true ) {
			// Stay a while, and listen.
		}
	}
}
