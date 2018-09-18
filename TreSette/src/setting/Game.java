package setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import AI.Player;
import AI.PlayerAI;

public class Game {

	private Map<Integer, List<Card>> assCarte = new HashMap<>();
	private Player[] players = new PlayerAI[4];
	private Set<Card> exCards = new HashSet<>();
	private int[] punteggi = {0, 0};
	private Map<Integer, List<Card.Suit>> semiAttivi = new HashMap<>(); // player x semi

	public Game() {
		initialise();
	}

	/**
	 * esegue il gioco 
	 */
//	public void run()
//	{
//		
//		System.out.println("Inizio mano.");
//		System.out.println("Inizia il player "+assCarte[9]+".");
//		
//		int turno=assCarte[9];
//		
//		//10 "passate"
//		for(int g=1;g<=10;g++)
//		{
//			System.out.println(">Passata "+g+".");
//			
//			for(int j=0;j<4;j++)
//			{
//				PlayerAI p= players[(turno+j) % 4];
//				p.getMossa();
//			}
//		}
//		
//	}
//	
	
	
	
	public Set<Card> getExCards() {
		return new HashSet<>(exCards);
	}

	public Map<Integer, List<Card.Suit>> getSemiAttivi() {
		return null;
	}

	/**
	 * Distribuisce le carte e inizializza i player
	 */
	private void initialise()
	{
		List<Integer> temp = new LinkedList<>();
		for(int i=0; i<40; i++)
			temp.add(i);
		
		Collections.shuffle(temp);
		
		for(int i=0; i<4; i++)
		{
			List<Card> carteInMano = new LinkedList<>(); 
			
			for(int j= 0; j<10; j++)
				carteInMano.add(new Card(temp.remove(0)));
			
			assCarte.put(i, carteInMano);
			semiAttivi.put(i, Arrays.asList(Card.Suit.values()));
			
			players[i] = new PlayerAI(i, carteInMano, this);
		}
	}
}
