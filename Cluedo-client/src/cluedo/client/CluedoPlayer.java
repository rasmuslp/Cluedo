package cluedo.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cluedo.common.cards.Card;
import cluedo.common.cards.ThreeCardPack;
import crossnet.log.Log;

/**
 * A player of the game.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public abstract class CluedoPlayer {

	/**
	 * Name of this player.
	 */
	protected final String name;

	/**
	 * The cards on this players hand.
	 */
	protected Map< String, Card > cardsOnHand = new HashMap<>();

	protected CluedoPlayer( final String name ) {
		this.name = name;
	}

	/**
	 * Gets the name of this player.
	 * 
	 * @return The name of this player.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Give this player a card.
	 * 
	 * @param card
	 *            The card for this player.
	 */
	public void handCard( Card card ) {
		this.cardsOnHand.put( card.getID(), card );
	}

	/**
	 * @return The number of cards on this players hand.
	 */
	public int getNoCards() {
		return this.cardsOnHand.size();
	}

	public void printCardsOnHand() {
		List< Card > hand = new ArrayList<>( this.cardsOnHand.values() );
		Collections.sort( hand );
		String out = " - Cards on hand:";
		for ( Card card : hand ) {
			out += " " + card.getID();
		}
		Log.info( "Cluedo-player", out );
	}

	/**
	 * This player must make a suggestion.
	 * 
	 * @return The suggestion.
	 */
	public abstract ThreeCardPack makeSuggestion();

	/**
	 * This player must try to disprove the suggestion.
	 * 
	 * @param suggestion
	 *            The suggestion to disprove.
	 * @return A card that disproves the suggestion. {@code null} if this player has no such card.
	 */
	public Card disproveSuggestion( final ThreeCardPack suggestion ) {
		Card characterCard = suggestion.getCharacterCard();
		Card roomCard = suggestion.getRoomCard();
		Card weaponCard = suggestion.getWeaponCard();

		Log.info( "Cluedo-player", "You are asked if you have got any of the following cards: " + characterCard.getID() + " " + roomCard.getID() + " " + weaponCard.getID() );

		boolean gotCharacterCard = this.cardsOnHand.containsKey( characterCard.getID() );
		boolean gotRoomCard = this.cardsOnHand.containsKey( roomCard.getID() );
		boolean gotWeaponCard = this.cardsOnHand.containsKey( weaponCard.getID() );

		List< Card > gotCards = new ArrayList<>();
		int gotNoCards = 0;
		if ( gotCharacterCard ) {
			Log.info( "Cluedo-player", " - You have the Character card " + characterCard.getID() );
			gotCards.add( characterCard );
			gotNoCards++;
		}
		if ( gotRoomCard ) {
			Log.info( "Cluedo-player", " - You have the Room card " + roomCard.getID() );
			gotCards.add( roomCard );
			gotNoCards++;
		}
		if ( gotWeaponCard ) {
			Log.info( "Cluedo-player", " - You have the Weapon card " + weaponCard.getID() );
			gotCards.add( weaponCard );
			gotNoCards++;
		}

		Card choosenCard = null;
		if ( gotNoCards == 0 ) {
			Log.info( "Cluedo-player", " * You have none of these." );
		} else if ( gotNoCards == 1 ) {
			if ( gotCharacterCard ) {
				choosenCard = characterCard;
			} else if ( gotRoomCard ) {
				choosenCard = roomCard;
			} else if ( gotWeaponCard ) {
				choosenCard = weaponCard;
			}
			Log.info( "Cluedo-player", " * You show the card: " + choosenCard.getID() );
		} else {
			// 1+ cards
			String out = " - You have more than one card. You must choose which card to show:";
			for ( Card card : gotCards ) {
				out += " " + card.getID();
			}
			Log.info( "Cluedo-player", out );

			choosenCard = this.disproveSuggestionChoose( gotCards );

			Log.info( "Cluedo-player", " * You choose to show " + choosenCard.getID() );
		}

		return choosenCard;
	}

	//TODO: Give the three cards asked for ?
	public abstract Card disproveSuggestionChoose( final List< Card > gotCards );

	//TODO: The player must know who shows the card.
	/**
	 * Show a card to this player.
	 * 
	 * @param card
	 *            The card to show this player.
	 */
	public abstract void showCard( final Card card );

	/**
	 * This player may make an accusation.
	 * 
	 * @return The accusation. {@code null} if no accusation is made.
	 */
	public abstract ThreeCardPack makeAccusation();

	/**
	 * Prints the cards from the three lists.
	 * 
	 * @param characterCards
	 *            The character cards to print.
	 * @param roomCards
	 *            The room cards to print.
	 * @param weaponCards
	 *            The weapon cards to print.
	 */
	protected static void printCards( final List< Card > characterCards, final List< Card > roomCards, final List< Card > weaponCards ) {
		String out = " - Characters:";
		for ( Card card : characterCards ) {
			out += " " + card.getID();
		}
		Log.info( "Cluedo-player", out );

		out = " - Rooms:";
		for ( Card card : roomCards ) {
			out += " " + card.getID();
		}
		Log.info( "Cluedo-player", out );

		out = " - Weapons:";
		for ( Card card : weaponCards ) {
			out += " " + card.getID();
		}
		Log.info( "Cluedo-player", out );
	}

}
