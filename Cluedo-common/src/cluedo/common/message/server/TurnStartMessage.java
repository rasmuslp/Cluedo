package cluedo.common.message.server;

import glhf.common.entity.single.IntegerEntity;
import cluedo.common.message.CluedoEntityMessage;
import cluedo.common.message.CluedoMessageType;

public class TurnStartMessage extends CluedoEntityMessage< IntegerEntity > {

	public TurnStartMessage( int id ) {
		super( CluedoMessageType.S_TURN_START, new IntegerEntity( id ) );
	}

	public int getID() {
		return this.getEntity().get();
	}

}