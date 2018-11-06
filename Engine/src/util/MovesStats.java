package util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovesStats
{
	private static MovesStats instance = null;
	private String name;
	private List<Map<Integer, Map<Integer, Long>>> stats;

	// depth x domTeam x dominante x carta
	private long[][][][] stats2 = new long[10][2][10][10];

	public static MovesStats getInstance()
	{
		if (instance == null)
			instance = new MovesStats();
		return instance;
	}

	public MovesStats()
	{
		this.name = "Moves statistics for alpha-beta";
		this.stats = new ArrayList<>();
		for (int i = 0; i < 10; i++)
			stats.add(new HashMap<>());

		for (long[][][] a : stats2)
			for (long[][] b : a)
				for (long[] c : b)
					Arrays.fill(c, 0L);
	}

	public MovesStats addStats(int depth, int domCard, int chosenCard)
	{
		if (domCard / 10 != chosenCard / 10)
			return this;

		Integer key = Integer.valueOf(domCard % 10);
		Integer val = Integer.valueOf(chosenCard % 10);

		Map<Integer, Map<Integer, Long>> s = stats.get(depth);
		if (!s.containsKey(Integer.valueOf(domCard % 10)))
			s.put(Integer.valueOf(domCard % 10), new HashMap<>());

		s.get(key).merge(val, Long.valueOf(1), Long::sum);
		return this;
	}

	public MovesStats addStats2(int depth, int domCard, int domTeam, int chosenCard)
	{
		if (domCard / 10 != chosenCard / 10)
			return this;

		stats2[depth][domTeam][domCard][chosenCard] += 1;

		return this;
	}

	public String dump()
	{
		StringBuilder s = new StringBuilder(name + "\n");
		for (int d = 0; d < 10; d++)
		{
			s.append("+++ Profondità ").append(d).append(" +++\n");
			Map<Integer, Map<Integer, Long>> dati1 = stats.get(d);
			for (int dom = 0; dom <= 10; dom++)
			{
				if (!dati1.containsKey(Integer.valueOf(dom)))
					continue;
				Map<Integer, Long> dati2 = dati1.get(Integer.valueOf(dom));

				s.append("Domina ").append(dom).append("\n\t");

				Map<Integer, Double> dati3 = normalize(dati2);
				s.append(dati3.toString()).append("\n");
			}
			s.append("\n");
		}
		return s.toString();
	}

	public void dumpToFile(String filename)
	{
		try
		{
			FileOutputStream fileOutputStream = new FileOutputStream(filename);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(stats2);
			objectOutputStream.flush();
			objectOutputStream.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static Map<Integer, Double> normalize(Map<Integer, Long> dati)
	{
		Map<Integer, Double> a = new HashMap<>();
		long t = 0;
		for (long i : dati.values())
			t += i;
		for (Integer j : dati.keySet())
			a.put(j, Double.valueOf(dati.get(j).doubleValue() * 100 / t));

		return a;
	}

}
