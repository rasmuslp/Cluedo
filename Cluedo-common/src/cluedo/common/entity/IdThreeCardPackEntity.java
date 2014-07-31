package cluedo.common.entity;

import glhf.common.entity.IdSingleEntity;

import java.io.IOException;

import cluedo.common.cards.ThreeCardPack;
import crossnet.util.ByteArrayWriter;

/**
 * This is an ID - ThreeCardPack wrapper.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class IdThreeCardPackEntity extends IdSingleEntity< ThreeCardPack > {

	/**
	 * Create an ID - ThreeCardPack tuple.
	 * 
	 * @param id
	 *            The ID to wrap.
	 * @param threeCardPack
	 *            The ThreeCardPack to wrap.
	 */
	public IdThreeCardPackEntity( final int id, final ThreeCardPack threeCardPack ) {
		super( id, threeCardPack );
	}

	@Override
	public void serialise( ByteArrayWriter to ) throws IOException {
		this.id.serialise( to );
		this.entity.serialise( to );
	}

}
