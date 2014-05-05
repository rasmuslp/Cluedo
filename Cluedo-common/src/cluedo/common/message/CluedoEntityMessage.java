package cluedo.common.message;

import glhf.common.entity.Entity;

import java.io.IOException;

import crossnet.util.ByteArrayWriter;

/**
 * Abstract Message that can transport an Entity.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 * @param <T>
 *            The type of Entity.
 */
public abstract class CluedoEntityMessage< T extends Entity > extends CluedoMessage {

	/**
	 * The Entity.
	 */
	private final T entity;

	/**
	 * Create a new CluedoEntityMessage of provided type and Entity.
	 * 
	 * @param cluedoMessageType
	 *            The type of CluedoMessage.
	 * @param entity
	 *            The Entity.
	 */
	public CluedoEntityMessage( CluedoMessageType cluedoMessageType, T entity ) {
		super( cluedoMessageType );
		this.entity = entity;
	}

	/**
	 * @return The Entity.
	 */
	public T getEntity() {
		return this.entity;
	}

	@Override
	protected void serializeCluedoPayload( ByteArrayWriter to ) throws IOException {
		this.entity.serialise( to );
	}

}
