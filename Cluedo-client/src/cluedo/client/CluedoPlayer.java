package cluedo.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cluedo.common.cards.Card;
import cluedo.common.cards.ThreeCardPack;
import cluedo.common.definition.Definition;

/**
 * A player of the game.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class CluedoPlayer {

	private final List< Card > cardsSeen = new ArrayList<>();

	/**
	 * The last suggestion made.
	 */
	private ThreeCardPack lastSuggestion;

	/**
	 * Name of this player.
	 */
	protected String name = "";

	/**
	 * Denotes whether this player is an active part of the game.
	 * 
	 * {@code False} iff this player has lost the game by making a false accusation.
	 */
	private boolean active = true;

	/**
	 * The cards on this players hand.
	 */
	protected Map< String, Card > cardsOnHand = new HashMap<>();

	/**
	 * Gets the name of this player.
	 * 
	 * @return The name of this player.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets this player as not active.
	 */
	public void setPassive() {
		this.active = false;
	}

	/**
	 * Checks whether this player is active.
	 * 
	 * @return {@code True} iff this player is active. {@code False} otherwise.
	 */
	public boolean isActive() {
		return this.active;
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
	 * Gets the number of cards on this players hand.
	 * 
	 * @return
	 */
	public int getNoCards() {
		return this.cardsOnHand.size();
	}

	public void printCardsOnHand() {
		List< Card > hand = new ArrayList<>( this.cardsOnHand.values() );
		Collections.sort( hand );
		String out = this.getName() + " Hand:";
		for ( Card card : hand ) {
			out += " " + card.getID();
		}
		System.out.println( out );
	}

	/**
	 * This player must make a suggestion.
	 * 
	 * @return The suggestion.
	 */
	public ThreeCardPack makeSuggestion() {
		System.out.println( this.getName() + " you may make a suggestion." );

		// Filter known cards.
		List< Card > possibleCharacterCards = this.filterCardsSeenAndOnHand( Definition.definition.getCharacterCards() );
		List< Card > possibleRoomCards = this.filterCardsSeenAndOnHand( Definition.definition.getRoomCards() );
		List< Card > possibleWeaponCards = this.filterCardsSeenAndOnHand( Definition.definition.getWeaponCards() );

		this.printCardsOnHand();
		this.printCardsSeen();

		System.out.println( this.getName() + " this is the unknown cards:" );
		CluedoPlayer.printCards( possibleCharacterCards, possibleRoomCards, possibleWeaponCards );

		// Choose cards.
		Random rng = new Random();
		Card character = possibleCharacterCards.get( rng.nextInt( possibleCharacterCards.size() ) );
		Card room = possibleRoomCards.get( rng.nextInt( possibleRoomCards.size() ) );
		Card weapon = possibleWeaponCards.get( rng.nextInt( possibleWeaponCards.size() ) );

		System.out.println( "Choosen cards: " + character.getID() + " " + room.getID() + " " + weapon.getID() );

		this.lastSuggestion = new ThreeCardPack( character, room, weapon );

		return this.lastSuggestion;
	}

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

		System.out.println( this.getName() + ": You are asked if you got any of the following cards: " + characterCard.getID() + " " + roomCard.getID() + " " + weaponCard.getID() );

		boolean gotCharacterCard = this.cardsOnHand.containsKey( characterCard.getID() );
		boolean gotRoomCard = this.cardsOnHand.containsKey( roomCard.getID() );
		boolean gotWeaponCard = this.cardsOnHand.containsKey( weaponCard.getID() );

		List< Card > gotCards = new ArrayList<>();
		int gotNoCards = 0;
		if ( gotCharacterCard ) {
			System.out.println( this.getName() + ": You have the Character card " + characterCard.getID() );
			gotCards.add( characterCard );
			gotNoCards++;
		}
		if ( gotRoomCard ) {
			System.out.println( this.getName() + ": You have the Room card " + roomCard.getID() );
			gotCards.add( roomCard );
			gotNoCards++;
		}
		if ( gotWeaponCard ) {
			System.out.println( this.getName() + ": You have the Weapon card " + weaponCard.getID() );
			gotCards.add( weaponCard );
			gotNoCards++;
		}

		Card choosenCard = null;
		if ( gotNoCards == 0 ) {
			System.out.println( this.getName() + ": You have none of these." );
		} else if ( gotNoCards == 1 ) {
			if ( gotCharacterCard ) {
				choosenCard = characterCard;
			} else if ( gotRoomCard ) {
				choosenCard = roomCard;
			} else if ( gotWeaponCard ) {
				choosenCard = weaponCard;
			}
			System.out.println( this.getName() + ": You have this: " + choosenCard.getID() );
		} else {
			// 1+ cards
			choosenCard = this.disproveSuggestionChoose( gotCards );
		}

		return choosenCard;
	}

	//TODO: Give the three cards asked for ?
	public Card disproveSuggestionChoose( final List< Card > gotCards ) {
		Random rng = new Random();
		Card card = gotCards.get( rng.nextInt( gotCards.size() ) );
		System.out.println( this.getName() + " choose to show " + card.getID() );

		return card;
	}

	//TODO: The player must know who shows the card.
	/**
	 * Show a card to this player.
	 * 
	 * @param card
	 *            The card to show this player.
	 */
	public void showCard( final Card card ) {
		System.out.println( this.getName() + ": A player had the card " + card.getID() );
		if ( !this.cardsSeen.contains( card ) ) {
			this.cardsSeen.add( card );
		}
	}

	/**
	 * This player may make an accusation.
	 * 
	 * @return The accusation. {@code null} if no accusation is made.
	 */
	public ThreeCardPack makeAccusation() {
		System.out.println( this.getName() + " you may make an accusation." );

		// Filter known cards.
		List< Card > possibleCharacterCards = this.filterCardsSeenAndOnHand( Definition.definition.getCharacterCards() );
		List< Card > possibleRoomCards = this.filterCardsSeenAndOnHand( Definition.definition.getRoomCards() );
		List< Card > possibleWeaponCards = this.filterCardsSeenAndOnHand( Definition.definition.getWeaponCards() );

		System.out.println( this.getName() + " after subtracting seen cards, this is the unknown cards:" );
		CluedoPlayer.printCards( possibleCharacterCards, possibleRoomCards, possibleWeaponCards );

		if ( possibleCharacterCards.size() > 1 || possibleRoomCards.size() > 1 || possibleWeaponCards.size() > 1 ) {
			// Still unknowns
			System.out.println( this.getName() + " 'I'm not even sure, but this must be right'" );
		}

		// Since this AI doesn't bluff, the last suggestion must be the solution.
		return this.lastSuggestion;
	}

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
		String out = "Characters:";
		for ( Card card : characterCards ) {
			out += " " + card.getID();
		}
		out += "\n";

		out += "Rooms:";
		for ( Card card : roomCards ) {
			out += " " + card.getID();
		}
		out += "\n";

		out += "Weapons:";
		for ( Card card : weaponCards ) {
			out += " " + card.getID();
		}

		System.out.println( out );
	}

	private void printCardsSeen() {
		List< Card > hand = new ArrayList<>( this.cardsSeen );
		Collections.sort( hand );
		String out = this.getName() + " Seen cards:";
		for ( Card card : hand ) {
			out += " " + card.getID();
		}
		System.out.println( out );
	}

	private List< Card > filterCardsSeenAndOnHand( List< Card > cards ) {
		List< Card > filteredCards = cards;
		filteredCards.removeAll( this.cardsSeen );
		filteredCards.removeAll( this.cardsOnHand.values() );
		return filteredCards;
	}

}
