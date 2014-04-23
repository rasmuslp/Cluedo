package game;

import game.cards.Card;
import game.cards.CaseFile;
import game.cards.ThreeCardPack;
import game.definition.Definition;
import game.player.AiPlayer;
import game.player.HumanPlayer;
import game.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class Game {

	private final Definition definition;

	private final ArrayList< Player > players = new ArrayList<>();

	private CaseFile caseFile;

	public Game( final Definition definition, final int numberOfPlayers, final boolean humanParticipation ) {
		this.definition = definition;

		if ( humanParticipation ) {
			this.players.add( new HumanPlayer( this.definition ) );
		}
		while ( this.players.size() < numberOfPlayers ) {
			this.players.add( new AiPlayer( this.definition, "AI Player, no. " + ( this.players.size() + 1 ) ) );
		}
	}

	public void prepareGame() {
		// Get cards from definition
		List< Card > characterCards = this.definition.getCharacterCards();
		List< Card > roomCards = this.definition.getRoomCards();
		List< Card > weaponCards = this.definition.getWeaponCards();

		//TODO: There needs to be a check somewhere that there is at least X of each type of card

		/// Determine contents of Case File

		// Find random indices of Cards
		Random rng = new Random();
		int characterIndex = rng.nextInt( characterCards.size() );
		int roomIndex = rng.nextInt( roomCards.size() );
		int weaponIndex = rng.nextInt( weaponCards.size() );

		// Make Case File
		Card characterCard = characterCards.remove( characterIndex );
		Card roomCard = roomCards.remove( roomIndex );
		Card weaponCard = weaponCards.remove( weaponIndex );
		this.caseFile = new CaseFile( characterCard, roomCard, weaponCard );

		/// Deck of remaining cards
		List< Card > deck = new ArrayList<>();
		deck.addAll( characterCards );
		deck.addAll( roomCards );
		deck.addAll( weaponCards );
		Collections.shuffle( deck, rng );

		// Hand out cards to players
		int playerIndex = 0;
		for ( int i = 0; i < deck.size(); i++ ) {
			Card card = deck.get( i );
			this.players.get( playerIndex ).handCard( card );
			playerIndex = ( playerIndex + 1 ) % this.players.size();
		}

		for ( Player player : this.players ) {
			System.out.println( player.getName() + " got " + player.getNoCards() + " cards." );
		}

//		Tile[][] board = this.definition.getBoard().getBoard();
//		Board.printBoard( board );
//		Board.printNoOfNeighbours( board );
//		Board.printNoOfAssociatedRooms( board );

	}

	public void gameLoop() {
		Player winningPlayer = null;

		// Index of current player.
		int currentPlayerNo = -1;

		boolean gameOver = false;
		while ( !gameOver ) {
			/// Determine next player.

			// Counts passive players to detect wrap around.
			int passivePlayerCount = 0;

			// Find next active player, if any.
			Player currentPlayer;
			do {
				if ( passivePlayerCount == this.players.size() ) {
					// No active players left.
					currentPlayer = null;
					break;
				}

				currentPlayerNo = ( currentPlayerNo + 1 ) % this.players.size();
				currentPlayer = this.players.get( currentPlayerNo );
				if ( currentPlayer.isActive() ) {
					// Found active player.
					break;
				}

				passivePlayerCount++;
			} while ( true );

			if ( currentPlayer == null ) {
				gameOver = true;
				break;
			}

			/// Player turn begins.
			System.out.println( currentPlayer.getName() + " it's your turn." );

			// Player must make a suggestion.
			ThreeCardPack suggestion = currentPlayer.makeSuggestion();

			// Disprove suggestion loop.
			boolean noDisporve = false;
			int nextPlayerNo = currentPlayerNo;
			while ( true ) {
				nextPlayerNo = ( nextPlayerNo + 1 ) % this.players.size();
				Player nextPlayer = this.players.get( nextPlayerNo );
				if ( currentPlayer == nextPlayer ) {
					// Wrapped around. No more players to ask.
					noDisporve = true;
					break;
				}
				Card card = nextPlayer.disproveSuggestion( suggestion );
				if ( card != null ) {
					currentPlayer.showCard( card );
					break;
				}
			}

			// If no one disproves the suggestion, the player may make an accusation.
			if ( noDisporve ) {
				System.out.println( "No one disproved " + currentPlayer.getName() + "'s suggestions." );
				ThreeCardPack accusation = currentPlayer.makeAccusation();
				if ( accusation == null ) {
					// Player made no accusation
					continue;
				}
				System.out.println( currentPlayer.getName() + " goes for it and makes an accusation." );
				if ( this.caseFile.tryAccusation( accusation ) ) {
					// Player solved the murder.
					System.out.println( currentPlayer.getName() + " made the right call." );
					winningPlayer = currentPlayer;
					gameOver = true;
				} else {
					// Player looses.
					System.out.println( currentPlayer.getName() + " made the wrong call and is now a passive player." );
					currentPlayer.setPassive();
				}
			}
		}

		// Announce end of game.
		if ( winningPlayer != null ) {
			System.out.println( "The murder was solved by " + winningPlayer.getName() + "!" );
		} else {
			System.out.println( "No one was able to solve the murder." );
		}
		System.out.println( this.caseFile );
	}
}
