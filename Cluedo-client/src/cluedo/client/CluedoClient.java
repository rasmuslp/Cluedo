package cluedo.client;

import glhf.client.GlhfClient;
import glhf.common.entity.EntityList;
import glhf.common.entity.single.IntegerEntity;
import glhf.common.entity.single.StringEntity;
import glhf.common.player.Player;
import glhf.common.player.PlayerListener;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import cluedo.common.cards.Card;
import cluedo.common.cards.ThreeCardPack;
import cluedo.common.definition.Definition;
import cluedo.common.definition.DefinitionException;
import cluedo.common.definition.DefinitionParser;
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
import crossnet.Connection;
import crossnet.listener.ConnectionListenerAdapter;
import crossnet.log.Log;
import crossnet.message.Message;

public class CluedoClient {

	private GlhfClient glhfClient = new GlhfClient();

	private CluedoPlayer cluedoPlayer;

	private Definition definition;

	public CluedoClient( final int port, final String host, final CluedoPlayer cluedoPlayer ) throws UnknownHostException, IOException {
		this.cluedoPlayer = cluedoPlayer;

		this.glhfClient.getMessageParser().setTieredMessageParser( new CluedoMessageParser() );

		this.glhfClient.addPlayerListener( new PlayerListener() {

			@Override
			public void updated( Player player ) {
				// TODO Auto-generated method stub

			}

			@Override
			public void disconnected( Player player ) {
				// TODO Auto-generated method stub

			}

			@Override
			public void connected( Player player ) {
				// TODO Auto-generated method stub

			}

			@Override
			public void chat( Player sender, String chat, Player receiver ) {
				// TODO Auto-generated method stub

			}
		} );

		this.glhfClient.addConnectionListener( new ConnectionListenerAdapter() {

			@Override
			public void connected( Connection connection ) {
				// Client connected to server.
				CluedoClient.this.glhfClient.setName( CluedoClient.this.cluedoPlayer.getName() );
			}

			@Override
			public void disconnected( Connection connection ) {
				// Client has been disconnected from the server.
			}

			@Override
			public void received( Connection connection, Message message ) {
				if ( !( message instanceof CluedoMessage ) ) {
					return;
				}

				switch ( ( (CluedoMessage) message ).getCluedoType() ) {
				// Server Messages

					case S_DEFINITION: {
						DefinitionMessage definitionMessage = (DefinitionMessage) message;

						// Parse Definition
						try {
							List< String > definitionText = new ArrayList<>();
							for ( StringEntity line : definitionMessage.getEntity() ) {
								definitionText.add( line.get() );
							}
							CluedoClient.this.definition = DefinitionParser.parseDefinitionFromText( "Server provided definition", definitionText );
						} catch ( DefinitionException e ) {
							Log.error( "Cluedo-client", "Could not parse definition from server.", e );
							//TODO DIE by Exception.
							System.exit( -1 );
						}

						// Set ready
						CluedoClient.this.glhfClient.setReady( true );
						break;
					}

					case S_STARTING: {
						StartingMessage startingMessage = (StartingMessage) message;
						Log.info( "Cluedo-client", "Game starting. Player order:" );
						for ( IntegerEntity integerEntity : startingMessage.getEntity() ) {
							int id = integerEntity.get();
							Player player = CluedoClient.this.glhfClient.getPlayers().get( id );
							Log.info( "Cluedo-client", player.getName() + " with ID: " + id );
						}
						break;
					}

					case S_HAND_CARD: {
						HandCardMessage hardCardMessage = (HandCardMessage) message;
						Log.info( "Cluedo-client", "Got a card: " + hardCardMessage.getCard().getID() );
						CluedoClient.this.cluedoPlayer.handCard( hardCardMessage.getCard() );
						break;
					}

					case S_TURN_START: {
						TurnStartMessage turnStartMessage = (TurnStartMessage) message;
						if ( turnStartMessage.getID() == connection.getID() ) {
							// Make suggestion
							ThreeCardPack threeCardPack = CluedoClient.this.cluedoPlayer.makeSuggestion();
							CluedoClient.this.glhfClient.send( new SuggestionMessage( threeCardPack ) );
						}
						break;
					}

					case S_DISPROVE_REQ: {
						// Disprove another players suggestion.
						ThreeCardPack threeCardPack = ( (DisproveRequestMessage) message ).getSuggestion();
						Card card = CluedoClient.this.cluedoPlayer.disproveSuggestion( threeCardPack );
						EntityList< Card > cards = new EntityList<>();
						if ( card != null ) {
							cards.add( card );

						}
						CluedoClient.this.glhfClient.send( new DisproveMessage( cards ) );
						break;
					}

					// Client Messages

					case C_SUGGESTION:
					case C_ACCUSATION:
					case C_TURN_END:
						Log.warn( "Cluedo-client", "Got unexpected Message Type: " + message.getMessageClass() );
						break;

					// Common Messages

					case DISPROVE: {
						DisproveMessage disproveMessage = (DisproveMessage) message;
						if ( disproveMessage.disproved() ) {
							// Other Player disproved the suggestion.
							Card card = disproveMessage.getEntity().get( 0 );
							CluedoClient.this.cluedoPlayer.showCard( card );
						} else {
							// May make accusation or end turn.
							ThreeCardPack threeCardPack = CluedoClient.this.cluedoPlayer.makeAccusation();
							if ( threeCardPack == null ) {
								CluedoClient.this.glhfClient.send( new TurnEndMessage() );
							} else {
								CluedoClient.this.glhfClient.send( new AccusationMessage( threeCardPack ) );
							}
						}
						break;
					}

					default:
						Log.warn( "Cluedo-client", "Got unknown Message Type: " + ( (CluedoMessage) message ).getCluedoType() );
						break;
				}
			}
		} );

		this.glhfClient.connect( InetAddress.getByName( host ), port );
	}

	public void loop() {
		while ( true ) {
			// Stay a while, and listen.

			try {
				Thread.sleep( 100 );
			} catch ( InterruptedException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
