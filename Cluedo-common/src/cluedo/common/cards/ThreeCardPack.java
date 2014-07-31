package cluedo.common.cards;

import glhf.common.entity.Entity;

import java.io.IOException;

import cluedo.common.cards.Card.CardType;
import cluedo.common.definition.Definition;
import crossnet.util.ByteArrayReader;
import crossnet.util.ByteArrayWriter;

/**
 * This is used for suggestions and accusations, as both consists of three cards, one of each type.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class ThreeCardPack implements Entity {

	private final Card characterCard;
	private final Card roomCard;
	private final Card weaponCard;

	/**
	 * A pack of three cards of different type.
	 * 
	 * @param characterCard
	 *            The character card. This must be of {@link Card.CardType#CHARACTER}.
	 * @param roomCard
	 *            The room card. This must be of {@link Card.CardType#ROOM}.
	 * @param weaponCard
	 *            The weapon card. This must be of {@link Card.CardType#WEAPON}.
	 * @throws IllegalArgumentException
	 */
	public ThreeCardPack( final Card characterCard, final Card roomCard, final Card weaponCard ) {
		if ( characterCard.getType() != CardType.CHARACTER ) {
			//TODO Log
			throw new IllegalArgumentException( "Character card must be of type 'CHARACTER'." );
		}

		if ( roomCard.getType() != CardType.ROOM ) {
			//TODO Log
			throw new IllegalArgumentException( "Room card must be of type 'ROOM'." );
		}

		if ( weaponCard.getType() != CardType.WEAPON ) {
			//TODO Log
			throw new IllegalArgumentException( "Weapon card must be of type 'WEAPON'." );
		}

		this.characterCard = characterCard;
		this.roomCard = roomCard;
		this.weaponCard = weaponCard;
	}

	public Card getCharacterCard() {
		return this.characterCard;
	}

	public Card getRoomCard() {
		return this.roomCard;
	}

	public Card getWeaponCard() {
		return this.weaponCard;
	}

	@Override
	public String toString() {
		String out = "It was " + this.getCharacterCard().getTitle() + " (" + this.getCharacterCard().getID() + ")";
		out += " with the " + this.getWeaponCard().getTitle() + " (" + this.getWeaponCard().getID() + ")";
		out += " in the " + this.getRoomCard().getTitle() + " (" + this.getRoomCard().getID() + ").";
		return out;
	}

	@Override
	public void serialise( ByteArrayWriter to ) throws IOException {
		to.writeString255( this.characterCard.getID() );
		to.writeString255( this.roomCard.getID() );
		to.writeString255( this.weaponCard.getID() );
	}

	/**
	 * Construct an ThreeCardPack from the provided payload.
	 * 
	 * @param payload
	 *            The payload from which to determine the content of this.
	 * @return A freshly parsed ThreeCardPack.
	 * @throws IOException
	 *             In case of parse errors.
	 */
	public static ThreeCardPack parse( ByteArrayReader payload ) throws IOException {
		String characterCardID = payload.readString255();
		String roomCardID = payload.readString255();
		String weaponCardID = payload.readString255();

		Card characterCard = Definition.definition.getAllCards().get( characterCardID );
		Card roomCard = Definition.definition.getAllCards().get( roomCardID );
		Card weaponCard = Definition.definition.getAllCards().get( weaponCardID );

		return new ThreeCardPack( characterCard, roomCard, weaponCard );
	}

}
