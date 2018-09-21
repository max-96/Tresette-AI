package AI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GreedyMinMaxTest {
	
	public static void main(String[] args) {
		test1();
		
	}
	
	private static void test1()
	{
		List<List<Integer>> assegnamento= new ArrayList<>();
		List<Integer> carteInGioco=new ArrayList<>();
		
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

		System.out.println("Punti per carta: "+Arrays.toString(DeterministicAI.puntiPerCarta));
		System.out.println("Domini per carta: "+Arrays.toString(DeterministicAI.dominioPerCarta));
		System.out.println("Ass:"+assegnamento);
		System.out.println("Carte:"+ carteInGioco);
		
		
		
		GreedyMinMax gmm= new GreedyMinMax(0);
		
		
		System.out.println(gmm.getBestMove(assegnamento, carteInGioco));
		
		
		
		
		
	}

}
