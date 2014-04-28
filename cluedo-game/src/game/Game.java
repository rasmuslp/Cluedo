package game;

import game.cards.Card;
import game.cards.CaseFile;
import game.cards.ThreeCardPack;
import game.definition.Definition;
import game.player.AiCluedoPlayer;
import game.player.HumanCluedoPlayer;
import game.player.CluedoPlayer;

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

	private final ArrayList< CluedoPlayer > cluedoPlayers = new ArrayList<>();

	private CaseFile caseFile;

	public Game( final Definition definition, final int numberOfPlayers, final boolean humanParticipation ) {
		this.definition = definition;

		if ( humanParticipation ) {
			this.cluedoPlayers.add( new HumanCluedoPlayer( this.definition ) );
		}
		while ( this.cluedoPlayers.size() < numberOfPlayers ) {
			this.cluedoPlayers.add( new AiCluedoPlayer( this.definition, "AI CluedoPlayer, no. " + ( this.cluedoPlayers.size() + 1 ) ) );
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

		// Hand out cards to cluedoPlayers
		int playerIndex = 0;
		for ( int i = 0; i < deck.size(); i++ ) {
			Card card = deck.get( i );
			this.cluedoPlayers.get( playerIndex ).handCard( card );
			playerIndex = ( playerIndex + 1 ) % this.cluedoPlayers.size();
		}

		for ( CluedoPlayer cluedoPlayer : this.cluedoPlayers ) {
			System.out.println( cluedoPlayer.getName() + " got " + cluedoPlayer.getNoCards() + " cards." );
		}

//		Tile[][] board = this.definition.getBoard().getBoard();
//		Board.printBoard( board );
//		Board.printNoOfNeighbours( board );
//		Board.printNoOfAssociatedRooms( board );

	}

	public void gameLoop() {
		CluedoPlayer winningCluedoPlayer = null;

		// Index of current player.
		int currentPlayerNo = -1;

		boolean gameOver = false;
		while ( !gameOver ) {
			/// Determine next player.

			// Counts passive cluedoPlayers to detect wrap around.
			int passivePlayerCount = 0;

			// Find next active player, if any.
			CluedoPlayer currentCluedoPlayer;
			do {
				if ( passivePlayerCount == this.cluedoPlayers.size() ) {
					// No active cluedoPlayers left.
					currentCluedoPlayer = null;
					break;
				}

				currentPlayerNo = ( currentPlayerNo + 1 ) % this.cluedoPlayers.size();
				currentCluedoPlayer = this.cluedoPlayers.get( currentPlayerNo );
				if ( currentCluedoPlayer.isActive() ) {
					// Found active player.
					break;
				}

				passivePlayerCount++;
			} while ( true );

			if ( currentCluedoPlayer == null ) {
				gameOver = true;
				break;
			}

			/// CluedoPlayer turn begins.
			System.out.println( currentCluedoPlayer.getName() + " it's your turn." );

			// CluedoPlayer must make a suggestion.
			ThreeCardPack suggestion = currentCluedoPlayer.makeSuggestion();

			// Disprove suggestion loop.
			boolean noDisporve = false;
			int nextPlayerNo = currentPlayerNo;
			while ( true ) {
				nextPlayerNo = ( nextPlayerNo + 1 ) % this.cluedoPlayers.size();
				CluedoPlayer nextCluedoPlayer = this.cluedoPlayers.get( nextPlayerNo );
				if ( currentCluedoPlayer == nextCluedoPlayer ) {
					// Wrapped around. No more cluedoPlayers to ask.
					noDisporve = true;
					break;
				}
				Card card = nextCluedoPlayer.disproveSuggestion( suggestion );
				if ( card != null ) {
					currentCluedoPlayer.showCard( card );
					break;
				}
			}

			// If no one disproves the suggestion, the player may make an accusation.
			if ( noDisporve ) {
				System.out.println( "No one disproved " + currentCluedoPlayer.getName() + "'s suggestions." );
				ThreeCardPack accusation = currentCluedoPlayer.makeAccusation();
				if ( accusation == null ) {
					// CluedoPlayer made no accusation
					continue;
				}
				System.out.println( currentCluedoPlayer.getName() + " goes for it and makes an accusation." );
				if ( this.caseFile.tryAccusation( accusation ) ) {
					// CluedoPlayer solved the murder.
					System.out.println( currentCluedoPlayer.getName() + " made the right call." );
					winningCluedoPlayer = currentCluedoPlayer;
					gameOver = true;
				} else {
					// CluedoPlayer looses.
					System.out.println( currentCluedoPlayer.getName() + " made the wrong call and is now a passive player." );
					currentCluedoPlayer.setPassive();
				}
			}
		}

		// Announce end of game.
		if ( winningCluedoPlayer != null ) {
			System.out.println( "The murder was solved by " + winningCluedoPlayer.getName() + "!" );
		} else {
			System.out.println( "No one was able to solve the murder." );
		}
		System.out.println( this.caseFile );
	}
}
