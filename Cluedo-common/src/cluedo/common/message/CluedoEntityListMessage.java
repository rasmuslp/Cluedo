package cluedo.common.message;

import glhf.common.entity.Entity;

import java.io.IOException;
import java.util.List;

import crossnet.util.ByteArrayWriter;

/**
 * Abstract Message that can transport a list of Entities.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 * @param <T>
 *            The type of Entity in the list.
 */
public abstract class CluedoEntityListMessage< T extends Entity > extends CluedoMessage {

	/**
	 * The Entity list.
	 */
	protected final List< T > list;

	/**
	 * Create a new CluedoEntityListMessage of provided type and list of Entities.
	 * 
	 * @param messageType
	 *            The type of CluedoMessage.
	 * @param list
	 *            The list of Entities.
	 */
	public CluedoEntityListMessage( CluedoMessageType messageType, List< T > list ) {
		super( messageType );
		if ( list == null ) {
			throw new IllegalArgumentException( "List cannot be null." );
		}
		this.list = list;
	}

	/**
	 * @return The Entity list.
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
