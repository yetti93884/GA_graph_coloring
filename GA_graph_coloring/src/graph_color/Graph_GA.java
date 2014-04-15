package graph_color;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableUndirectedGraph;

public class Graph_GA{
	
	ListenableUndirectedGraph<Integer,DefaultEdge> graph_inp;
//	VisualizeGraph visualizer;
	
	Integer num_vertex;
	Integer num_edge;
	
	Graph_GA()
	{
		graph_inp =new ListenableUndirectedGraph<Integer,DefaultEdge>(DefaultEdge.class);
		
		num_vertex = 0;
		num_edge = 0;
	}
	
	/**\fn void readData(String inp_file) throws NumberFormatException, IOException
	 * 
	 * @param inp_file filename containing the data
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	void readData(String inp_file) throws NumberFormatException, IOException
	{
		boolean flag_FOUND_DIMACS_CONFIG = false;
		int count_edges = 0;
		
		BufferedReader reader = new BufferedReader(new FileReader(inp_file));
		String line = null;
		while ((line = reader.readLine()) != null) {
		    // ...
			String[] parts = line.split("\\s");
			
			if(parts[0].equals("p") && parts[1].equals("edge"))
			{
				num_vertex = Integer.valueOf(parts[2]);
				num_edge = Integer.valueOf(parts[3]);
				System.out.println("vertices : "+ num_vertex + ", edges = " + num_edge);
				for(int i=1; i<=num_vertex; i++)
				{
					graph_inp.addVertex(i);
				}
				flag_FOUND_DIMACS_CONFIG = true;
			}
			if(flag_FOUND_DIMACS_CONFIG)
			{
				if(parts[0].equals("e"))
				{
					Integer edge_point1 = Integer.valueOf(parts[1]);
					Integer edge_point2 = Integer.valueOf(parts[2]);
					graph_inp.addEdge(edge_point1, edge_point2);
					count_edges++;
				}
			}
		}
		if(flag_FOUND_DIMACS_CONFIG && num_edge==count_edges)
			System.out.println("FILE "+inp_file + " read successfully...");
		else
			System.out.println("FILE "+inp_file + " COULD NOT BE READ PROPERLY...");
		
		reader.close();
	}
	public static void main()
	{
	}
}
