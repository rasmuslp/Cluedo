package logic;

public class LogicTest {

	public static void main( String[] args ) throws InterruptedException {
		Proposition p1 = new Proposition( "p1", true );
		Proposition p2 = new Proposition( "p2", false );
		Proposition p3 = new Proposition( "p3", true );
		LogicEntity statement = new And( p1, new Negation( new Or( p2, new Negation( p3 ) ) ) );

		System.out.println( statement );
		System.out.println( "Valid: " + statement.isValid() );
	}
}
