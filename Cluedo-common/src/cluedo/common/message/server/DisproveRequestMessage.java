package cluedo.common.message.server;

import cluedo.common.cards.ThreeCardPack;
import cluedo.common.message.CluedoEntityMessage;
import cluedo.common.message.CluedoMessageType;

public class DisproveRequestMessage extends CluedoEntityMessage< ThreeCardPack > {

	public DisproveRequestMessage( ThreeCardPack threeCardPack ) {
		super( CluedoMessageType.S_DISPROVE_REQ, threeCardPack );
	}

	public ThreeCardPack getSuggestion() {
		return this.getEntity();
	}

}
