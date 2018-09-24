package minmax;

import java.util.ArrayList;

public class DebugGraph {

 public Node root;
	
	public static class Node
	{
		public Integer valore=-1;
		public ArrayList<Node> figli=new ArrayList<>();
		
		public boolean checkNumber(Integer c)
		{
			return valore.equals(c);
		}
	}
}
