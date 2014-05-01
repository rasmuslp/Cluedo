package cluedo.common.message.client;

import java.io.IOException;

import cluedo.common.cards.ThreeCardPack;
import cluedo.common.message.CluedoMessageType;
import cluedo.common.message.ThreeCardPackMessage;
import crossnet.log.Log;
import crossnet.util.ByteArrayReader;

public class AccusationMessage extends ThreeCardPackMessage {

	public AccusationMessage( ThreeCardPack threeCardPack ) {
		super( CluedoMessageType.C_ACCUSATION, threeCardPack );
	}

	/**
	 * Construct an AccusationMessage from the provided payload.
	 * 
	 * @param payload
	 *            The payload from which to determine the content of this.
	 * @return A freshly parsed AccusationMessage.
	 */
	public static AccusationMessage parse( ByteArrayReader payload ) {
		try {
			ThreeCardPack threeCardPack = ThreeCardPack.parse( payload );
			return new AccusationMessage( threeCardPack );
		} catch ( IOException e ) {
			Log.error( "Cluedo-common", "Error deserializing AccusationMessage:", e );
		}

		return null;
	}

}
