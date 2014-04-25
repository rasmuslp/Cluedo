package cluedo.server;

import java.nio.file.Path;
import java.util.List;

import cluedo.common.definition.Definition;
import cluedo.common.definition.DefinitionException;
import cluedo.server.definition.DefinitionManager;
import crossnet.log.Log;

public class CluedoServer {

	private List< String > definitionText;
	private Definition definition;

	public CluedoServer( final int port, final int noPlayers, final Path definitionPath ) {
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

	}
}
