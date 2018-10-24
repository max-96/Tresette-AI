package AI;

import setting.Card;

public class HumanPlayer extends Player
{
	public HumanPlayer(int id)
	{
		super(id);
	}
	
	@Override
	public Card getMove()
	{
		return null;
	}
	
//	@Override
//	public Integer getBestMove(List<List<Integer>> assegnamentoCarte, List<Integer> carteInGioco, double scoreMyTeam,
//			double scoreOtherTeam) {
//		
//		System.out.println(assegnamentoCarte);
//		int myint;
//		Scanner keyboard = new Scanner(System.in);
//		System.out.println("Sei il giocatore numero "+playerId);
//		List<Integer> mosse = carteInGioco.size() == 0 ? new ArrayList<>(assegnamentoCarte.get(playerId))
//				: DeterministicAI.possibiliMosse(assegnamentoCarte.get(playerId), carteInGioco.get(0) / 10);
//		System.out.println(mosse);
//		do {
//		
//		System.out.println("che carta butti");
//		myint = keyboard.nextInt();
//		
//		}while((!mosse.contains(myint)));
//	
//		return myint;
//
//	}

}
