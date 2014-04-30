package cluedo.common.message.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cluedo.common.message.CluedoListMessage;
import cluedo.common.message.CluedoMessageType;
import crossnet.log.Log;
import crossnet.util.ByteArrayReader;
import crossnet.util.ByteArrayWriter;

public class DefinitionMessage extends CluedoListMessage< String > {

	public DefinitionMessage( List< String > text ) {
		super( CluedoMessageType.S_DEFINITION, text );
	}

	@Override
	protected void serializeStatic( ByteArrayWriter to ) throws IOException {
		// No static information to serialise.
	}

	@Override
	protected void serializeListObject( int atIndex, ByteArrayWriter to ) throws IOException {
		String line = this.list.get( atIndex );
		to.writeString255( line );
	}

	/**
	 * Construct an DefinitionMessage from the provided payload.
	 * 
	 * @param payload
	 *            The payload from which to determine the content of this.
	 * @return A freshly parsed DefinitionMessage.
	 */
	public static DefinitionMessage parse( ByteArrayReader payload ) {
		try {
			List< String > lines = new ArrayList<>();
			int count = payload.readInt();
			for ( int i = 0; i < count; i++ ) {
				String line = payload.readString255();
				lines.add( line );
			}
			return new DefinitionMessage( lines );
		} catch ( IOException e ) {
			Log.error( "Cluedo-common", "Error deserializing DefinitionMessage:", e );
		}

		return null;
	}

}
