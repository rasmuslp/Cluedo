package cluedo.common.definition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cluedo.common.board.Board;
import cluedo.common.cards.Card;

/**
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class Definition {

	private final String name;
	private final List< Card > characterCards;
	private final List< Card > roomCards;
	private final List< Card > weaponCards;
	private final Board board;

	public Definition( final String name, final List< Card > characterCards, final List< Card > roomCards, final List< Card > weaponCards, final Board board ) {
		this.name = name;
		this.characterCards = characterCards;
		this.roomCards = roomCards;
		this.weaponCards = weaponCards;
		this.board = board;

		Collections.sort( this.characterCards );
		Collections.sort( this.roomCards );
		Collections.sort( this.weaponCards );
	}

	public String getName() {
		return this.name;
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
