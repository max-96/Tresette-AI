package MCTS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MCTSTest {

	public static void main(String[] args) {
		test1();

	}
	
	public static void test1()
	{
		List<List<Integer>> assegnamentoCasuale = new ArrayList<>();
		List<Integer> cardsOnTable = new ArrayList<>();
		{
			List<Integer> t = new ArrayList<>();

			for (int i = 0; i < 40; i++) {
				t.add(i);
			}
			Collections.shuffle(t, new Random(1200));

			for (int i = 0; i < 4; i++) {
				List<Integer> l = new ArrayList<>(t.subList(i * 10, (i + 1) * 10));
				assegnamentoCasuale.add(l);
			}
		}
		
		System.out.println("Assegnamento: ");
		System.out.println(assegnamentoCasuale);
		MonteCarloTreeSearch mcts= new MonteCarloTreeSearch(0, 100000);
		Integer mossa=mcts.getBestMove(assegnamentoCasuale, cardsOnTable, 0);
		System.out.println("Mossa Scelta: "+mossa);
		
	}

}
