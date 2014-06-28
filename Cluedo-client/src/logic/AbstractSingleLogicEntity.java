package logic;

public abstract class AbstractSingleLogicEntity implements LogicEntity {

	protected final LogicEntity logicEntity;

	protected AbstractSingleLogicEntity( final LogicEntity logicEntity ) {
		this.logicEntity = logicEntity;
	}

	@Override
	public int getDepthToEnd() {
		return 1 + this.logicEntity.getDepthToEnd();
	}

}
