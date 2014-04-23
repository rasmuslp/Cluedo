package game.definition;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class DefinitionManager {

	private static final String DIR = "definitions";
	private static final String DOTEXT = ".cluedo";

	/**
	 * Definition map from ID to raw text
	 */
	private final Map< String, List< String >> definitions = new HashMap<>();

	public DefinitionManager() {
		// Find definitions
		List< Path > definitionPaths = null;
		try {
			definitionPaths = DefinitionManager.findDefinitionPaths();
		} catch ( DefinitionException e ) {
			//TODO: Log
			System.out.println( "Error finding definitions: " + e.getMessage() );
			return;
		}

		// Load definitions
		for ( Path path : definitionPaths ) {
			String name = DefinitionManager.pathToDefinitionName( path );
			List< String > text = null;
			try {
				text = Files.readAllLines( path, Charset.defaultCharset() );
			} catch ( IOException e ) {
				//TODO Log
				System.out.println( "Error reading file '" + path.toAbsolutePath() + "': " + e.getMessage() );
				continue;
			}

			try {
				// Validates definition by trying to construct it.
				Definition definition = DefinitionParser.parseDefinitionFromText( name, text );
				this.definitions.put( definition.getName(), text );
			} catch ( DefinitionException e ) {
				//TODO LOG
				System.out.println( "Error parsing definition '" + name + "': " + e.getMessage() );
			}
		}
	}

	public List< String > getDefinitionNames() {
		return new ArrayList<>( this.definitions.keySet() );
	}

	public Definition getDefinition( final String name ) throws DefinitionException {
		if ( !this.definitions.containsKey( name ) ) {
			throw new DefinitionException( "The definition '" + name + "' was not found." );
		}

		// Return new Definition
		return DefinitionParser.parseDefinitionFromText( name, this.definitions.get( name ) );
	}

	public List< String > getDefinitionText( final String name ) throws DefinitionException {
		if ( !this.definitions.containsKey( name ) ) {
			throw new DefinitionException( "The definition '" + name + "' was not found." );
		}

		// Return copy of Definition text
		return new ArrayList<>( this.definitions.get( name ) );
	}

	public static List< Path > findDefinitionPaths() throws DefinitionException {
		List< Path > definitionPaths = new ArrayList<>();
		Path dir = Paths.get( DIR );
		try ( DirectoryStream< Path > paths = Files.newDirectoryStream( dir, "*" + DOTEXT ) ) {
			for ( Path path : paths ) {
				if ( Files.isRegularFile( path ) ) {
					definitionPaths.add( path );
				}
			}
		} catch ( IOException e ) {
			throw new DefinitionException( "Error occured when looking for definitions in '" + dir.toAbsolutePath() + "': " + e.getMessage() );
		}

		return definitionPaths;
	}

	public static String pathToDefinitionName( final Path path ) {
		String fileName = path.getFileName().toString();
		int dotIndex = fileName.lastIndexOf( '.' );
		return fileName.substring( 0, dotIndex );
	}

}
