package cluedo.common.message.client;

import java.io.IOException;

import cluedo.common.cards.Card;
import cluedo.common.cards.ThreeCardPack;
import cluedo.common.definition.Definition;
import cluedo.common.message.CluedoMessage;
import cluedo.common.message.CluedoMessageType;
import crossnet.log.Log;
import crossnet.util.ByteArrayReader;
import crossnet.util.ByteArrayWriter;

public class SuggestionMessage extends CluedoMessage {

	private final ThreeCardPack threeCardPack;

	public SuggestionMessage( ThreeCardPack threeCardPack ) {
		super( CluedoMessageType.C_SUGGESTION );
		this.threeCardPack = threeCardPack;
	}

	protected SuggestionMessage( CluedoMessageType cluedoMessageType, ThreeCardPack threeCardPack ) {
		super( cluedoMessageType );
		this.threeCardPack = threeCardPack;
	}

	public ThreeCardPack getThreeCardPack() {
		return this.threeCardPack;
	}

	@Override
	protected void serializePayload( ByteArrayWriter to ) throws IOException {
		to.writeString255( this.threeCardPack.getCharacterCard().getID() );
		to.writeString255( this.threeCardPack.getRoomCard().getID() );
		to.writeString255( this.threeCardPack.getWeaponCard().getID() );
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
			String characterCardID = payload.readString255();
			String roomCardID = payload.readString255();
			String weaponCardID = payload.readString255();

			Card characterCard = Definition.definition.getAllCards().get( characterCardID );
			Card roomCard = Definition.definition.getAllCards().get( roomCardID );
			Card weaponCard = Definition.definition.getAllCards().get( weaponCardID );

			return new SuggestionMessage( new ThreeCardPack( characterCard, roomCard, weaponCard ) );
		} catch ( IOException e ) {
			Log.error( "Cluedo-common", "Error deserializing SuggestionMessage:", e );
		}

		return null;
	}

}
