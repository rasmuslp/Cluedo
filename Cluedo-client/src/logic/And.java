package logic;

public class And extends AbstractPairLogicEntity {

	public And( final LogicEntity logicEntity1, final LogicEntity logicEntity2 ) {
		super( logicEntity1, logicEntity2 );
	}

	@Override
	public boolean isValid() {
		//TODO: Optimise based on depth ?
		return this.logicEntity1.isValid() && this.logicEntity2.isValid();
	}

	@Override
	public String toString() {
		return "(" + this.logicEntity1 + " and " + this.logicEntity2 + ")";
	}

}
