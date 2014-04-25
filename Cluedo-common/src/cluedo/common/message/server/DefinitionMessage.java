package cluedo.common.message.server;

import java.io.IOException;
import java.nio.charset.Charset;
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
		byte[] line = this.list.get( atIndex ).getBytes( Charset.forName( "UTF-8" ) );
		to.writeByte( line.length );
		to.writeByteArray( line );
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
				int lineLength = payload.readUnsignedByte();
				byte[] lineBytes = new byte[lineLength];
				payload.readByteArray( lineBytes );
				String line = new String( lineBytes, Charset.forName( "UTF-8" ) );
				lines.add( line );
			}
			return new DefinitionMessage( lines );
		} catch ( IOException e ) {
			Log.error( "Cluedo-common", "Error deserializing DefinitionMessage:", e );
		}

		return null;
	}

}
