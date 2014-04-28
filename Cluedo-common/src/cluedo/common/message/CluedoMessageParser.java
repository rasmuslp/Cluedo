package cluedo.common.message;

import cluedo.common.message.server.DefinitionMessage;
import cluedo.common.message.server.StartingMessage;
import crossnet.log.Log;
import crossnet.message.AbstractMessageParser;
import crossnet.message.Message;
import crossnet.util.ByteArrayReader;

public class CluedoMessageParser extends AbstractMessageParser< CluedoMessageType > {

	public CluedoMessageParser() {
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

			case S_STARTING:
				message = StartingMessage.parse( payload );
				break;

			// Client Messages

			default:
				Log.error( "Cluedo-common", "Unknown CluedoMessageType, cannot parse: " + messageType );
				break;
		}

		return message;
	}

}
