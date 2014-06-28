package logic;

public class Or extends AbstractPairLogicEntity {

	protected Or( LogicEntity logicEntity1, LogicEntity logicEntity2 ) {
		super( logicEntity1, logicEntity2 );
	}

	@Override
	public boolean isValid() {
		return this.logicEntity1.isValid() || this.logicEntity2.isValid();
	}

	@Override
	public String toString() {
		return "(" + this.logicEntity1 + " or " + this.logicEntity2 + ")";
	}

}
