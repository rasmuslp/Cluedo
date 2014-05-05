package cluedo.common.message.client;

import cluedo.common.cards.ThreeCardPack;
import cluedo.common.message.CluedoEntityMessage;
import cluedo.common.message.CluedoMessageType;

public class AccusationMessage extends CluedoEntityMessage< ThreeCardPack > {

	public AccusationMessage( ThreeCardPack threeCardPack ) {
		super( CluedoMessageType.C_ACCUSATION, threeCardPack );
	}

	public ThreeCardPack getAccusation() {
		return this.getEntity();
	}

}
