package cluedo.common.message.server;

import java.io.IOException;
import java.nio.charset.Charset;

import cluedo.common.cards.Card;
import cluedo.common.definition.Definition;
import cluedo.common.message.CluedoMessage;
import cluedo.common.message.CluedoMessageType;
import crossnet.log.Log;
import crossnet.util.ByteArrayReader;
import crossnet.util.ByteArrayWriter;

public class HandCardMessage extends CluedoMessage {

	private final Card card;

	public HandCardMessage( Card card ) {
		super( CluedoMessageType.S_HAND_CARD );
		this.card = card;
	}

	public Card getCard() {
		return this.card;
	}

	@Override
	protected void serializePayload( ByteArrayWriter to ) throws IOException {
		to.writeByteArray( this.card.getID().getBytes( Charset.forName( "UTF-8" ) ) );
	}

	/**
	 * Construct an HandCardMessage from the provided payload.
	 * 
	 * @param payload
	 *            The payload from which to determine the content of this.
	 * @return A freshly parsed HandCardMessage.
	 */
	public static HandCardMessage parse( ByteArrayReader payload ) {
		try {
			int bytes = payload.bytesAvailable();
			byte[] data = new byte[bytes];
			payload.readByteArray( data );
			String name = new String( data, Charset.forName( "UTF-8" ) );
			Card card = Definition.definition.getAllCards().get( name );
			return new HandCardMessage( card );
		} catch ( IOException e ) {
			Log.error( "Cluedo-common", "Error deserializing HandCardMessage:", e );
		}

		return null;
	}

}
