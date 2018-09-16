package AI;

import java.util.HashSet;
import java.util.LinkedList;

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
			System.out.println("Dormo. Size:"+ccc.soluzioni.size());
			Thread.sleep(1000);

		}
		System.out.println("Stampo:");
		int c=1;
		while (!ccc.soluzioni.isEmpty()) {
			System.out.print(c);
			c++;
			System.out.println(ccc.soluzioni.take());
		}
	}
}