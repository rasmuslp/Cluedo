package cluedo.common.message;

import crossnet.message.AbstractMessage;

public abstract class CluedoMessage extends AbstractMessage< CluedoMessageType > {

	public CluedoMessage( CluedoMessageType messageType ) {
		super( messageType );
	}

}
