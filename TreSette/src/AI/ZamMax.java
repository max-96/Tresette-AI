package AI;

import java.util.LinkedList;
import java.util.List;

public class ZamMax {

	private List<List<Integer>> assegnamentoIniziale;
	private int turno;
	private int passata;
	

		
	public ZamMax(List<List<Integer>> assegnamentoIniziale, int turno, int passata) {
		this.assegnamentoIniziale = assegnamentoIniziale;
		this.turno = turno;
		this.passata = passata;
	}







	public static List<Integer> possibiliMosse(List<Integer> carte, int semeAttuale)
	{
		List<Integer> mosse=new LinkedList<>();
		for(Integer c: carte)
		{
			if(c/10 == semeAttuale)
				mosse.add(c);
		}
		
		if(mosse.isEmpty())
			mosse.addAll(carte);
		
		return mosse;		
	}
	
	
}
