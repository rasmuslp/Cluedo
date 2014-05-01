package cluedo.common.message.server;

import java.io.IOException;

import cluedo.common.cards.ThreeCardPack;
import cluedo.common.message.CluedoMessageType;
import cluedo.common.message.ThreeCardPackMessage;
import crossnet.log.Log;
import crossnet.util.ByteArrayReader;

public class DisproveRequestMessage extends ThreeCardPackMessage {

	public DisproveRequestMessage( ThreeCardPack threeCardPack ) {
		super( CluedoMessageType.S_DISPROVE_REQ, threeCardPack );
	}

	/**
	 * Construct an DisproveRequestMessage from the provided payload.
	 * 
	 * @param payload
	 *            The payload from which to determine the content of this.
	 * @return A freshly parsed DisproveRequestMessage.
	 */
	public static DisproveRequestMessage parse( ByteArrayReader payload ) {
		try {
			ThreeCardPack threeCardPack = ThreeCardPack.parse( payload );
			return new DisproveRequestMessage( threeCardPack );
		} catch ( IOException e ) {
			Log.error( "Cluedo-common", "Error deserializing DisproveRequestMessage:", e );
		}

		return null;
	}
}
