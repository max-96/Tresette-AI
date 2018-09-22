package AI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;

import minmax.GreedyMinMax.GreedyMinMaxForkino;
import setting.Card;
import setting.Game;

public class GreedyPlayerAI extends Player
{
	private static final int N_BOARD_GEN = 100;


	/**
	 * Numero di carte sconosciute dall'attuale player per ciascun player
	 */
	int[] unknownCards = new int[4];

	public GreedyPlayerAI(int id, List<Card> carte, Game gioco)
	{
		super(id, carte, gioco);
	}

	public Card getMossa()
	{
		HashSet<Integer> carteUsate= new HashSet<Integer>();
		for(Card c: gioco.getExCards())
			carteUsate.add(c.toInt());
		
		ArrayList<Integer> carteInMano= new ArrayList<>();
		for(Card c: this.carteInMano)
			carteInMano.add(c.toInt());
		
		List<Card.Suit>[] semiAttivi=gioco.getSemiAttivi();
//		boolean[][] semAtt=new boolean [4][4];
//		for(int p=0;p<semiAttivi.length;p++)
//		{
//			//TODO conversione da lista di cards a booleani
//			
//			
//			
//		}
		boolean[][] semAtt= { { true, true, true, true }, { true, true, true, true }, { true, true, true, true },
				{ true, true, true, true }, }; //TODO rimuovere sto placeholder
		
		/*
		 * Conversione in interi e booleani finita 
		 */
		
		SforzaSolver solver = new SforzaSolver(this.id, carteUsate, carteInMano, gioco.getAllCardsInHand(), semAtt, N_BOARD_GEN);
		solver.startProducing(); //inizio la produzione di possibili assegnamenti
		
		ConcurrentHashMap<Integer, Double> punteggiPerAzione= new ConcurrentHashMap<>(carteInMano.size(), 0, ForkJoinPool.getCommonPoolParallelism());
		ArrayList<GreedyMinMaxForkino> threads= new ArrayList<>();
		ForkJoinPool pool= new ForkJoinPool();
		

		try {
			List<List<Integer>> assegnamento= solver.possibiliAssegnamenti.take();
			while(assegnamento != Collections.EMPTY_LIST)
			{
				//TODO sostituire la emptylist con le carte in gioco al momento
				GreedyMinMaxForkino t = new GreedyMinMaxForkino(id, assegnamento, Collections.emptyList(), punteggiPerAzione);
				pool.execute(t);
				threads.add(t);
				
				
				assegnamento=solver.possibiliAssegnamenti.take();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
}
