package cluedo.common.message.common;

import glhf.common.entity.EntityList;
import cluedo.common.cards.Card;
import cluedo.common.message.CluedoEntityMessage;
import cluedo.common.message.CluedoMessageType;

public class DisproveMessage extends CluedoEntityMessage< EntityList< Card > > {

	public DisproveMessage( EntityList< Card > list ) {
		super( CluedoMessageType.DISPROVE, list );
	}

	public boolean disproved() {
		if ( this.getEntity().size() == 0 ) {
			return false;
		}

		return true;
	}

}
