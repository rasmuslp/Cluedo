package cluedo.common.message.server;

import cluedo.common.cards.Card;
import cluedo.common.message.CluedoEntityMessage;
import cluedo.common.message.CluedoMessageType;

public class HandCardMessage extends CluedoEntityMessage< Card > {

	public HandCardMessage( Card card ) {
		super( CluedoMessageType.S_HAND_CARD, card );
	}

	public Card getCard() {
		return this.getEntity();
	}

}
