package graph_color;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;

import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableUndirectedGraph;

public class Graph_GA{
	
	ListenableUndirectedGraph<GA_Graph_Node,DefaultEdge> graph_inp;
//	VisualizeGraph visualizer;
	
	Integer num_vertex;
	Integer num_edge;
	
	Graph_GA()
	{
		graph_inp =new ListenableUndirectedGraph<GA_Graph_Node,DefaultEdge>(DefaultEdge.class);
		
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
					GA_Graph_Node new_node = new GA_Graph_Node(i);
					graph_inp.addVertex(new_node);
				}
				flag_FOUND_DIMACS_CONFIG = true;
			}
			if(flag_FOUND_DIMACS_CONFIG)
			{
				if(parts[0].equals("e"))
				{
					GA_Graph_Node vertex_container[] = getNodes();
					
//					System.out.println(vertex_container[count_edges].numID);
					Integer edge_point1 = Integer.valueOf(parts[1]);
					Integer edge_point2 = Integer.valueOf(parts[2]);
					graph_inp.addEdge(vertex_container[edge_point1-1], vertex_container[edge_point2-1]);
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
	GA_Graph_Node[] getNodes()
	{
		Set<GA_Graph_Node> vertex_set = graph_inp.vertexSet();
		Iterator<GA_Graph_Node> node_iterator= vertex_set.iterator();
		
		GA_Graph_Node vertex_container[] = new GA_Graph_Node[num_vertex];
		for(int j=0;j<num_vertex;j++)
		{
			vertex_container[j] = node_iterator.next();
		}
		
		return vertex_container;
	}
	public static void main(String[] args) throws NumberFormatException, IOException
	{
		Graph_GA obj = new Graph_GA();
		obj.readData("dataset/myciel3.col");
		
		DisplayGraph frame = new DisplayGraph(obj,4);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1280, 720);
		frame.setVisible(true);
	}
}
