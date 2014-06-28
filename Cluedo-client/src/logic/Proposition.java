package logic;

public class Proposition implements LogicEntity {

	private final String name;
	private final boolean value;

	public Proposition( final String name, final boolean value ) {
		this.name = name;
		this.value = value;
	}

	@Override
	public boolean isValid() {
		return this.value;
	}

	@Override
	public int getDepthToEnd() {
		return 0;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
