package cluedo.common.message.server;

import glhf.common.entity.list.IntegerList;
import cluedo.common.message.CluedoEntityMessage;
import cluedo.common.message.CluedoMessageType;

public class GameStartMessage extends CluedoEntityMessage< IntegerList > {

	public GameStartMessage( IntegerList list ) {
		super( CluedoMessageType.S_GAME_START, list );
	}

}
