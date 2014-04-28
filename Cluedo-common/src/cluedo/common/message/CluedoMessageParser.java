package cluedo.common.message;

import cluedo.common.message.client.AccusationMessage;
import cluedo.common.message.client.DisproveMessage;
import cluedo.common.message.client.SuggestionMessage;
import cluedo.common.message.server.DefinitionMessage;
import cluedo.common.message.server.DisproveRequestMessage;
import cluedo.common.message.server.HandCardMessage;
import cluedo.common.message.server.StartingMessage;
import cluedo.common.message.server.TurnStartMessage;
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
			case S_HAND_CARD:
				message = HandCardMessage.parse( payload );
				break;
			case S_TURN_START:
				message = TurnStartMessage.parse( payload );
				break;
			case S_DISPROVE_REQ:
				message = DisproveRequestMessage.parse( payload );
				break;

			// Client Messages
			case C_SUGGESTION:
				message = SuggestionMessage.parse( payload );
				break;
			case C_DISPROVE:
				message = DisproveMessage.parse( payload );
				break;
			case C_ACCUSATION:
				message = AccusationMessage.parse( payload );
				break;

			default:
				Log.error( "Cluedo-common", "Unknown CluedoMessageType, cannot parse: " + messageType );
				break;
		}

		return message;
	}

}
