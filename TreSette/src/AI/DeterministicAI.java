package AI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class DeterministicAI {
	
	//utilizzando conversione Asso, 2, 3, 4, 5, 6, 7, fante, cavallo, re
	public final static double[] puntiPerCarta= {1, 1.0/3, 1.0/3, 0, 0, 0, 1.0/3, 1.0/3, 1.0/3, 1.0/3} ; 
	public final static int[] dominioPerCarta= {7, 8, 9, 0, 1, 2, 3, 4, 5, 6};
	
	
	public abstract Integer getBestMove(List<List<Integer>> assegnamentoCarte, List<Integer> carteInGioco);
	
	
	public static List<Integer> possibiliMosse(List<Integer> carte, int semeAttuale)
	{
		List<Integer> mosse=new ArrayList<>();
		for(Integer c: carte)
		{
			if(c/10 == semeAttuale)
				mosse.add(c);
		}
		
		if(mosse.isEmpty())
			mosse.addAll(carte);
		
		return mosse;		
	}
	
	
	
}
