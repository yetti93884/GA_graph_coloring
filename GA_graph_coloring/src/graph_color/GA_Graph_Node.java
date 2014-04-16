package graph_color;

public class GA_Graph_Node {
	
	public Integer numID;
	public Integer colorID;
	public Integer max_num_colors;
	GA_Graph_Node(int i)
	{
		numID = i;
		colorID = i%4+1;
	}
	GA_Graph_Node(int i, int col)
	{
		numID = i;
		colorID = col;
	}

}
