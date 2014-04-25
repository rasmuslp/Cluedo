package cluedo.client;

import glhf.client.Client;
import glhf.common.player.Player;
import glhf.common.player.PlayerListener;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import cluedo.common.message.server.DefinitionMessage;
import crossnet.Connection;
import crossnet.listener.ConnectionListenerAdapter;
import crossnet.message.Message;

public class CluedoClient {

	private Client client = new Client();

	public CluedoClient( final int port, final String host, final String name ) throws UnknownHostException, IOException {
		//this.client.getMessageParser().setTieredMessageParser( new ClientMessageParser() );

		this.client.addPlayerListener( new PlayerListener() {

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

		this.client.addConnectionListener( new ConnectionListenerAdapter() {

			@Override
			public void connected( Connection connection ) {
				// Client connected to server.
				CluedoClient.this.client.setName( name );
			}

			@Override
			public void disconnected( Connection connection ) {
				// Client has been disconnected from the server.
			}

			@Override
			public void received( Connection connection, Message message ) {

				if ( message instanceof DefinitionMessage ) {
					System.out.println( "Got DEFFELEFFER!" );
				}

			}
		} );

		this.client.connect( InetAddress.getByName( host ), port );
	}

	public void loop() {
		while ( true ) {
			// Stay a while, and listen.
		}
	}
}
