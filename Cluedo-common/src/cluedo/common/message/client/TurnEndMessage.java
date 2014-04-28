package cluedo.common.message.client;

import java.io.IOException;

import cluedo.common.message.CluedoMessage;
import cluedo.common.message.CluedoMessageType;
import crossnet.util.ByteArrayWriter;

public class TurnEndMessage extends CluedoMessage {

	public TurnEndMessage() {
		super( CluedoMessageType.C_TURN_END );
	}

	@Override
	protected void serializePayload( ByteArrayWriter to ) throws IOException {
		// No information to serialise.
	}

}
