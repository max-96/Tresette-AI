package AI;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import setting.Card;

public class MyCSP
{
	private Set<Integer> carteInGioco;
	private int id;
	private int n;
	private List<Integer>[] sol;
	private int[] numeroCarteInmano;
	private List<Integer> mieCarte;
	List<Integer>[] semiAttivi;
	
	protected void compute()
	{
		Collections.copy(sol[id], mieCarte);
		carteInGioco.removeAll(mieCarte);
		
		for(int i=0; i<2; i++)
		{
			int player = (id + 1) % 4;
			
		}
	}
}
