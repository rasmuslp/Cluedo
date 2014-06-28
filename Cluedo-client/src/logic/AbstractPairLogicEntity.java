package logic;

public abstract class AbstractPairLogicEntity implements LogicEntity {

	protected final LogicEntity logicEntity1;
	protected final LogicEntity logicEntity2;

	protected AbstractPairLogicEntity( final LogicEntity logicEntity1, final LogicEntity logicEntity2 ) {
		this.logicEntity1 = logicEntity1;
		this.logicEntity2 = logicEntity2;
	}

	@Override
	public int getDepthToEnd() {
		return 1 + Math.max( this.logicEntity1.getDepthToEnd(), this.logicEntity2.getDepthToEnd() );
	}

}
