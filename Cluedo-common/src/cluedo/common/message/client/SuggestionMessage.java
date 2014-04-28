package cluedo.common.message.client;

import java.io.IOException;
import java.nio.charset.Charset;

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
		byte[] characterCard = this.threeCardPack.getCharacterCard().getID().getBytes( Charset.forName( "UTF-8" ) );
		to.writeByte( characterCard.length );
		to.writeByteArray( characterCard );
		byte[] roomCard = this.threeCardPack.getRoomCard().getID().getBytes( Charset.forName( "UTF-8" ) );
		to.writeByte( roomCard.length );
		to.writeByteArray( roomCard );
		byte[] weaponCard = this.threeCardPack.getWeaponCard().getID().getBytes( Charset.forName( "UTF-8" ) );
		to.writeByte( weaponCard.length );
		to.writeByteArray( weaponCard );
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

			return new SuggestionMessage( new ThreeCardPack( characterCard, roomCard, weaponCard ) );
		} catch ( IOException e ) {
			Log.error( "Cluedo-common", "Error deserializing SuggestionMessage:", e );
		}

		return null;
	}

}
