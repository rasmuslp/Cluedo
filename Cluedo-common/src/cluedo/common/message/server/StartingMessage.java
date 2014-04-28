package cluedo.common.message.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cluedo.common.message.CluedoListMessage;
import cluedo.common.message.CluedoMessageType;
import crossnet.log.Log;
import crossnet.util.ByteArrayReader;
import crossnet.util.ByteArrayWriter;

public class StartingMessage extends CluedoListMessage< Integer > {

	public StartingMessage( List< Integer > list ) {
		super( CluedoMessageType.S_STARTING, list );
	}

	@Override
	protected void serializeStatic( ByteArrayWriter to ) throws IOException {
		// No static information to serialise.
	}

	@Override
	protected void serializeListObject( int atIndex, ByteArrayWriter to ) throws IOException {
		int value = this.list.get( atIndex );
		to.writeInt( value );
	}

	/**
	 * Construct an StartingMessage from the provided payload.
	 * 
	 * @param payload
	 *            The payload from which to determine the content of this.
	 * @return A freshly parsed StartingMessage.
	 */
	public static StartingMessage parse( ByteArrayReader payload ) {
		try {
			List< Integer > ids = new ArrayList<>();
			int count = payload.readInt();
			for ( int i = 0; i < count; i++ ) {
				int id = payload.readInt();
				ids.add( id );
			}
			return new StartingMessage( ids );
		} catch ( IOException e ) {
			Log.error( "Cluedo-common", "Error deserializing StartingMessage:", e );
		}

		return null;
	}

}
