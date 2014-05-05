package cluedo.common.message.client;

import cluedo.common.cards.ThreeCardPack;
import cluedo.common.message.CluedoEntityMessage;
import cluedo.common.message.CluedoMessageType;

public class SuggestionMessage extends CluedoEntityMessage< ThreeCardPack > {

	public SuggestionMessage( ThreeCardPack threeCardPack ) {
		super( CluedoMessageType.C_SUGGESTION, threeCardPack );
	}

	public ThreeCardPack getSuggestion() {
		return this.getEntity();
	}

}
