package cluedo.common.message.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cluedo.common.cards.Card;
import cluedo.common.definition.Definition;
import cluedo.common.message.CluedoListMessage;
import cluedo.common.message.CluedoMessageType;
import crossnet.log.Log;
import crossnet.util.ByteArrayReader;
import crossnet.util.ByteArrayWriter;

public class DisproveMessage extends CluedoListMessage< Card > {

	public DisproveMessage( List< Card > list ) {
		super( CluedoMessageType.C_DISPROVE, list );
	}

	public boolean disproved() {
		if ( this.list.size() == 0 ) {
			return false;
		}

		return true;
	}

	@Override
	protected void serializeStatic( ByteArrayWriter to ) throws IOException {
		// No static information to serialise.
	}

	@Override
	protected void serializeListObject( int atIndex, ByteArrayWriter to ) throws IOException {
		Card card = this.list.get( atIndex );
		to.writeString255( card.getID() );
	}

	/**
	 * Construct an DisproveMessage from the provided payload.
	 * 
	 * @param payload
	 *            The payload from which to determine the content of this.
	 * @return A freshly parsed DisproveMessage.
	 */
	public static DisproveMessage parse( ByteArrayReader payload ) {
		try {
			List< Card > cards = new ArrayList<>();
			int count = payload.readInt();
			for ( int i = 0; i < count; i++ ) {
				String cardID = payload.readString255();
				cards.add( Definition.definition.getAllCards().get( cardID ) );
			}
			return new DisproveMessage( cards );
		} catch ( IOException e ) {
			Log.error( "Cluedo-common", "Error deserializing DisproveMessage:", e );
		}

		return null;
	}

}
