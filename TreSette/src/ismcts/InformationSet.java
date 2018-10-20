package ismcts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import AI.DeterministicAI;
import util.CardsUtils;

public class InformationSet
{
	public final boolean terminal;

	private final double[] scores;
	private final List<Set<Integer>> possibleCards;
	private final List<Integer> cardsOnTable;
	private final int currentPlayer;
	private final byte[] cardsLeft;
	public final boolean maxNode;
	public final byte maxTeam;

	public InformationSet(double[] scores, List<Set<Integer>> possibleCards, List<Integer> cardsOnTable,
			int currentPlayer, byte[] cardsLeft, byte maxTeam)
	{
		this.scores = scores;
		this.possibleCards = possibleCards;
		this.cardsOnTable = cardsOnTable;
		this.currentPlayer = currentPlayer;
		this.cardsLeft = cardsLeft;
		this.terminal = cardsLeft[currentPlayer] == 0;
		this.maxNode = (currentPlayer & 1) == maxTeam;
		this.maxTeam = maxTeam;

	}

	public List<Integer> generateActions(Object determinizzazione)
	{

		if (terminal)
			return null;

		// TODO aggiornare con determinizzazione

		return null;

	}

	public InformationSet genSuccessor(Integer mossa)
	{
		if (terminal)
			return null;

		List<Set<Integer>> newPossibleCards = new ArrayList<>(possibleCards);
		List<Integer> newCardsOnTable = new ArrayList<>(cardsOnTable);
		newPossibleCards.get(currentPlayer).remove(mossa);
		newCardsOnTable.add(mossa);
		byte[] newCardsLeft = Arrays.copyOf(cardsLeft, 4);
		newCardsLeft[currentPlayer] -= 1;

		if (newCardsOnTable.size() < 4)
		{
			InformationSet is = new InformationSet(scores, newPossibleCards, newCardsOnTable, (currentPlayer + 1) % 4,
					newCardsLeft, maxTeam);
			return is;
		} else
		{
			// Compute dominante
			double punteggio = 0;
			int domCard = newCardsOnTable.get(0).intValue();
			Integer startPlayer = Integer.valueOf((currentPlayer + 1) % 4);
			Integer domPlayer = startPlayer;
			for (int i = 1; i < 4; i++)
			{
				int c = newCardsOnTable.get(i).intValue();
				punteggio += CardsUtils.puntiPerCarta[c % 10];
				if (domCard / 10 == c / 10
						&& CardsUtils.dominioPerCarta[c % 10] > CardsUtils.dominioPerCarta[domCard % 10])
				{
					domPlayer = Integer.valueOf((startPlayer.intValue() + i) % 4);
					domCard = c;
				}
			}
			double[] newScores = Arrays.copyOf(scores, 2);
			newScores[domPlayer & 1] += punteggio;
			newCardsOnTable = Collections.emptyList();

			InformationSet is = new InformationSet(newScores, newPossibleCards, newCardsOnTable, domPlayer,
					newCardsLeft, maxTeam);
			return is;
		}

	}

	public Integer genRandMossa(Object determin)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public double getScoreSoFar()
	{
		double r = scores[0] - scores[1];
		return (maxTeam == 0) ? r : -r;
	}

}
