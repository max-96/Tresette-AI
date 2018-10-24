package AI;

import java.util.List;

import setting.Card;
import setting.Game;

/**
 * A test AI to check if Game is working correctly.
 * This AI just throw one of the cards in his hand without thinking.
 * Of course this is created by the only one who could handle a delicate task like this.
 * 
 * @author Zamma
 *
 */
public class DumbPlayerAI extends Player{

	public DumbPlayerAI(int id, List<Card> carte, Game gioco) {
		super(id, carte, gioco);
	}

	@Override
	public Card getMove() {
		
		int len = this.carteInMano.size();
		return this.carteInMano.remove(len-1);
	}

}
