package AI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import MCTS.MonteCarloTreeSearch;
import minmax.AlphaBetaKiller3;
import minmax.AlphaBetaTest;

public class AITesting {
	public static void main(String[] args) {
		
		test1();
		System.out.println("--------");
		float[] alphaP= AlphaBetaTest.normalize(AlphaBetaKiller3.alphacount);
		float[] betaP= AlphaBetaTest.normalize(AlphaBetaKiller3.betacount);
		System.out.println(Arrays.toString(alphaP));
		System.out.println(Arrays.toString(betaP));
		System.out.println((double)AlphaBetaKiller3.maxExecTime / 1000);
		System.out.println((double)MonteCarloTreeSearch.maxExecTime / 1000);
	}

	private static void test1() {
		int[] winnings= {0,0};
		boolean[] playerALFABETA= {true, false, true, false};
		for (int k = 0; k < 5; k++) {
			List<List<Integer>> randomAssignment = randomAssignment();
			List<Integer> cardsOnTable = new ArrayList<>();
			int currentplayer= find4Denari(randomAssignment);
			int initTeam=currentplayer%2;
			GameState gs=new GameState(randomAssignment, cardsOnTable, currentplayer, true, 0, 0);
			double score1=0, score2=0;
			while(!gs.terminal)
			{				
				DeterministicAI dai=playerALFABETA[currentplayer] ? genABK3(currentplayer) : genMCTS(currentplayer);
				Integer mossa=dai.getBestMove(randomAssignment, cardsOnTable, score1 , score2);
				gs= gs.genSuccessor(mossa);
				System.out.print("x");
				randomAssignment=gs.getCardsAssignment();
				cardsOnTable= gs.getCardsOnTable();
				currentplayer=gs.currentPlayer;
				if(currentplayer%2 == initTeam)
				{
					score1=gs.scoreMyTeam;
					score2=gs.scoreOtherTeam;
				}
				else
				{
					score2=gs.scoreMyTeam;
					score1=gs.scoreOtherTeam;
				}
				
			}
			
			if(score1>score2)
			{
				winnings[initTeam%2]+=1;
				System.out.println("."+(initTeam)%2);
			}
			else
			{
				winnings[(initTeam+1)%2]+=1;
				System.out.println("."+(initTeam+1)%2);
			}
			
		}
		System.out.println("TEAM EVEN:\t"+winnings[0]);
		System.out.println("TEAM ODD:\t"+winnings[1]);
		

	}

	private static int find4Denari(List<List<Integer>> l)
	{
		for(int i=0;i<4;i++)
			if (l.get(i).contains(3))
				return i;
		return -1;
	}
	
	
	private static List<List<Integer>> randomAssignment() {
		List<List<Integer>> assegnamentoCasuale = new ArrayList<>();
		List<Integer> t = new ArrayList<>();

		for (int i = 0; i < 40; i++) {
			t.add(i);
		}
		Collections.shuffle(t);

		for (int i = 0; i < 4; i++) {
			List<Integer> l = new ArrayList<>(t.subList(i * 10, (i + 1) * 10));
			assegnamentoCasuale.add(l);
		}
		return assegnamentoCasuale;

	}

	public static AlphaBetaKiller3 genABK3(int player) {
		return new AlphaBetaKiller3(player, 16);
	}

	public static MonteCarloTreeSearch genMCTS(int player) {
		return new MonteCarloTreeSearch(player, 30000);
	}
}
