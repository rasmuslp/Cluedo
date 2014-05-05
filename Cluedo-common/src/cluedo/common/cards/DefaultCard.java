package cluedo.common.cards;

import java.io.IOException;

import crossnet.util.ByteArrayWriter;

/**
 * Default implementation of a {@link Card}.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class DefaultCard implements Card {

	private final CardType type;
	private final String ID;
	private final String title;

	public DefaultCard( final CardType type, final String ID, final String title ) {
		this.type = type;
		this.ID = ID;
		this.title = title;
	}

	@Override
	public final CardType getType() {
		return this.type;
	}

	@Override
	public final String getID() {
		return this.ID;
	}

	@Override
	public final String getTitle() {
		return this.title;
	}

	@Override
	public final String toString() {
		return "Card. Type: " + this.type + " ID: " + this.ID + " Title: " + this.title;
	}

	@Override
	public int compareTo( Card other ) {
		return this.getID().compareTo( other.getID() );
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.ID.hashCode();
		result = prime * result + this.title.hashCode();
		result = prime * result + this.type.hashCode();
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj ) {
			return true;
		}

		if ( obj == null ) {
			return false;
		}

		if ( !( obj instanceof DefaultCard ) ) {
			return false;
		}

		DefaultCard other = (DefaultCard) obj;

		if ( !this.ID.equals( other.ID ) ) {
			return false;
		}

		if ( !this.title.equals( other.title ) ) {
			return false;
		}

		if ( this.type != other.type ) {
			return false;
		}

		return true;
	}

	@Override
	public void serialise( ByteArrayWriter to ) throws IOException {
		to.writeString255( this.ID );
	}

}
