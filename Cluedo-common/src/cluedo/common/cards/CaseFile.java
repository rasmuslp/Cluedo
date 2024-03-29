package cluedo.common.cards;

/**
 * The case file on the murder. Contains the solution to the game.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class CaseFile extends ThreeCardPack {

	private boolean solved = false;

	public CaseFile( Card characterCard, Card roomCard, Card weaponCard ) {
		super( characterCard, roomCard, weaponCard );
	}

	/**
	 * Compares the content of an accusation with the contents of this CaseFile.
	 * 
	 * @param threeCardPack
	 *            The accusation which to compare against this CaseFile.
	 * @return {@code True} iff the accusation is correct. {@code False} otherwise.
	 */
	public boolean tryAccusation( final ThreeCardPack threeCardPack ) {
		if ( this.getCharacterCard().equals( threeCardPack.getCharacterCard() ) && this.getRoomCard().equals( threeCardPack.getRoomCard() )
				&& this.getWeaponCard().equals( threeCardPack.getWeaponCard() ) ) {
			this.solved = true;
			return true;
		}

		return false;
	}

	@Override
	public String toString() {
		String out = "";
		if ( this.solved ) {
			out += super.toString();
		}

		return out;
	}
}
