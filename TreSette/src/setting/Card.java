package setting;

public class Card implements Comparable<Card>
{	
	public enum Suit
	{
		COPPE(0), DENARI(10), BASTONI(20), SPADE(30);
		
		private int val;
		
		private Suit(int value) { this.val = val; }
		
		public int getVal() { return val; }
	}
	
	public enum Value
	{
		TRE(0, 1), DUE(1, 1), ASSO(2, 3), RE(3, 1), CAVALLO(4, 1), FANTE(5, 1), SETTE(6, 0), SEI(7, 0), CINQUE(8, 0), QUATTRO(9, 0);
		
		private int val;
		private int punti;
		
		private Value(int value, int point)
		{
			this.val = val;
			this.punti = point;
		}
		
		public int getVal() { return val; }
		
		public int getPunti() { return punti; }
	}
	

	private static final Suit[] intToSuit = {Suit.COPPE, Suit.DENARI, Suit.BASTONI, Suit.SPADE};
	private static final Value[] intToValue = {Value.TRE, Value.DUE, Value.ASSO, Value.RE, Value.CAVALLO, Value.FANTE, Value.SETTE,
			Value.SEI, Value.CINQUE, Value.QUATTRO};
	
	private Suit seme;
	private Value valore;
	
	public Card(Suit suit, Value value)
	{
		this.seme = suit;
		this.valore = value;
	}
	
	public Card(int intRepr)
	{
		if (intRepr < 0 || intRepr > 39) return;
		
		this.seme = intToSuit[intRepr / 10];
		this.valore = intToValue[intRepr % 10];
	}
	
	public Suit getSeme() { return seme; }
	
	public Value getValore() { return valore; }
	
	public int getPunti() { return valore.getPunti(); }
	
	public int toInt() { return seme.getVal() + valore.getVal(); }
	
	/**
	 * metodo temporaneo creato per comparare le carte: può e forse deve essere rifatto meglio.
	 * vieni utilizzato da Game per trovare il giocatore con il 4 di danari(ovvero il giocatore che inizia)
	 * @param other
	 * @return
	 */
	
	public boolean equals(Card other)
	{
		if(seme.equals(other.seme)) return false;
		if(valore.equals(other.valore)) return false;
		return true;
	}
	
	@Override
	public int compareTo(Card other)
	{
		if (seme != other.seme) return 0;
		return valore.getVal(); //- other.valore.getVal();
	}
	
	public boolean betterThan(Card other)
	{
		//DA IMPLEMENTARE
		return true;
	}
	
}
