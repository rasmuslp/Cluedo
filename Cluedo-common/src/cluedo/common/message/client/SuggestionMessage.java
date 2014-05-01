package cluedo.common.message.client;

import java.io.IOException;

import cluedo.common.cards.ThreeCardPack;
import cluedo.common.message.CluedoMessageType;
import cluedo.common.message.ThreeCardPackMessage;
import crossnet.log.Log;
import crossnet.util.ByteArrayReader;

public class SuggestionMessage extends ThreeCardPackMessage {

	public SuggestionMessage( ThreeCardPack threeCardPack ) {
		super( CluedoMessageType.C_SUGGESTION, threeCardPack );
	}

	/**
	 * Construct an SuggestionMessage from the provided payload.
	 * 
	 * @param payload
	 *            The payload from which to determine the content of this.
	 * @return A freshly parsed SuggestionMessage.
	 */
	public static SuggestionMessage parse( ByteArrayReader payload ) {
		try {
			ThreeCardPack threeCardPack = ThreeCardPack.parse( payload );
			return new SuggestionMessage( threeCardPack );
		} catch ( IOException e ) {
			Log.error( "Cluedo-common", "Error deserializing SuggestionMessage:", e );
		}

		return null;
	}

}
