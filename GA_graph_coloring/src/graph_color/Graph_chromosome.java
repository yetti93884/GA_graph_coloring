package graph_color;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.jgrapht.graph.DefaultEdge;

/** class Graph_chromosome 
 * \brief stores the staus of colors of the graph in the form of colors
 * @author Atulya Shivam Shree
 * @version 1.0
 * 17th April, 2014
 *
 */
public class Graph_chromosome {
	
	private int chromosome_size;
	/** <stores the size of the chromosome*/
	
	private int num_colors;
	/** <stores the maximum variation in colors in the graph>*/
	
	private int[] chromosome;
	/** <stores a particular state of colors in the graph as a vector of colors*/
	
	/** \fn Graph_chromosome(Graph_GA obj)
	 * 
	 * @param obj the Graph_GA for which the chromosome must be built
	 */
	Graph_chromosome(int size, int colors)
	{
		chromosome_size = size;
		num_colors = colors;
		chromosome = new int[size];
	}
	
	/** \fn public void random_init()
	 * \brief randomly initialize the state of the chromosome
	 */
	public void random_init()
	{
		Random random_generator = new Random();
		
		for(int i=0;i<chromosome_size;i++)
		{
			chromosome[i] = random_generator.nextInt(num_colors)+1;
		}
	}
	
	/** \fn public int getChromosome(int pos)
	 * \brief get the value of chromosome at position pos
	 * @param pos index number of the chromosome
	 * @return value of color at index pos
	 */
	public int getChromosomeAt(int pos)
	{
		if(pos>chromosome_size||pos<1)
			return 0;
		else
			return chromosome[pos-1];
	}
	
	/** \fn public int getFitness()
	 * \brief returns the fitness of the chromosome in our graph
	 * @return fitness score is the negative of the number of bad edges
	 */
	public int getFitness(Graph_GA obj)
	{
		int fitness_score = 0;
		Set<DefaultEdge> edges= obj.graph_inp.edgeSet();
        Iterator<DefaultEdge> edges_list = edges.iterator();
        
        while(edges_list.hasNext())
        {
        	DefaultEdge new_edge = edges_list.next();
        	GA_Graph_Node source = obj.graph_inp.getEdgeSource(new_edge);
        	GA_Graph_Node target = obj.graph_inp.getEdgeTarget(new_edge);
        	if(chromosome[source.numID-1] == chromosome[target.numID-1])
        	{
        		fitness_score = fitness_score-1;
        	}
        }
		return fitness_score;
	}
	
	/** \fn public void mutation()
	 * \brief mutates the chromosome according to the given graph as input
	 */
	public void mutation(Graph_GA obj, float mutation_index)
	{
		
		List<Integer> list_num = new ArrayList<Integer>();
		Integer[] shuffled_list = null;
		List<Integer> mutation_vertex = new ArrayList<Integer>();
		
		for(int i=0;i<chromosome_size;i++)
		{
			list_num.add(i+1);
		}
		java.util.Collections.shuffle(list_num);
		shuffled_list = list_num.toArray(new Integer[list_num.size()]);
		
		for(int i=0;i<(chromosome_size*mutation_index);i++)
		{
			mutation_vertex.add(shuffled_list[i]);
		}
		
		
		Random random_generator = new Random();
		
		GA_Graph_Node vertex_container[] = obj.getNodes();
		
		for(int i=0;i<obj.num_vertex;i++)
		{
			Integer[] valid_colors = null;
			List<Integer> adjacent_vertex_color = new LinkedList<Integer>();
			Set<DefaultEdge> adjacent_edges = obj.graph_inp.edgesOf(vertex_container[i]);
			Iterator<DefaultEdge> adj_edge_list = adjacent_edges.iterator();
			
			while(adj_edge_list.hasNext())
			{
				DefaultEdge adj_edge = adj_edge_list.next();
				GA_Graph_Node adj_node = null;
				adj_node = obj.graph_inp.getEdgeSource(adj_edge);
				if(adj_node==null||adj_node==vertex_container[i])
				{
					adj_node = obj.graph_inp.getEdgeTarget(adj_edge);
				}
				adjacent_vertex_color.add(chromosome[adj_node.numID-1]);
			}
			if(adjacent_vertex_color.contains(chromosome[i])&&mutation_vertex.contains(i+1))
        	{
				List<Integer> valid_color_list = new LinkedList<Integer>();
        		for(int j=0;j<num_colors;j++)
        		{
        			if(adjacent_vertex_color.contains(j+1) == false)
        				valid_color_list.add(j+1);	
            	}
        		
        		valid_colors = valid_color_list.toArray(new Integer[valid_color_list.size()]);
        		
//        		System.out.println(valid_colors.toString());
        		if(valid_colors.length> 0)
        		{
	        		int rand_num = random_generator.nextInt(valid_colors.length);
	    			int new_color = valid_colors[rand_num];
	    			
	    			chromosome[i] = new_color;
        		}
        	}
			
        }
	}
	
	/** \fn public static Graph_chromosome generateChild(Graph_chromosome parent1,Graph_chromosome parent2, int crossover_point)
	 * \brief produces a child chromsome given the parents 
	 * @param parent1 parent chromosome 1
	 * @param parent2 parent chromosome 2
	 * @param crossover_point the point from where the two chromosomes mix into each other
	 * @return child chromosome
	 */			
	public static Graph_chromosome generateChild(Graph_chromosome parent1,
			Graph_chromosome parent2 )
	{
		Random random_generator = new Random();
		int crossover_point = random_generator.nextInt(parent1.chromosome_size);
		
		Graph_chromosome child = new Graph_chromosome(parent1.chromosome_size,parent1.num_colors);
		
		for(int i=0;i<child.chromosome_size;i++)
		{
			if(i<crossover_point)
				child.chromosome[i] = parent1.getChromosomeAt(i+1);
			else
				child.chromosome[i] = parent2.getChromosomeAt(i+1);
		}
		return child;
	}

}
