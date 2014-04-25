package cluedo.server;

import cluedo.common.message.CluedoMessageType;
import crossnet.log.Log;
import crossnet.message.AbstractMessageParser;
import crossnet.message.Message;
import crossnet.util.ByteArrayReader;

public class ServerMessageParser extends AbstractMessageParser< CluedoMessageType > {

	public ServerMessageParser() {
		super( CluedoMessageType.class );
	}

	@Override
	protected Message parseType( CluedoMessageType messageType, ByteArrayReader payload ) {
		Message message = null;

		switch ( messageType ) {
		// Common Messages

		// Server Messages
			case S_DEFINITION:
				Log.error( "Cluedo-server", "Server received Server CluedoMessageType. This makes no sense. Type was: " + messageType );
				break;

			// Client Messages
			case C_DUMMY:
				//message =
				break;

			default:
				Log.error( "Cluedo-server", "Unknown CluedoMessageType, cannot parse: " + messageType );
				break;
		}

		return message;
	}

}
