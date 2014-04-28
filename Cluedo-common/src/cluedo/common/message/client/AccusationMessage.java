package cluedo.common.message.client;

import java.io.IOException;
import java.nio.charset.Charset;

import cluedo.common.cards.Card;
import cluedo.common.cards.ThreeCardPack;
import cluedo.common.definition.Definition;
import cluedo.common.message.CluedoMessageType;
import crossnet.log.Log;
import crossnet.util.ByteArrayReader;

public class AccusationMessage extends SuggestionMessage {

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
			int characterCardLength = payload.readUnsignedByte();
			byte[] characterCardBytes = new byte[characterCardLength];
			payload.readByteArray( characterCardBytes );
			String characterCardID = new String( characterCardBytes, Charset.forName( "UTF-8" ) );

			int roomCardLength = payload.readUnsignedByte();
			byte[] roomCardBytes = new byte[roomCardLength];
			payload.readByteArray( roomCardBytes );
			String roomCardID = new String( roomCardBytes, Charset.forName( "UTF-8" ) );

			int weaponCardLength = payload.readUnsignedByte();
			byte[] weaponCardBytes = new byte[weaponCardLength];
			payload.readByteArray( weaponCardBytes );
			String weaponCardID = new String( weaponCardBytes, Charset.forName( "UTF-8" ) );

			Card characterCard = Definition.definition.getAllCards().get( characterCardID );
			Card roomCard = Definition.definition.getAllCards().get( roomCardID );
			Card weaponCard = Definition.definition.getAllCards().get( weaponCardID );

			return new AccusationMessage( new ThreeCardPack( characterCard, roomCard, weaponCard ) );
		} catch ( IOException e ) {
			Log.error( "Cluedo-common", "Error deserializing AccusationMessage:", e );
		}

		return null;
	}

}
