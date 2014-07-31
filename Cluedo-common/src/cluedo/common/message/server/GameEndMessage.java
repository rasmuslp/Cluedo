package cluedo.common.message.server;

import cluedo.common.cards.ThreeCardPack;
import cluedo.common.entity.IdThreeCardPackEntity;
import cluedo.common.message.CluedoEntityMessage;
import cluedo.common.message.CluedoMessageType;

public class GameEndMessage extends CluedoEntityMessage< IdThreeCardPackEntity > {

	public GameEndMessage( final int id, final ThreeCardPack threeCardPack ) {
		super( CluedoMessageType.S_GAME_END, new IdThreeCardPackEntity( id, threeCardPack ) );
	}

	/**
	 * @return The player ID of the winning player. 0 iff no winner.
	 */
	public int getID() {
		return this.getEntity().getId();
	}

	/**
	 * @return the CaseFile.
	 */
	public ThreeCardPack getCaseFile() {
		return this.getEntity().getEntity();
	}

}
