package graph_color;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableUndirectedGraph;

public class Graph_GA{
	
	ListenableUndirectedGraph<GA_Graph_Node,DefaultEdge> graph_inp;
//	VisualizeGraph visualizer;
	
	Integer num_vertex;
	Integer num_edge;
	
	int K;
	/**< the chromatic number of the graph as preset by the user*/
	
	int max_iter;
	/**< maximum number of iterations for which GA is run*/
	
	float mutation_index;
	/**< mutation index for the graph*/
	
	int MAX_VERTEX_DEGREE;
	/**< the maximum degree for any vertex in the graph*/
	
	Graph_GA()
	{
		graph_inp =new ListenableUndirectedGraph<GA_Graph_Node,DefaultEdge>(DefaultEdge.class);
		
		num_vertex = 0;
		num_edge = 0;
	}
	
	/** \fn void findMaxVertexDegree()
	 * \brief finds the maximum degree of any vertex present in the graph
	 */
	void findMaxVertexDegree()
	{
		GA_Graph_Node vertex_container[] = getNodes();
		int[] vertex_degree = new int[num_vertex];
		int max=0;
		for(int i=0;i<num_vertex;i++)
		{
			vertex_degree[i] = graph_inp.degreeOf( vertex_container[i]);
			if(vertex_degree[i] > max)
				max = vertex_degree[i];  
		}
		MAX_VERTEX_DEGREE = max;
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
		obj.readData("dataset/queen8_8.col");
		obj.findMaxVertexDegree();
		
		int chromatic_number = 20;
		
		boolean flag_SOLUTION_ARRIVED = true;
		boolean flag_LAST_ATTEMPT_FAILED = false;
		
		while(flag_SOLUTION_ARRIVED == true)
		{
			obj.K = chromatic_number;
			GA_KGraph ga_obj = new GA_KGraph(obj,100,2000,0.75f);
			
			long startTime = System.currentTimeMillis();
			System.out.println("\n\n-----------------------------------------\nStarting_GA ....");
			ga_obj.generateInitialPopulation();
			int num_iter = ga_obj.runGAGenerations(0);
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			System.out.println("Starting_GA ...." + totalTime + "ms DONE");
			
			
			ga_obj.setChromosomeToGraph(ga_obj.final_winner);
			if(ga_obj.flag_STOPPING_CONDITION==true)
			{
				chromatic_number--;
				System.out.println("Found the Solution using Genetic Algorithm in " + num_iter + " iteratons .....");
			}
			else
			{
				if(flag_LAST_ATTEMPT_FAILED == false)
				{
					flag_LAST_ATTEMPT_FAILED = true;
				}
				else
				{
					flag_SOLUTION_ARRIVED = false;
				}
				chromatic_number++;
				System.out.println("COULD NOT FIND the Solution using Genetic Algorithm for "+
							num_iter + " iteratons  for number of colours ="+obj.K);
//				System.out.println("Reporting the best solution based on crowd vote ");
			}
			
		}
		System.out.println("Optimum chromatic number is " + chromatic_number);
		DisplayGraph frame = new DisplayGraph(obj,obj.K+1);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1280, 720);
		frame.setVisible(true);
	}
}
