package AI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
//import java.util.Random;


import MCTS.MonteCarloTreeSearch;
import minmax.AlphaBetaKiller;
import minmax.AlphaBetaKiller3;

public class AITesting {
	
	public static int NUM_TRAILS=500;
	
	public static void main(String[] args) {
		
//		System.out.println(getStats(4291,709, 5000));
		Locale.setDefault(Locale.US);
		test3();
//		System.out.println("--------");
////		float[] alphaP= AlphaBetaTest.normalize(AlphaBetaKiller3.alphacount);
////		float[] betaP= AlphaBetaTest.normalize(AlphaBetaKiller3.betacount);
////		System.out.println(Arrays.toString(alphaP));
////		System.out.println(Arrays.toString(betaP));
		System.out.println(String.format("AlphaBeta Max Time:\t%.3f", (double)AlphaBetaKiller3.maxExecTime / 1000));
		System.out.println(String.format("MonteCarloTS Max Time:\t%.3f", (double)MonteCarloTreeSearch.maxExecTime / 1000));
	}

	private static void test1() {
		int[] winnings= {0,0};
		boolean[] playerALFABETA= {true, false, true, false};
		for (int k = 0; k < NUM_TRAILS; k++) {
			List<List<Integer>> randomAssignment = randomAssignment();
			List<Integer> cardsOnTable = new ArrayList<>();
			int currentplayer= find4Denari(randomAssignment);
			int initTeam=currentplayer%2;
			System.out.print(initTeam+" ");
			NeutralGameState gs=new NeutralGameState(randomAssignment, cardsOnTable, currentplayer, 0, 0);
			double score1=0, score2=0;
			
			while(!gs.terminal)
			{				
				DeterministicAI dai=playerALFABETA[currentplayer] ? genABK3(currentplayer, 4) : genMCTS(currentplayer, 30);
				Integer mossa=dai.getBestMove(randomAssignment, cardsOnTable, score1 , score2);
				gs= gs.genSuccessor(mossa);
				System.out.print("x");
				randomAssignment=gs.getCardsAssignment();
				cardsOnTable= gs.getCardsOnTable();
				currentplayer=gs.currentPlayer;

			}
			
			if(gs.getScoreEven() > gs.getScoreOdd())
			{
				winnings[0]+=1;
				System.out.println(".0");
			}
			else
			{
				winnings[1]+=1;
				System.out.println(".1");
			}
			
		}
		System.out.println("TEAM EVEN:\t"+winnings[0]);
		System.out.println("TEAM ODD:\t"+winnings[1]);
		System.out.println(getStats(winnings[0], winnings[1], NUM_TRAILS));

	}

	
	
	private static void test2() {
		int[] winnings= {0,0};
		boolean[] playerALFABETA= {true, false, true, false};
		for (int k = 0; k < NUM_TRAILS; k++) {
			List<List<Integer>> randomAssignment = randomAssignment();
			List<Integer> cardsOnTable = new ArrayList<>();
			int currentplayer= find4Denari(randomAssignment);
			int initTeam=currentplayer%2;
			System.out.print(initTeam+" ");
			NeutralGameState gs=new NeutralGameState(randomAssignment, cardsOnTable, currentplayer, 0, 0);
			double score1=0, score2=0;
			
			while(!gs.terminal)
			{				
				DeterministicAI dai=playerALFABETA[currentplayer] ? genABK3(currentplayer, 16) : genRandWalk(currentplayer);
				Integer mossa=dai.getBestMove(randomAssignment, cardsOnTable, 0 , 0);
				gs= gs.genSuccessor(mossa);
				System.out.print("x");
				randomAssignment=gs.getCardsAssignment();
				cardsOnTable= gs.getCardsOnTable();
				currentplayer=gs.currentPlayer;

			}
			
			if(gs.getScoreEven() > gs.getScoreOdd())
			{
				winnings[0]+=1;
				System.out.println(".0");
			}
			else
			{
				winnings[1]+=1;
				System.out.println(".1");
			}
			
		}
		System.out.println("TEAM EVEN:\t"+winnings[0]);
		System.out.println("TEAM ODD:\t"+winnings[1]);
		System.out.println(getStats(winnings[0], winnings[1], NUM_TRAILS));
		

	}
	
	private static void test3() {
		int[] winnings= {0,0};
		boolean[] playerMCTS= {true, false, true, false};
		for (int k = 0; k < NUM_TRAILS; k++) {
			List<List<Integer>> randomAssignment = randomAssignment();
			List<Integer> cardsOnTable = new ArrayList<>();
			int currentplayer= find4Denari(randomAssignment);
			int initTeam=currentplayer%2;
			System.out.print(initTeam+" ");
			NeutralGameState gs=new NeutralGameState(randomAssignment, cardsOnTable, currentplayer, 0, 0);
			double score1=0, score2=0;
			
			while(!gs.terminal)
			{				
				DeterministicAI dai=playerMCTS[currentplayer] ? genMCTS(currentplayer, 60000) : genRandWalk(currentplayer);
				Integer mossa=dai.getBestMove(randomAssignment, cardsOnTable, score1 , score2);
				gs= gs.genSuccessor(mossa);
				System.out.print("x");
				randomAssignment=gs.getCardsAssignment();
				cardsOnTable= gs.getCardsOnTable();
				currentplayer=gs.currentPlayer;

			}
			
			if(gs.getScoreEven() > gs.getScoreOdd())
			{
				winnings[0]+=1;
				System.out.println(".0");
			}
			else
			{
				winnings[1]+=1;
				System.out.println(".1");
			}
			
		}
		System.out.println("TEAM EVEN:\t"+winnings[0]);
		System.out.println("TEAM ODD:\t"+winnings[1]);
		System.out.println(getStats(winnings[0], winnings[1], NUM_TRAILS));

	}
	
	private static void test4() {
		int[] winnings= {0,0};
		boolean[] playerALFABETA= {true, false, true, false};
//		for (int k = 0; k < 20; k++) {
			List<List<Integer>> randomAssignment = randomAssignment();
			
			System.out.println(randomAssignment);
			List<Integer> cardsOnTable = new ArrayList<>();
			int currentplayer= find4Denari(randomAssignment);
			System.out.println("Inizia player: "+currentplayer);
			int initTeam=currentplayer%2;
			AIGameState gs=new AIGameState(randomAssignment, cardsOnTable, currentplayer, true, 0, 0);
			double score1=0, score2=0;
			while(!gs.terminal)
			{	
				
				DeterministicAI dai = playerALFABETA[currentplayer] ? genABK3(currentplayer) :  genRandWalk(currentplayer);
				Integer mossa=dai.getBestMove(randomAssignment, cardsOnTable, score1 , score2);
				System.out.print(currentplayer+":"+mossa+"\t");
				
				
				gs= gs.genSuccessor(mossa, true);
				
				randomAssignment=gs.getCardsAssignment();
				//System.out.println(randomAssignment);
				
				cardsOnTable= gs.getCardsOnTable();
				if(cardsOnTable.isEmpty())
					System.out.println();
				
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
//			}
	}
	
	private static void test5() {
		int[] winnings= {0,0};
		boolean[] playerALFABETA= {true, false, true, false};
//		for (int k = 0; k < 20; k++) {
			List<List<Integer>> randomAssignment = randomAssignment();
			
			System.out.println(randomAssignment);
			List<Integer> cardsOnTable = new ArrayList<>();
			int currentplayer= find4Denari(randomAssignment);
			System.out.println("Inizia player: "+currentplayer);
			int initTeam=currentplayer%2;
			AIGameState gs=new AIGameState(randomAssignment, cardsOnTable, currentplayer, true, 0, 0);
			double score1=0, score2=0;
			while(!gs.terminal)
			{	
				
				DeterministicAI dai = playerALFABETA[currentplayer] ? genABK3(currentplayer) :  new HumanPlayer(currentplayer);
					
				Integer mossa=dai.getBestMove(randomAssignment, cardsOnTable, score1 , score2);
				System.out.print(currentplayer+":"+mossa+"\t");
				
				
				gs= gs.genSuccessor(mossa);
				
				randomAssignment=gs.getCardsAssignment();
				//System.out.println(randomAssignment);
				
				cardsOnTable= gs.getCardsOnTable();
				if(cardsOnTable.isEmpty())
					System.out.println();
				
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
//			}
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

	public static String getStats(double evenWinnings, double oddWinnings, double total)
	{
		
		if(evenWinnings + oddWinnings != total)
			throw new RuntimeException("Errore calcolo");
		double avg = evenWinnings/total;
		
		double t1 = 1.0 - avg;
		double t2 = avg;
		
		double ssd = Math.sqrt((evenWinnings*(t1*t1) + oddWinnings*(t2*t2))/(total-1));
		double moreorless=1.96*(ssd / Math.sqrt(total));
		double infBound = avg - moreorless;
		double uppBound = avg + moreorless;
		
		
		String format= "Avg:\t%.5f\nSsd:\t%.5f\nCI:\t[ %.5f ; %.5f ]\n";
		return String.format(format, avg, ssd, infBound, uppBound);	
	}
	public static RandWalk genRandWalk(int player) {
		return new RandWalk(player);
	}

	public static AlphaBetaKiller3 genABK3(int player) {
		return new AlphaBetaKiller3(player, 16);
	}
	
	public static AlphaBetaKiller genABK(int player) {
		return new AlphaBetaKiller(player, 3);
	}
	
	public static AlphaBetaKiller3 genABK3(int player, int depth) {
		return new AlphaBetaKiller3(player, depth);
	}
	
	public static MonteCarloTreeSearch genMCTS(int player) {
		return genMCTS(player, 50000);
	}
	public static MonteCarloTreeSearch genMCTS(int player, int iter) {
		return new MonteCarloTreeSearch(player, iter);
	}
}
