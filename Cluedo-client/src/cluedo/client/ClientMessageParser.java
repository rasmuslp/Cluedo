package cluedo.client;

import cluedo.common.message.CluedoMessageType;
import cluedo.common.message.server.DefinitionMessage;
import crossnet.log.Log;
import crossnet.message.AbstractMessageParser;
import crossnet.message.Message;
import crossnet.util.ByteArrayReader;

public class ClientMessageParser extends AbstractMessageParser< CluedoMessageType > {

	public ClientMessageParser() {
		super( CluedoMessageType.class );
	}

	@Override
	protected Message parseType( CluedoMessageType messageType, ByteArrayReader payload ) {
		Message message = null;

		switch ( messageType ) {
		// Common Messages

		// Server Messages
			case S_DEFINITION:
				message = DefinitionMessage.parse( payload );
				break;

			// Client Messages
			case C_DUMMY:
				Log.error( "Cluedo-client", "Client received Client CluedoMessageType. This makes no sense. Type was: " + messageType );
				break;

			default:
				Log.error( "Cluedo-client", "Unknown CluedoMessageType, cannot parse: " + messageType );
				break;
		}

		return message;
	}

}
