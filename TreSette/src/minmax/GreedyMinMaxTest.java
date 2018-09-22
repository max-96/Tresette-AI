package minmax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import AI.DeterministicAI;

public class GreedyMinMaxTest {
	
	public static void main(String[] args) {
		test1();
		
	}
	
	private static void test1()
	{
		int player=0;
		
		List<List<Integer>> assegnamento= new ArrayList<>();
		List<Integer> carteInGioco=new ArrayList<>();
		ConcurrentHashMap<Integer, Double> valoriCarte= new ConcurrentHashMap<>();
		
		/*
		 * Assegno valori a assegnamento
		 */
		for(int i=0;i<4;i++)
		{
			List<Integer> t=new ArrayList<>();
			t.add(9-i);
		assegnamento.add( t);
		}
		assegnamento.get(0).add(0);
		assegnamento.get(0).add(1);
		assegnamento.get(0).add(2);
		assegnamento.get(1).add(4);
		carteInGioco.add(3);

		for(Integer c: assegnamento.get(player))
			valoriCarte.put(c, 0.0);
		
			
		
		
		System.out.println("Punti per carta: "+Arrays.toString(DeterministicAI.puntiPerCarta));
		System.out.println("Domini per carta: "+Arrays.toString(DeterministicAI.dominioPerCarta));
		System.out.println("Ass:"+assegnamento);
		System.out.println("Carte:"+ carteInGioco);
		System.out.println("Valori carte:"+ valoriCarte);
		
		
		GreedyMinMax gmm= new GreedyMinMax(0);
		
		
		
		System.out.println("Mossa Scelta: "+gmm.getBestMove(assegnamento, carteInGioco, valoriCarte ));
		
		System.out.println("Valori carte:"+ valoriCarte);
		
		
		
	}

}
