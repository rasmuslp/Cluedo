package cluedo.common.message.server;

import glhf.common.entity.list.IntegerList;
import cluedo.common.message.CluedoEntityMessage;
import cluedo.common.message.CluedoMessageType;

public class StartingMessage extends CluedoEntityMessage< IntegerList > {

	public StartingMessage( IntegerList list ) {
		super( CluedoMessageType.S_STARTING, list );
	}

}
