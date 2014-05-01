package cluedo.common.message;

import java.io.IOException;
import java.util.List;

import crossnet.util.ByteArrayWriter;

/**
 * Abstract Message that is used internally to maintain state. Has a List as a part of the payload.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 * @param <T>
 *            The list type.
 */
public abstract class CluedoListMessage< T > extends CluedoMessage {

	/**
	 * The list payload.
	 */
	protected final List< T > list;

	public CluedoListMessage( CluedoMessageType messageType, List< T > list ) {
		super( messageType );
		if ( list == null ) {
			throw new IllegalArgumentException( "List cannot be null." );
		}
		this.list = list;
	}

	/**
	 * @return The list payload.
	 */
	public List< T > getList() {
		return this.list;
	}

	@Override
	protected void serializeCluedoPayload( ByteArrayWriter to ) throws IOException {
		this.serializeStatic( to );
		int count = this.list.size();
		to.writeInt( count );
		for ( int i = 0; i < count; i++ ) {
			this.serializeListObject( i, to );
		}
	}

	/**
	 * Serialises any extra non-list information necessary.
	 * 
	 * @param to
	 *            The destination of the serialisation.
	 * @throws IOException
	 *             If a serialisation error occurs.
	 */
	protected abstract void serializeStatic( ByteArrayWriter to ) throws IOException;

	/**
	 * Serialises the specific object of the {@link #list}.
	 * 
	 * @param atIndex
	 *            An index of the Object to serialise.
	 * @param to
	 *            The destination of the serialisation.
	 * @throws IOException
	 *             If a serialisation error occurs.
	 */
	protected abstract void serializeListObject( int atIndex, ByteArrayWriter to ) throws IOException;

}
