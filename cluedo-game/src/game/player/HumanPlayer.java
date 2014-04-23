package game.player;

import game.cards.Card;
import game.cards.ThreeCardPack;
import game.definition.Definition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class HumanPlayer extends Player {

	public HumanPlayer( final Definition definition ) {
		super( definition );
		BufferedReader in = new BufferedReader( new InputStreamReader( System.in ) );
		String defaultName = "Human Player";
		System.out.println( "Enter your player name: ['" + defaultName + "']" );
		String input = "";
		try {
			input = in.readLine();
		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if ( input.isEmpty() ) {
			input = defaultName;
		}

		this.name = input;
	}

	/**
	 * Choose three cards, one of each type, from all possible cards of the {@link Definition}.
	 * 
	 * @return The three choosen cards.
	 */
	private ThreeCardPack chooseCards() {
		System.out.println( "Write 3 space separated IDs:" );
		BufferedReader in = new BufferedReader( new InputStreamReader( System.in ) );

		Card character = null;
		Card room = null;
		Card weapon = null;

		while ( true ) {
			try {
				// Read.
				String input = in.readLine();

				// See if there were three space separated entries.
				String[] split = input.split( "\\s+" );
				if ( split.length != 3 ) {
					System.out.println( "Couldn't read 3 IDs. Try again." );
					continue;
				}
				for ( int i = 0; i < 3; i++ ) {
					split[i] = split[i].trim();
				}

				// Match entered data with possible cards.
				for ( int i = 0; i < 3; i++ ) {
					if ( character == null ) {
						if ( split[i] == null ) {
							continue;
						}
						for ( Card card : this.definition.getCharacterCards() ) {
							if ( card.getID().equalsIgnoreCase( split[i] ) ) {
								character = card;
								split[i] = null;
								break;
							}
						}
					}
					if ( room == null ) {
						if ( split[i] == null ) {
							continue;
						}
						for ( Card card : this.definition.getRoomCards() ) {
							if ( card.getID().equalsIgnoreCase( split[i] ) ) {
								room = card;
								split[i] = null;
								break;
							}
						}
					}
					if ( weapon == null ) {
						if ( split[i] == null ) {
							continue;
						}
						for ( Card card : this.definition.getWeaponCards() ) {
							if ( card.getID().equalsIgnoreCase( split[i] ) ) {
								weapon = card;
								split[i] = null;
								break;
							}
						}
					}
				}

				// Validates matches.
				if ( character == null || room == null || weapon == null ) {
					System.out.println( "Couldn't read 3 IDs. Try again." );
				} else {
					break;
				}
			} catch ( IOException e ) {
				System.out.println( "Couldn't read 3 IDs. Try again." );
				e.printStackTrace();
			}
		}

		System.out.println( "Choosen cards: " + character.getID() + " " + room.getID() + " " + weapon.getID() );

		return new ThreeCardPack( character, room, weapon );
	}

	@Override
	public ThreeCardPack makeSuggestion() {
		System.out.println( this.getName() + " you may make a suggestion." );

		this.printCardsOnHand();

		return this.chooseCards();
	}

	@Override
	public Card disproveSuggestionChoose( final List< Card > gotCards ) {
		Card choosenCard = null;

		String out = this.getName() + ": You have more than one card. You must choose which card to show:";
		for ( Card card : gotCards ) {
			out += " " + card.getID();
		}
		System.out.println( out );

		// Choose card
		BufferedReader in = new BufferedReader( new InputStreamReader( System.in ) );

		// Choose a character card
		boolean valid = false;
		while ( !valid ) {
			try {
				String input = in.readLine().trim();
				for ( Card card : gotCards ) {
					if ( card.getID().equalsIgnoreCase( input ) ) {
						choosenCard = card;
						valid = true;
						break;
					}
				}
				if ( !valid ) {
					System.out.println( "Couldn't read ID. Try again." );
				}
			} catch ( IOException e ) {
				System.out.println( "Couldn't read ID. Try again." );
				e.printStackTrace();
			}
		}

		return choosenCard;
	}

	@Override
	public void showCard( final Card card ) {
		System.out.println( this.getName() + ": A player had the card " + card.getID() );
	}

	@Override
	public ThreeCardPack makeAccusation() {
		System.out.println( this.getName() + " you may make am accusation." );

		this.printCardsOnHand();

		System.out.println( "Would you like to make an accusation ? (y/n) [n]" );
		BufferedReader in = new BufferedReader( new InputStreamReader( System.in ) );
		boolean makeAccusation = false;
		while ( true ) {
			try {
				String input = in.readLine();
				if ( input.isEmpty() ) {
					// Default
					makeAccusation = false;
					break;
				}
				if ( input.equalsIgnoreCase( "y" ) ) {
					makeAccusation = true;
					break;
				} else if ( input.equalsIgnoreCase( "n" ) ) {
					makeAccusation = false;
					break;
				}
				System.out.println( "Must be 'y' or 'n'." );
			} catch ( IOException e ) {
				System.out.println( "Must be 'y' or 'n'." );
			}
		}

		if ( !makeAccusation ) {
			return null;
		}

		return this.chooseCards();
	}
}
