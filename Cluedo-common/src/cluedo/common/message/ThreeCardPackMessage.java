package cluedo.common.message;

import java.io.IOException;

import cluedo.common.cards.ThreeCardPack;
import crossnet.util.ByteArrayWriter;

public abstract class ThreeCardPackMessage extends CluedoMessage {

	private final ThreeCardPack threeCardPack;

	protected ThreeCardPackMessage( CluedoMessageType cluedoMessageType, ThreeCardPack threeCardPack ) {
		super( cluedoMessageType );
		this.threeCardPack = threeCardPack;
	}

	public ThreeCardPack getThreeCardPack() {
		return this.threeCardPack;
	}

	@Override
	protected void serializeCluedoPayload( ByteArrayWriter to ) throws IOException {
		this.threeCardPack.serialize( to );
	}

}
