package cluedo.common.message;

import glhf.common.message.common.TieredGlhfMessage;

import java.io.IOException;

import crossnet.util.ByteArrayWriter;

/**
 * Abstract Message that is used internally to maintain state.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public abstract class CluedoMessage extends TieredGlhfMessage {

	/**
	 * The type of Message.
	 */
	protected final CluedoMessageType cluedoMessageType;

	public CluedoMessage( CluedoMessageType cluedoMessageType ) {
		this.cluedoMessageType = cluedoMessageType;
	}

	@Override
	protected void serializeGlhfPayload( ByteArrayWriter to ) throws IOException {
		to.writeByte( this.cluedoMessageType.ordinal() );
		this.serializeCluedoPayload( to );
	}

	/**
	 * Serialises the payload of the CluedoMessage.
	 * 
	 * @param to
	 *            The destination of the serialisation.
	 * @throws IOException
	 *             If a serialisation error occurs.
	 */
	protected abstract void serializeCluedoPayload( ByteArrayWriter to ) throws IOException;

}
