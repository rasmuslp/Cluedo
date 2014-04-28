package cluedo.common.message.server;

import java.io.IOException;

import cluedo.common.message.CluedoMessage;
import cluedo.common.message.CluedoMessageType;
import crossnet.log.Log;
import crossnet.util.ByteArrayReader;
import crossnet.util.ByteArrayWriter;

public class TurnStartMessage extends CluedoMessage {

	private final int id;

	public TurnStartMessage( int id ) {
		super( CluedoMessageType.S_TURN_START );
		this.id = id;
	}

	public int getID() {
		return this.id;
	}

	@Override
	protected void serializePayload( ByteArrayWriter to ) throws IOException {
		to.writeInt( this.id );
	}

	/**
	 * Construct an TurnStartMessage from the provided payload.
	 * 
	 * @param payload
	 *            The payload from which to determine the content of this.
	 * @return A freshly parsed TurnStartMessage.
	 */
	public static TurnStartMessage parse( ByteArrayReader payload ) {
		try {
			int id = payload.readInt();
			return new TurnStartMessage( id );
		} catch ( IOException e ) {
			Log.error( "Cluedo-common", "Error deserializing TurnStartMessage:", e );
		}

		return null;
	}
}
