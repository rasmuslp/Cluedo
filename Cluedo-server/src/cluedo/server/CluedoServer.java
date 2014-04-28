package cluedo.server;

import glhf.common.message.GlhfListMessage;
import glhf.common.message.GlhfMessage;
import glhf.common.message.common.ChatMessage;
import glhf.common.player.Player;
import glhf.server.GlhfConnection;
import glhf.server.GlhfServer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import cluedo.common.definition.Definition;
import cluedo.common.definition.DefinitionException;
import cluedo.common.message.CluedoMessageParser;
import cluedo.common.message.server.DefinitionMessage;
import cluedo.common.message.server.StartingMessage;
import cluedo.server.definition.DefinitionManager;
import crossnet.Connection;
import crossnet.listener.ConnectionListenerAdapter;
import crossnet.log.Log;
import crossnet.message.Message;
import crossnet.message.crossnet.CrossNetMessage;

public class CluedoServer {

	private final GlhfServer glhfServer = new GlhfServer();

	private final int noPlayers;

	private List< String > definitionText;
	private Definition definition;

	private boolean gameRunning = false;

	public CluedoServer( final int port, final int noPlayers, final Path definitionPath ) throws IOException {
		this.noPlayers = noPlayers;

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

		this.glhfServer.getMessageParser().setTieredMessageParser( new CluedoMessageParser() );

		this.glhfServer.addConnectionListener( new ConnectionListenerAdapter() {

			@Override
			public void connected( Connection connection ) {
				GlhfConnection glhfConnection = (GlhfConnection) connection;
				if ( CluedoServer.this.glhfServer.getPlayers().size() > CluedoServer.this.noPlayers ) {
					// Kick
					glhfConnection.send( new ChatMessage( "Game full." ) );
					connection.close();
				} else {
					// Allow join
					glhfConnection.send( new DefinitionMessage( CluedoServer.this.definitionText ) );
				}

			}

			@Override
			public void disconnected( Connection connection ) {
				// Override this if necessary.
			}

			@Override
			public void received( Connection connection, Message message ) {
				if ( !( message instanceof GlhfMessage || message instanceof GlhfListMessage || message instanceof CrossNetMessage ) ) {
					Log.warn( "Cluedo-server", "Got unexpected Message Type: " + message.getClass().getSimpleName() );
				}
			}
		} );

		this.glhfServer.bind( port );
	}

	public void loop() {
		while ( true ) {
			// Stay a while, and listen.

			if ( !this.gameRunning ) {
				if ( this.allReady() ) {
					List< Integer > list = new ArrayList<>();
					for ( Player player : this.glhfServer.getPlayers().values() ) {
						list.add( player.getID() );
					}
					this.glhfServer.sendToAll( new StartingMessage( list ) );
					this.gameRunning = true;
				}
			}

			try {
				Thread.sleep( 100 );
			} catch ( InterruptedException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private boolean allReady() {
		if ( this.noPlayers != this.glhfServer.getPlayers().size() ) {
			return false;
		}

		for ( Player player : this.glhfServer.getPlayers().values() ) {
			if ( !player.isReady() ) {
				return false;
			}
		}

		return true;
	}
}
