package cluedo.common.message.server;

import java.io.IOException;

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
		to.writeString255( this.card.getID() );
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
			String cardID = payload.readString255();
			Card card = Definition.definition.getAllCards().get( cardID );
			return new HandCardMessage( card );
		} catch ( IOException e ) {
			Log.error( "Cluedo-common", "Error deserializing HandCardMessage:", e );
		}

		return null;
	}

}
