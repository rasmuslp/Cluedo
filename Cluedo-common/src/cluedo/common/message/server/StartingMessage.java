package cluedo.common.message.server;

import glhf.common.entity.single.IntegerEntity;

import java.io.IOException;
import java.util.List;

import cluedo.common.message.CluedoEntityListMessage;
import cluedo.common.message.CluedoMessageType;
import crossnet.util.ByteArrayWriter;

public class StartingMessage extends CluedoEntityListMessage< IntegerEntity > {

	public StartingMessage( List< IntegerEntity > list ) {
		super( CluedoMessageType.S_STARTING, list );
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
