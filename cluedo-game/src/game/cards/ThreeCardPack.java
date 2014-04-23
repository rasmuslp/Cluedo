package game.cards;

import game.cards.Card.CardType;

/**
 * This is used for suggestions and accusations, as both consists of three cards, one of each type.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class ThreeCardPack {

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

}
