package cluedo.common.message;

import java.util.List;

import crossnet.message.AbstractListMessage;

public abstract class CluedoListMessage< T > extends AbstractListMessage< T, CluedoMessageType > {

	public CluedoListMessage( CluedoMessageType messageType, List< T > list ) {
		super( messageType, list );
	}

}
