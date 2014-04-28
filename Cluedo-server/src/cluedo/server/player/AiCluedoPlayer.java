package cluedo.server.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import cluedo.common.cards.Card;
import cluedo.common.cards.ThreeCardPack;
import cluedo.common.definition.Definition;

public class AiCluedoPlayer extends CluedoPlayer {

	private final List< Card > cardsSeen = new ArrayList<>();

	/**
	 * The last suggestion made.
	 */
	private ThreeCardPack lastSuggestion;

	public AiCluedoPlayer( final Definition definition, final String name ) {
		super( definition );
		this.name = name;
	}

	private List< Card > filterCardsSeenAndOnHand( List< Card > cards ) {
		List< Card > filteredCards = cards;
		filteredCards.removeAll( this.cardsSeen );
		filteredCards.removeAll( this.cardsOnHand.values() );
		return filteredCards;
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

	@Override
	public ThreeCardPack makeSuggestion() {
		System.out.println( this.getName() + " you may make a suggestion." );

		// Filter known cards.
		List< Card > possibleCharacterCards = this.filterCardsSeenAndOnHand( this.definition.getCharacterCards() );
		List< Card > possibleRoomCards = this.filterCardsSeenAndOnHand( this.definition.getRoomCards() );
		List< Card > possibleWeaponCards = this.filterCardsSeenAndOnHand( this.definition.getWeaponCards() );

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

	@Override
	public Card disproveSuggestionChoose( final List< Card > gotCards ) {
		Random rng = new Random();
		Card card = gotCards.get( rng.nextInt( gotCards.size() ) );
		System.out.println( this.getName() + " choose to show " + card.getID() );

		return card;
	}

	@Override
	public void showCard( final Card card ) {
		System.out.println( this.getName() + ": A player had the card " + card.getID() );
		if ( !this.cardsSeen.contains( card ) ) {
			this.cardsSeen.add( card );
		}
	}

	@Override
	public ThreeCardPack makeAccusation() {
		System.out.println( this.getName() + " you may make an accusation." );

		// Filter known cards.
		List< Card > possibleCharacterCards = this.filterCardsSeenAndOnHand( this.definition.getCharacterCards() );
		List< Card > possibleRoomCards = this.filterCardsSeenAndOnHand( this.definition.getRoomCards() );
		List< Card > possibleWeaponCards = this.filterCardsSeenAndOnHand( this.definition.getWeaponCards() );

		System.out.println( this.getName() + " after subtracting seen cards, this is the unknown cards:" );
		CluedoPlayer.printCards( possibleCharacterCards, possibleRoomCards, possibleWeaponCards );

		if ( possibleCharacterCards.size() > 1 || possibleRoomCards.size() > 1 || possibleWeaponCards.size() > 1 ) {
			// Still unknowns
			System.out.println( this.getName() + " 'I'm not even sure, but this must be right'" );
		}

		// Since this AI doesn't bluff, the last suggestion must be the solution.
		return this.lastSuggestion;
	}

}
