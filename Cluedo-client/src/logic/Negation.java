package logic;

public class Negation extends AbstractSingleLogicEntity {

	public Negation( final LogicEntity logicEntity ) {
		super( logicEntity );
	}

	@Override
	public boolean isValid() {
		return !this.logicEntity.isValid();
	}

	@Override
	public String toString() {
		return "!" + this.logicEntity;
	}

}
