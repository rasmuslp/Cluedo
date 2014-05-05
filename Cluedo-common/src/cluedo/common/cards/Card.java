package cluedo.common.cards;

import glhf.common.entity.Entity;
import cluedo.common.definition.Definition;

/**
 * A card.
 * 
 * NB: This must be immutable, as it is a part of a {@link Definition}, which is copied.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public interface Card extends Comparable< Card >, Entity {

	/**
	 * The possible types of cards.
	 * 
	 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
	 * 
	 */
	public enum CardType {
		/**
		 * Represents a character.
		 */
		CHARACTER,

		/**
		 * Represents a room.
		 */
		ROOM,

		/**
		 * Represents a weapon.
		 */
		WEAPON
	}

	/**
	 * Gets the type of this card.
	 * 
	 * @return The type of card.
	 */
	public CardType getType();

	/**
	 * Gets the ID of this card.
	 * 
	 * @return The ID of this card.
	 */
	public String getID();

	/**
	 * Gets the title of this card.
	 * 
	 * @return The title of this card.
	 */
	public String getTitle();

}
