package cluedo.common.message.server;

import glhf.common.entity.list.StringList;
import cluedo.common.message.CluedoEntityMessage;
import cluedo.common.message.CluedoMessageType;

public class DefinitionMessage extends CluedoEntityMessage< StringList > {

	public DefinitionMessage( StringList text ) {
		super( CluedoMessageType.S_DEFINITION, text );
	}

}
