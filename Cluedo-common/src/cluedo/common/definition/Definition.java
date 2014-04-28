package cluedo.common.definition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cluedo.common.board.Board;
import cluedo.common.cards.Card;

/**
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class Definition {

	//TODO: Consider the location of this
	public static Definition definition;

	private final String name;
	private final int minNoPlayers;
	private final int maxNoPlayers;
	private final Map< String, Card > allCards = new TreeMap<>();
	private final List< Card > characterCards;
	private final List< Card > roomCards;
	private final List< Card > weaponCards;
	private final Board board;

	public Definition( final String name, final int minNoPlayers, final int maxNoPlayers, final List< Card > characterCards, final List< Card > roomCards, final List< Card > weaponCards,
			final Board board ) {
		this.name = name;
		this.minNoPlayers = minNoPlayers;
		this.maxNoPlayers = maxNoPlayers;
		this.characterCards = characterCards;
		this.roomCards = roomCards;
		this.weaponCards = weaponCards;
		this.board = board;

		Collections.sort( this.characterCards );
		Collections.sort( this.roomCards );
		Collections.sort( this.weaponCards );

		for ( Card card : this.characterCards ) {
			this.allCards.put( card.getID(), card );
		}
		for ( Card card : this.roomCards ) {
			this.allCards.put( card.getID(), card );
		}
		for ( Card card : this.weaponCards ) {
			this.allCards.put( card.getID(), card );
		}
	}

	public String getName() {
		return this.name;
	}

	public int getMinimumNumberOfPlayers() {
		return this.minNoPlayers;
	}

	public int getMaximumNumberOfPlayers() {
		return this.maxNoPlayers;
	}

	public Map< String, Card > getAllCards() {
		return new TreeMap<>( this.allCards );
	}

	public List< Card > getCharacterCards() {
		return new ArrayList<>( this.characterCards );
	}

	public List< Card > getRoomCards() {
		return new ArrayList<>( this.roomCards );
	}

	public List< Card > getWeaponCards() {
		return new ArrayList<>( this.weaponCards );
	}

	public Board getBoard() {
		return this.board;
	}
}
