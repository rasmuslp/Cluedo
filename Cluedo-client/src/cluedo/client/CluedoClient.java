package cluedo.client;

import glhf.client.GlhfClient;
import glhf.common.message.GlhfListMessage;
import glhf.common.message.GlhfMessage;
import glhf.common.player.Player;
import glhf.common.player.PlayerListener;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import cluedo.common.cards.Card;
import cluedo.common.cards.ThreeCardPack;
import cluedo.common.definition.Definition;
import cluedo.common.definition.DefinitionException;
import cluedo.common.definition.DefinitionParser;
import cluedo.common.message.CluedoMessageParser;
import cluedo.common.message.client.AccusationMessage;
import cluedo.common.message.client.DisproveMessage;
import cluedo.common.message.client.SuggestionMessage;
import cluedo.common.message.client.TurnEndMessage;
import cluedo.common.message.server.DefinitionMessage;
import cluedo.common.message.server.DisproveRequestMessage;
import cluedo.common.message.server.HandCardMessage;
import cluedo.common.message.server.StartingMessage;
import cluedo.common.message.server.TurnStartMessage;
import crossnet.Connection;
import crossnet.listener.ConnectionListenerAdapter;
import crossnet.log.Log;
import crossnet.message.Message;
import crossnet.message.crossnet.CrossNetMessage;

public class CluedoClient {

	private GlhfClient glhfClient = new GlhfClient();

	private CluedoPlayer cluedoPlayer = new CluedoPlayer();

	private Definition definition;

	public CluedoClient( final int port, final String host, final String name ) throws UnknownHostException, IOException {
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
				CluedoClient.this.glhfClient.setName( name );
			}

			@Override
			public void disconnected( Connection connection ) {
				// Client has been disconnected from the server.
			}

			@Override
			public void received( Connection connection, Message message ) {

				if ( message instanceof DefinitionMessage ) {
					DefinitionMessage definitionMessage = (DefinitionMessage) message;

					// Parse Definition
					try {
						CluedoClient.this.definition = DefinitionParser.parseDefinitionFromText( "Server provided definition", definitionMessage.getList() );
					} catch ( DefinitionException e ) {
						Log.error( "Cluedo-client", "Could not parse definition from server.", e );
						//TODO DIE by Exception.
						System.exit( -1 );
					}

					// Set ready
					CluedoClient.this.glhfClient.setReady( true );
				} else if ( message instanceof StartingMessage ) {
					StartingMessage startingMessage = (StartingMessage) message;
					Log.info( "Cluedo-client", "Game starting. Player order:" );
					for ( int id : startingMessage.getList() ) {
						Player player = CluedoClient.this.glhfClient.getPlayers().get( id );

						Log.info( "Cluedo-client", player.getName() + " with ID: " + id );
					}
				} else if ( message instanceof HandCardMessage ) {
					HandCardMessage hardCardMessage = (HandCardMessage) message;
					Log.info( "Cluedo-client", "Got a card: " + hardCardMessage.getCard().getID() );
					CluedoClient.this.cluedoPlayer.handCard( hardCardMessage.getCard() );
				} else if ( message instanceof TurnStartMessage ) {
					TurnStartMessage turnStartMessage = (TurnStartMessage) message;
					if ( turnStartMessage.getID() == connection.getID() ) {
						// Make suggestion
						ThreeCardPack threeCardPack = CluedoClient.this.cluedoPlayer.makeSuggestion();
						CluedoClient.this.glhfClient.send( new SuggestionMessage( threeCardPack ) );
					}
				} else if ( message instanceof DisproveRequestMessage ) {
					// Disprove another players suggestion.
					ThreeCardPack threeCardPack = ( (DisproveRequestMessage) message ).getThreeCardPack();
					Card card = CluedoClient.this.cluedoPlayer.disproveSuggestion( threeCardPack );
					ArrayList< Card > cards = new ArrayList<>();
					if ( card != null ) {
						cards.add( card );

					}
					CluedoClient.this.glhfClient.send( new DisproveMessage( cards ) );
				} else if ( message instanceof DisproveMessage ) {
					DisproveMessage disproveMessage = (DisproveMessage) message;
					if ( disproveMessage.disproved() ) {
						// Other Player disproved the suggestion.
						Card card = disproveMessage.getList().get( 0 );
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
				} else if ( !( message instanceof GlhfMessage || message instanceof GlhfListMessage || message instanceof CrossNetMessage ) ) {
					Log.warn( "Cluedo-client", "Got unexpected Message Type: " + message.getClass().getSimpleName() );
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
