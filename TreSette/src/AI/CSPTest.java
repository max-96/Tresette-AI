package AI;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

public class CSPTest {
	public static void main(String[] args) throws InterruptedException {

		HashSet<Integer> ex = new HashSet<>();
		LinkedList<Integer> mano = new LinkedList<>();
		mano.add(0);
		mano.add(1);
		mano.add(2);
		mano.add(3);
		mano.add(4);
		mano.add(5);
		mano.add(6);
		mano.add(7);
		mano.add(8);
		mano.add(9);

		int[] rcards = { 0, 10, 10, 10 };
		boolean[][] piombi= {
				{false,false,false,false},
				{false,false,false,false},
				{false,false,false,false},
				{false,false,false,false},
		};
		
		CSPSolver ccc = new CSPSolver(0, ex, mano, rcards, 10000, piombi);

		
		ccc.produce();

		while (!ccc.isDone()) {
			Thread.sleep(100);

		}
		System.out.println("Stampo:");
		int c=1;
		while (!ccc.soluzioni.isEmpty()) {
			
			Map<Integer, Integer> sol=ccc.soluzioni.take();
			if(sol== Collections.EMPTY_MAP)
				System.out.println("Fine delle soluzioni");
			else
			{
				System.out.print(c+" ");
				System.out.println(sol);
			}
			c++;
		}
		System.out.println("Tempo di Esecuzione: "+ccc.tempoEsec+" ms");
	}
}