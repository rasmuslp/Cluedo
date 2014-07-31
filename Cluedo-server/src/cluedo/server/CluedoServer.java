package cluedo.server;

import glhf.common.entity.EntityList;
import glhf.common.entity.list.IntegerList;
import glhf.common.entity.list.StringList;
import glhf.common.message.common.ChatMessage;
import glhf.common.player.Player;
import glhf.server.GlhfServer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import cluedo.common.cards.Card;
import cluedo.common.cards.CaseFile;
import cluedo.common.cards.ThreeCardPack;
import cluedo.common.definition.Definition;
import cluedo.common.definition.DefinitionException;
import cluedo.common.message.CluedoMessage;
import cluedo.common.message.CluedoMessageParser;
import cluedo.common.message.client.AccusationMessage;
import cluedo.common.message.client.SuggestionMessage;
import cluedo.common.message.client.TurnEndMessage;
import cluedo.common.message.common.DisproveMessage;
import cluedo.common.message.server.DefinitionMessage;
import cluedo.common.message.server.DisproveRequestMessage;
import cluedo.common.message.server.HandCardMessage;
import cluedo.common.message.server.StartingMessage;
import cluedo.common.message.server.TurnStartMessage;
import cluedo.server.definition.DefinitionManager;
import crossnet.Connection;
import crossnet.listener.ConnectionListenerAdapter;
import crossnet.log.Log;
import crossnet.message.Message;

public class CluedoServer {

	private final GlhfServer glhfServer = new GlhfServer();

	private final int noPlayers;

	private List< String > definitionText;
	private Definition definition;

	private boolean gameRunning = false;

	private TreeMap< Integer, ServerCluedoPlayer > cluedoPlayers = new TreeMap<>();
	private CaseFile caseFile;

	public CluedoServer( final int port, final int noPlayers, final Path definitionPath ) throws IOException {
		this.noPlayers = noPlayers;

		if ( definitionPath == null ) {
			// Load default Definition
			DefinitionManager definitionManager = new DefinitionManager();
			String defaultDefinition = "Default";

			try {
				this.definitionText = definitionManager.getDefinitionText( defaultDefinition );
				this.definition = definitionManager.getDefinition( defaultDefinition );
			} catch ( DefinitionException e ) {
				Log.error( "Cluedo-server", "Could not load default definition '" + defaultDefinition + "'", e );
			}
		} else {
			String name = DefinitionManager.pathToDefinitionName( definitionPath );
			try {
				this.definitionText = DefinitionManager.readDefinitionText( definitionPath );
				this.definition = DefinitionManager.parseDefinitionFromText( name, this.definitionText );
			} catch ( DefinitionException e ) {
				Log.error( "Cluedo-server", "Could not load definition '" + name + "'", e );
			}
		}

		if ( this.definition == null ) {
			Log.error( "Cluedo-server", "No defintion, no play" );
			//TODO DIE by Exception.
			System.exit( -1 );
		}

		this.glhfServer.getMessageParser().setTieredMessageParser( new CluedoMessageParser() );

		this.glhfServer.addConnectionListener( new ConnectionListenerAdapter() {

			@Override
			public void connected( Connection connection ) {
				if ( CluedoServer.this.glhfServer.getPlayers().size() > CluedoServer.this.noPlayers ) {
					// Kick
					connection.send( new ChatMessage( "Game full." ) );
					connection.close();
				} else {
					// Allow join
					StringList definitionList = new StringList();
					for ( String line : CluedoServer.this.definitionText ) {
						definitionList.add( line );
					}
					connection.send( new DefinitionMessage( definitionList ) );
					CluedoServer.this.cluedoPlayers.put( connection.getID(), new ServerCluedoPlayer() );
				}

			}

			@Override
			public void disconnected( Connection connection ) {
				// Override this if necessary.
			}

			@Override
			public void received( Connection connection, Message message ) {
				if ( !( message instanceof CluedoMessage ) ) {
					return;
				}

				switch ( ( (CluedoMessage) message ).getCluedoType() ) {

				// Server Messages
					case S_DEFINITION:
					case S_STARTING:
					case S_HAND_CARD:
					case S_TURN_START:
					case S_DISPROVE_REQ:
						Log.warn( "Cluedo-server", "Got unexpected Message Type: " + message.getMessageClass() );
						break;

					// Client Messages
					case C_SUGGESTION:
					case DISPROVE:
					case C_ACCUSATION:
					case C_TURN_END:
						try {
							CluedoServer.this.cluedoPlayers.get( connection.getID() ).incommingMessages.put( message );
						} catch ( InterruptedException e ) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;

					default:
						Log.warn( "Cluedo-server", "Got unknown Message Type: " + ( (CluedoMessage) message ).getCluedoType() );
						break;

				}
			}
		} );

		this.glhfServer.bind( port );
	}

	public void loop() {
		Player winningPlayer = null;

		// Index of current player.
		Integer currentPlayerID = -1;

		Player currentPlayer = null;
		TurnState turnState = null;

		//TODO: Move
		Integer disproverID = null;
		ThreeCardPack threeCardPack = null;

		boolean gameOver = false;

		while ( !gameOver ) {
			// Stay a while, and listen.

			if ( !this.gameRunning ) {
				if ( this.allReady() ) {
					IntegerList list = new IntegerList();
					for ( Integer id : this.cluedoPlayers.keySet() ) {
						list.add( id );
					}
					this.glhfServer.sendToAll( new StartingMessage( list ) );
					this.prepareGame();
					this.gameRunning = true;
				}
			} else {

				// --- Game loop start

				if ( currentPlayer == null ) {
					/// Determine next player.

					// Counts passive cluedoPlayers to detect wrap around.
					int passivePlayerCount = 0;

					// Find next active player, if any.
					do {
						if ( passivePlayerCount == this.cluedoPlayers.size() ) {
							// No active cluedoPlayers left.
							currentPlayer = null;
							break;
						}

						currentPlayerID = this.cluedoPlayers.higherKey( currentPlayerID );
						if ( currentPlayerID == null ) {
							currentPlayerID = this.cluedoPlayers.firstKey();
						}
						currentPlayer = this.glhfServer.getPlayers().get( currentPlayerID );
						if ( this.cluedoPlayers.get( currentPlayerID ).isActive() ) {
							// Found active player.
							break;
						}

						passivePlayerCount++;
					} while ( true );

					if ( currentPlayer == null ) {
						gameOver = true;
						break;
					}

					/// CluedoPlayer turn begins.
					Log.info( "Cluedo-server", "It is " + currentPlayer.getName() + "'s turn (ID: " + currentPlayer.getID() + ")" );
					this.glhfServer.sendToAll( new TurnStartMessage( currentPlayer.getID() ) );

					turnState = TurnState.SUGGESTION;
				}

				switch ( turnState ) {
					case SUGGESTION:
						try {
							Message message = this.cluedoPlayers.get( currentPlayerID ).incommingMessages.take();
							if ( message instanceof SuggestionMessage ) {
								threeCardPack = ( (SuggestionMessage) message ).getSuggestion();
								disproverID = this.getNextToDisprove( currentPlayerID, currentPlayerID );
								this.glhfServer.getConnections().get( disproverID ).send( new DisproveRequestMessage( threeCardPack ) );
								turnState = TurnState.DISPROVE;
							} else {
								Log.error( "Cluedo-server", "Did not get SuggestionMessage when expecting it..." + message.getClass().getSimpleName() );
							}
						} catch ( InterruptedException e ) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case DISPROVE:
						try {
							Message message = this.cluedoPlayers.get( disproverID ).incommingMessages.take();
							if ( message instanceof DisproveMessage ) {
								DisproveMessage disproveMessage = (DisproveMessage) message;
								if ( disproveMessage.disproved() ) {
									// Notify current player
									//TODO: Seriously hax !
									this.glhfServer.getConnections().get( currentPlayerID ).send( disproveMessage );

									// Turn ends
									currentPlayer = null;
								} else {
									disproverID = this.getNextToDisprove( disproverID, currentPlayerID );
									if ( disproverID == null ) {
										// No one could disprove
										Log.info( "Cluedo-server", "No one could disprove " + currentPlayer.getName() + "'s suggestion. (ID: " + currentPlayerID + ")" );

										// Notify current player
										//TODO: Seriously hax !
										this.glhfServer.getConnections().get( currentPlayerID ).send( new DisproveMessage( new EntityList< Card >() ) );
										turnState = TurnState.ACCUSATION;
									} else {
										this.glhfServer.getConnections().get( disproverID ).send( new DisproveRequestMessage( threeCardPack ) );
									}
								}
							} else {
								Log.error( "Cluedo-server", "Did not get DisproveMessage when expecting it..." + message.getClass().getSimpleName() );
							}
						} catch ( InterruptedException e ) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case ACCUSATION:
						try {
							Message message = this.cluedoPlayers.get( currentPlayerID ).incommingMessages.take();
							if ( message instanceof TurnEndMessage ) {
								currentPlayer = null;
							} else if ( message instanceof AccusationMessage ) {
								Log.info( "Cluedo-server", currentPlayer.getName() + " goes for it and makes an accusation. (ID: " + currentPlayerID + ")" );
								ThreeCardPack accusation = ( (AccusationMessage) message ).getAccusation();

								if ( this.caseFile.tryAccusation( accusation ) ) {
									// Player solved the murder.
									Log.info( "Cluedo-server", currentPlayer.getName() + " made the right call. (ID: " + currentPlayerID + ")" );
									winningPlayer = currentPlayer;
									gameOver = true;
								} else {
									// Player looses.
									Log.info( "Cluedo-server", currentPlayer.getName() + " made the wrong call and is now a passive player. (ID: " + currentPlayerID + ")" );
									this.cluedoPlayers.get( currentPlayerID ).setPassive();

									// Turn ends
									currentPlayer = null;
								}
							} else {
								Log.error( "Cluedo-server", "Did not get TurnEndMessage/AccusationMessage when expecting it..." + message.getClass().getSimpleName() );
							}
						} catch ( InterruptedException e ) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;

					default:
						Log.error( "Cluedo-server", "Unknown turn state. Aborting" );
						//TODO: Die with exception ?
						System.exit( -1 );

				}

				// --- Game loop end

			}

			try {
				Thread.sleep( 100 );
			} catch ( InterruptedException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Announce end of game.
		if ( winningPlayer != null ) {
			Log.info( "Cluedo-server", "The murder was solved by " + winningPlayer.getName() + " ! (ID: " + currentPlayerID + ")" );
		} else {
			Log.info( "Cluedo-server", "No one was able to solve the murder." );
		}
		Log.info( "Cluedo-server", this.caseFile.toString() );

		System.exit( -1 );
	}

	private boolean allReady() {
		if ( this.noPlayers != this.glhfServer.getPlayers().size() ) {
			return false;
		}

		for ( Player player : this.glhfServer.getPlayers().values() ) {
			if ( !player.isReady() ) {
				return false;
			}
		}

		return true;
	}

	private void prepareGame() {
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
		Integer currentID = -1;
		while ( deck.size() > 0 ) {
			Card card = deck.remove( 0 );
			currentID = this.cluedoPlayers.higherKey( currentID );
			if ( currentID == null ) {
				currentID = this.cluedoPlayers.firstKey();
			}
			Connection connection = this.glhfServer.getConnections().get( currentID );
			connection.send( new HandCardMessage( card ) );
		}

//		for ( Integer id : this.cluedoPlayers.keySet() ) {
//			Log.info( "Cluedo-server", this.glhfServer.getPlayers().get( id ).getName() + " got " + this.cluedoPlayers.get( id ).getNoCards() + " cards." );
//		}
	}

	private Integer getNextToDisprove( int lastDisproverID, int currentPlayerID ) {
		Integer nextDisproverID = this.cluedoPlayers.higherKey( lastDisproverID );
		if ( nextDisproverID == null ) {
			nextDisproverID = this.cluedoPlayers.firstKey();
		}
		if ( nextDisproverID == currentPlayerID ) {
			// Wrapped around. No more cluedoPlayers to ask.
			return null;
		}
		return nextDisproverID;
	}
}
