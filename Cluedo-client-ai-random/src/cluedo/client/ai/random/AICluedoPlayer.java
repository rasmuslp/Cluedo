package cluedo.client.ai.random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import cluedo.client.CluedoPlayer;
import cluedo.common.cards.Card;
import cluedo.common.cards.ThreeCardPack;
import cluedo.common.definition.Definition;
import crossnet.log.Log;

/**
 * A player of the game.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class AICluedoPlayer extends CluedoPlayer {

	private final List< Card > cardsSeen = new ArrayList<>();

	/**
	 * The last suggestion made.
	 */
	private ThreeCardPack lastSuggestion;

	public AICluedoPlayer( final String name ) {
		super( name );
	}

	@Override
	public ThreeCardPack makeSuggestion() {
		Log.info( "Cluedo-player", "You may make a suggestion." );

		// Filter known cards.
		List< Card > possibleCharacterCards = this.filterCardsSeenAndOnHand( Definition.definition.getCharacterCards() );
		List< Card > possibleRoomCards = this.filterCardsSeenAndOnHand( Definition.definition.getRoomCards() );
		List< Card > possibleWeaponCards = this.filterCardsSeenAndOnHand( Definition.definition.getWeaponCards() );

		this.printCardsOnHand();
		this.printCardsSeen();

		Log.info( "Cluedo-player", " - Unknown cards:" );
		CluedoPlayer.printCards( possibleCharacterCards, possibleRoomCards, possibleWeaponCards );

		// Choose cards.
		Random rng = new Random();
		Card character = possibleCharacterCards.get( rng.nextInt( possibleCharacterCards.size() ) );
		Card room = possibleRoomCards.get( rng.nextInt( possibleRoomCards.size() ) );
		Card weapon = possibleWeaponCards.get( rng.nextInt( possibleWeaponCards.size() ) );

		Log.info( "Cluedo-player", " * Choosen cards: " + character.getID() + " " + room.getID() + " " + weapon.getID() );

		this.lastSuggestion = new ThreeCardPack( character, room, weapon );

		return this.lastSuggestion;
	}

	@Override
	public Card disproveSuggestionChoose( final List< Card > gotCards ) {
		Random rng = new Random();
		Card card = gotCards.get( rng.nextInt( gotCards.size() ) );

		return card;
	}

	@Override
	public void showCard( final Card card ) {
		Log.info( "Cluedo-player", " * A player had the card " + card.getID() );
		if ( !this.cardsSeen.contains( card ) ) {
			this.cardsSeen.add( card );
		}
	}

	@Override
	public ThreeCardPack makeAccusation() {
		Log.info( "Cluedo-player", "You may make an accusation." );

		// Filter known cards.
		List< Card > possibleCharacterCards = this.filterCardsSeenAndOnHand( Definition.definition.getCharacterCards() );
		List< Card > possibleRoomCards = this.filterCardsSeenAndOnHand( Definition.definition.getRoomCards() );
		List< Card > possibleWeaponCards = this.filterCardsSeenAndOnHand( Definition.definition.getWeaponCards() );

		Log.info( "Cluedo-player", " - After subtracting seen cards, this is the unknown cards:" );
		CluedoPlayer.printCards( possibleCharacterCards, possibleRoomCards, possibleWeaponCards );

		if ( ( possibleCharacterCards.size() > 1 ) || ( possibleRoomCards.size() > 1 ) || ( possibleWeaponCards.size() > 1 ) ) {
			// Still unknowns
			Log.info( "Cluedo-player", "'I'm not even sure, but this must be right'" );
		}

		// Since this AI doesn't bluff, the last suggestion must be the solution.
		Log.info( "Cluedo-player", " * Choosen cards: " + this.lastSuggestion.getCharacterCard().getID() + " " + this.lastSuggestion.getRoomCard().getID() + " "
				+ this.lastSuggestion.getWeaponCard().getID() );
		return this.lastSuggestion;
	}

	private void printCardsSeen() {
		String out = " - Seen cards:";

		if ( this.cardsSeen.size() == 0 ) {
			out += " None";
		} else {
			List< Card > hand = new ArrayList<>( this.cardsSeen );
			Collections.sort( hand );

			for ( Card card : hand ) {
				out += " " + card.getID();
			}
		}

		Log.info( "Cluedo-player", out );
	}

	private List< Card > filterCardsSeenAndOnHand( List< Card > cards ) {
		List< Card > filteredCards = cards;
		filteredCards.removeAll( this.cardsSeen );
		filteredCards.removeAll( this.cardsOnHand.values() );
		return filteredCards;
	}

}
