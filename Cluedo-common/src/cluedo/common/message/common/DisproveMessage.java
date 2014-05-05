package cluedo.common.message.common;

import java.io.IOException;
import java.util.List;

import cluedo.common.cards.Card;
import cluedo.common.message.CluedoEntityListMessage;
import cluedo.common.message.CluedoMessageType;
import crossnet.util.ByteArrayWriter;

public class DisproveMessage extends CluedoEntityListMessage< Card > {

	public DisproveMessage( List< Card > list ) {
		super( CluedoMessageType.DISPROVE, list );
	}

	public boolean disproved() {
		if ( this.list.size() == 0 ) {
			return false;
		}

		return true;
	}

	@Override
	protected void serializeStatic( ByteArrayWriter to ) throws IOException {
		// No static information to serialise.
	}

	@Override
	protected void serializeListObject( int atIndex, ByteArrayWriter to ) throws IOException {
		this.list.get( atIndex ).serialise( to );
	}

}
