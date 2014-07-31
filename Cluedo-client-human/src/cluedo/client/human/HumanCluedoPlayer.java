package cluedo.client.human;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import cluedo.client.CluedoPlayer;
import cluedo.common.cards.Card;
import cluedo.common.cards.ThreeCardPack;
import cluedo.common.definition.Definition;
import crossnet.log.Log;

public class HumanCluedoPlayer extends CluedoPlayer {

	public HumanCluedoPlayer( final String name ) {
		super( name );
	}

	@Override
	public ThreeCardPack makeSuggestion() {
		Log.info( "Cluedo-player", "You may make a suggestion." );

		this.printCardsOnHand();

		return this.chooseCards();
	}

	@Override
	public Card disproveSuggestionChoose( final List< Card > gotCards ) {
		Card choosenCard = null;

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
					Log.info( "Cluedo-player", " - Couldn't read ID. Try again." );
				}
			} catch ( IOException e ) {
				Log.info( "Cluedo-player", " - Couldn't read ID. Try again." );
				e.printStackTrace();
			}
		}

		return choosenCard;
	}

	@Override
	public void showCard( final Card card ) {
		Log.info( "Cluedo-player", " * A player had the card " + card.getID() );
	}

	@Override
	public ThreeCardPack makeAccusation() {
		Log.info( "Cluedo-player", "You may make an accusation." );

		this.printCardsOnHand();

		Log.info( "Cluedo-player", " - Would you like to make an accusation ? (y/n) [n]" );
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
				Log.info( "Cluedo-player", " - Must be 'y' or 'n'." );
			} catch ( IOException e ) {
				Log.info( "Cluedo-player", " - Must be 'y' or 'n'." );
			}
		}

		if ( !makeAccusation ) {
			return null;
		}

		return this.chooseCards();
	}

	/**
	 * Choose three cards, one of each type, from all possible cards of the {@link Definition}.
	 * 
	 * @return The three choosen cards.
	 */
	private ThreeCardPack chooseCards() {
		Log.info( "Cluedo-player", " - Write 3 space separated IDs:" );
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
					Log.info( "Cluedo-player", " - Couldn't read 3 IDs. Try again." );
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
						for ( Card card : Definition.definition.getCharacterCards() ) {
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
						for ( Card card : Definition.definition.getRoomCards() ) {
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
						for ( Card card : Definition.definition.getWeaponCards() ) {
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
					Log.info( "Cluedo-player", "Couldn't read 3 IDs. Try again." );
				} else {
					break;
				}
			} catch ( IOException e ) {
				Log.info( "Cluedo-player", "Couldn't read 3 IDs. Try again." );
				e.printStackTrace();
			}
		}

		Log.info( "Cluedo-player", " * Choosen cards: " + character.getID() + " " + room.getID() + " " + weapon.getID() );

		return new ThreeCardPack( character, room, weapon );
	}

}
