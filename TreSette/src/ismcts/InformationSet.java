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

	public InformationSet(double[] scores, List<Set<Integer>> possibleCards, List<Integer> cardsOnTable,
			int currentPlayer)
	{
		this.scores = scores;
		this.possibleCards = possibleCards;
		this.cardsOnTable = cardsOnTable;
		this.currentPlayer = currentPlayer;
		terminal = true;// TODO aggiornare

	}

	public InformationSet genSuccessor(Integer mossa)
	{
		if (terminal)
			return null;

		List<Set<Integer>> newPossibleCards = new ArrayList<>(possibleCards);
		List<Integer> newCardsOnTable = new ArrayList<>(cardsOnTable);
		newPossibleCards.get(currentPlayer).remove(mossa);
		newCardsOnTable.add(mossa);

		if (newCardsOnTable.size() < 4)
		{
			InformationSet is = new InformationSet(scores, newPossibleCards, newCardsOnTable, (currentPlayer + 1) % 4);
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

			InformationSet is = new InformationSet(newScores, newPossibleCards, newCardsOnTable, domPlayer);

			return is;

		}

	}

}
