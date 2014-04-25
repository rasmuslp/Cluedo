package cluedo.server.definition;

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

import cluedo.common.definition.Definition;
import cluedo.common.definition.DefinitionException;
import cluedo.common.definition.DefinitionParser;
import crossnet.log.Log;

/**
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class DefinitionManager {

	/**
	 * Name of directory containing Cluedo Definitions.
	 */
	private static final String DIR = "definitions";

	/**
	 * Cluedo Definition file extension.
	 */
	private static final String DOTEXT = ".cluedo";

	/**
	 * Definition map from ID to raw text
	 */
	private final Map< String, List< String >> definitions = new HashMap<>();

	public DefinitionManager() {
		// Find definitions
		List< Path > definitionPaths = DefinitionManager.findDefinitionPaths();
		if ( definitionPaths.size() == 0 ) {
			Log.warn( "Cluedo-server", "No definitions found" );
		}

		// Load definitions
		for ( Path path : definitionPaths ) {
			String name = DefinitionManager.pathToDefinitionName( path );

			try {
				List< String > text = DefinitionManager.readDefinitionText( path );
				// Parsing the Defintion validates it.
				Definition definition = DefinitionManager.parseDefinitionFromText( name, text );
				this.definitions.put( definition.getName(), text );
			} catch ( DefinitionException e ) {
				Log.error( "Cluedo-server", "Failed to load definition '" + name + "'", e );
			}
		}
	}

	/**
	 * Finds Cluedo Definitions in {@link #DIR}.
	 * 
	 * @return The paths of the found Definitions.
	 */
	public static List< Path > findDefinitionPaths() {
		List< Path > definitionPaths = new ArrayList<>();

		Path dir = Paths.get( DefinitionManager.DIR );
		try ( DirectoryStream< Path > paths = Files.newDirectoryStream( dir, "*" + DefinitionManager.DOTEXT ) ) {
			for ( Path path : paths ) {
				if ( Files.isRegularFile( path ) ) {
					definitionPaths.add( path );
				}
			}
		} catch ( IOException e ) {
			Log.error( "Cluedo-server", "An error occured when looking for definitions in '" + dir.toAbsolutePath() + "'", e );
		}

		return definitionPaths;
	}

	/**
	 * Converts a Path to a Definition name.
	 * 
	 * @param path
	 *            The path to convert.
	 * @return The Definition name.
	 */
	public static String pathToDefinitionName( final Path path ) {
		String fileName = path.getFileName().toString();
		int dotIndex = fileName.lastIndexOf( '.' );
		return fileName.substring( 0, dotIndex );
	}

	/**
	 * Reads a Definition file.
	 * 
	 * @param path
	 *            The path of the Definition file.
	 * @return The content of the Definition file.
	 * @throws DefinitionException
	 *             If an error occurred reading the file.
	 */
	public static List< String > readDefinitionText( final Path path ) throws DefinitionException {
		List< String > text = null;
		try {
			text = Files.readAllLines( path, Charset.forName( "UTF-8" ) );
		} catch ( IOException e ) {
			throw new DefinitionException( "Error reading file '" + path.toAbsolutePath() + "': " + e.getMessage() );
		}
		return text;
	}

	/**
	 * Parse a text into a Definition.
	 * 
	 * @param name
	 *            The name of the Definition.
	 * @param text
	 *            The text representing the Definition.
	 * @return A freshly parsed Definition.
	 * @throws DefinitionException
	 *             If an error occurs parsing the text.
	 */
	public static Definition parseDefinitionFromText( final String name, final List< String > text ) throws DefinitionException {
		Definition definition = DefinitionParser.parseDefinitionFromText( name, text );
		Log.info( "Cluedo-server", "Validated definition '" + name + "'" );
		return definition;
	}

	/**
	 * @return A list of Definition names.
	 */
	public List< String > getDefinitionNames() {
		return new ArrayList<>( this.definitions.keySet() );
	}

	/**
	 * Reads a Definition file.
	 * 
	 * @param name
	 *            The name of the Definition.
	 * @return The content of the Definition.
	 * @throws DefinitionException
	 *             If the Definition name doesn't exist.
	 */
	public List< String > getDefinitionText( final String name ) throws DefinitionException {
		if ( !this.definitions.containsKey( name ) ) {
			throw new DefinitionException( "The definition '" + name + "' was not found." );
		}

		// Return copy of Definition text
		return new ArrayList<>( this.definitions.get( name ) );
	}

	/**
	 * Gets a Definition text and parses it.
	 * 
	 * @param name
	 *            The name of the Definition.
	 * @return A freshly parsed Definition.
	 * @throws DefinitionException
	 *             If an error occurs parsing the Definition.
	 */
	public Definition getDefinition( final String name ) throws DefinitionException {
		return DefinitionParser.parseDefinitionFromText( name, this.getDefinitionText( name ) );
	}

}
