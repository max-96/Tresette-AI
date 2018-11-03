package ismcts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

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

	public List<Integer> generateActions(List<List<Integer>> det)
	{

		if (terminal)
			return null;

		// TODO aggiornare con determinizzazione

		return CardsUtils.getPossibiliMosse(det.get(currentPlayer), cardsOnTable);
	}

	public InformationSet genSuccessor(Integer mossa)
	{
		if (terminal)
			return null;

		assert possibleCards.get(currentPlayer).contains(mossa);

		List<Set<Integer>> newPossibleCards = new ArrayList<>(possibleCards);
		List<Integer> newCardsOnTable = new ArrayList<>(cardsOnTable);
		for(int i=0; i<4;i++)
			newPossibleCards.get(i).remove(mossa);
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

	public Integer genRandMossa(List<List<Integer>> determin)
	{
		List<Integer> mossa = generateActions(determin);

		return mossa.get(ThreadLocalRandom.current().nextInt(mossa.size()));

	}

	public double getScoreSoFar()
	{
		double r = scores[0] - scores[1];
		return (maxTeam == 0) ? r : -r;
	}

	public boolean isCompatible(List<List<Integer>> determ)
	{
		boolean sizes = determ.get(0).size() == cardsLeft[0] && determ.get(1).size() == cardsLeft[1]
				&& determ.get(2).size() == cardsLeft[2] && determ.get(3).size() == cardsLeft[3];

		return sizes && possibleCards.get(0).containsAll(determ.get(0))
				&& possibleCards.get(1).containsAll(determ.get(1)) && possibleCards.get(2).containsAll(determ.get(2))
				&& possibleCards.get(3).containsAll(determ.get(3));
	}

	public int getCurrentPlayer()
	{
		return currentPlayer;
	}
}
