package setting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import AI.PlayerAI;

public class Gioco {

	private int[] assCarte = new int[40];
	private PlayerAI[] players= new PlayerAI[4];
	private Set<Integer> exCards=new HashSet<>();
	private boolean[][] piombi=new boolean[4][4];// player x semi

	public Gioco() {
		initialise();
		
	}

	/**
	 * esegue il gioco 
	 */
	public void run()
	{
		
		System.out.println("Inizio mano.");
		System.out.println("Inizia il player "+assCarte[9]+".");
		
		int turno=assCarte[9];
		
		//10 "passate"
		for(int g=1;g<=10;g++)
		{
			System.out.println(">Passata "+g+".");
			
			for(int j=0;j<4;j++)
			{
				PlayerAI p= players[(turno+j) % 4];
				p.getMossa();
			}
		}
		
	}
	
	
	
	
	public HashSet<Integer> getExCards() {
		return new HashSet<>(exCards);
	}

	public boolean[][] getPiombi() {
		return piombi;
	}

	/**
	 * Distribuisce le carte e inizializza i player
	 */
	private void initialise()
	{
		@SuppressWarnings("unchecked")
		LinkedList<Integer>[] cardsPlayer= (LinkedList<Integer>[]) new LinkedList[4];
		
		ArrayList<Integer> temp= new ArrayList<>();
		for(int i=0; i<40; i++)
			temp.add(i);
		
		Collections.shuffle(temp);
		
		for (int i=0;i<40;i++)
		{
			assCarte[temp.get(i)] = (i % 4);
			cardsPlayer[(i%4)].add(temp.get(i));
		}
		
		for(int i=0;i<4;i++)
		{
			players[i]= new PlayerAI(i, cardsPlayer[i], this);
			for(int j=0;j<4;j++)
				piombi[i][j]=false;
		}
		
		
	}
}
