package AI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandWalk extends DeterministicAI {
	
	private int playerId;
	
	public RandWalk(int player) {
		playerId=player;
	}

	@Override
	public Integer getBestMove(List<List<Integer>> assegnamentoCarte, List<Integer> carteInGioco, double scoreMyTeam,
			double scoreOtherTeam) {
		List<Integer> mosse = carteInGioco.isEmpty() ? new ArrayList<>(assegnamentoCarte.get(playerId))
				: DeterministicAI.possibiliMosse(assegnamentoCarte.get(playerId), carteInGioco.get(0) / 10);
		
		Random r=new Random();
		
		return mosse.get(r.nextInt(mosse.size()));
		
	}

}
