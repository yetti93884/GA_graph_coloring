package graph_color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** class GA_KGraph
 * \brief implementation of the GA algorithm on the Graph
 * @author Atulya Shivam Shree
 * @version 1.0
 * 17th April, 2014
 *
 */
public class GA_KGraph {
	
	private Graph_GA inp_graph;
	/** <the graph to which the chromosome belongs*/
	
	private int population_size;
	/** <the number of chromosomes in the GA initially*/
	
	private int max_iter;
	/** < mximum number of iterations for which the GA is to be run*/
	
	private int[] fitness_score;
	
	private Graph_chromosome[] junta;
	/** <the population of the GA in form  of an array*/
	
	Graph_chromosome final_winner;
	/** <stores the final best chromosome */
	
	public boolean flag_STOPPING_CONDITION;
	/** <raised to true when the GA objective is fulfilled*/
	
	/** \fn GA_KGraph(Graph_GA obj)
	 * 
	 * @param obj the input graph for which GA is to be run
	 */
	GA_KGraph(Graph_GA obj,int population, int iter)
	{
		inp_graph = obj;
		population_size = population;
		max_iter = iter;
		fitness_score = new int[population_size];
		
		junta = new Graph_chromosome[population_size];
		
		for(int i=0;i<population_size;i++)
		{
			junta[i] = new Graph_chromosome(inp_graph.num_vertex,inp_graph.K);
		}
	}
	
	/** \fn void init(int population, int iter )
	 * 
	 * @param population population size for the GA
	 * @param iter maximum number of iterations
	 */
	void init(int population, int iter )
	{
		population_size = population;
		max_iter = iter;
	}
	/** \fn int getFitness(Graph_chromosome)
	 * \brief gives the fitness of the chromosom
	 * @return negative of the number of bad edges
	 */
	private int getFitness(Graph_chromosome obj)
	{
		int score = 0;
		score = obj.getFitness(inp_graph);
		return score;
	}
	
	/** \fn private Graph_chromosome[] parentSelection()
	 * \brief select two parents
	 * @return array of two chromosomes
	 */
	private Graph_chromosome[] parentSelection()
	{
		Random random_generator = new Random();
		Graph_chromosome[] parents = new Graph_chromosome[2];
		boolean flag_PARENTS_FOUND = false;
		List<Integer> list_num = new ArrayList<Integer>();
		Integer[] shuffled_list = null;
		
		while(flag_PARENTS_FOUND == true)
		{
			for(int i=0;i<population_size;i++)
			{
				list_num.add(i+1);
			}
			java.util.Collections.shuffle(list_num);
			shuffled_list = list_num.toArray(new Integer[list_num.size()]);
		}
		
		GA_Graph_Node vertex_container[] = inp_graph.getNodes();
		Graph_chromosome parent1A = junta[shuffled_list[0]-1];
		Graph_chromosome parent1B = junta[shuffled_list[0]-1];
		Graph_chromosome parent2A = junta[shuffled_list[0]-1];
		Graph_chromosome parent2B = junta[shuffled_list[0]-1];
				
		if(getFitness(parent1A)>getFitness(parent1B))
			parents[0] = parent1A;
		else
			parents[0] = parent1B;
		
		if(getFitness(parent2A)>getFitness(parent2B))
			parents[0] = parent2A;
		else
			parents[0] = parent2B;
		return parents;	
	}
	/** \fn public Graph_chromosome[] generateChildren()
	 * \brief generates the next generation based on the current generation
	 * @return array of chromosomes of size population_size
	 */
	public Graph_chromosome[] generateChildren()
	{
		Graph_chromosome[] parents = null;
		Graph_chromosome[] children = new Graph_chromosome[population_size];
		
		for(int i=0;i<population_size;i++)
		{
			parents = parentSelection();
			children[i] = Graph_chromosome.generateChild(parents[0], parents[1]);
		}
		return children;
	}
	
	/** \fn public void runGAGeneration()
	 * \brief main function executing the main logic of the GA
	 */
	public void runGAGeneration()
	{
		Graph_chromosome[] children;
		
		children = generateChildren();
		
		for(int i=0;i<population_size;i++)
		{
			children[i].mutation(inp_graph);
			junta[i] = children[i];
		}
	}
	
	/** \fn public void generateInitialPopulation()
	 * \brief generates the initial population with random chromosomes
	 */
	public void generateInitialPopulation()
	{
		for(int i=0;i<population_size;i++)
		{
			junta[i].random_init();
		}
		
		for(int i=0;i<inp_graph.K;i++)
		{
			System.out.println(junta[4].getChromosomeAt(i));
		}
	}
	
	/** \fn public boolean checkFinalCondition()
	 * \brief checks the Stopping condition for GA algorithm 
	 * @return true when STOPPING condition is reached
	 */
	public boolean checkFinalCondition()
	{
		calculateFitness();
		int temp_max = fitness_score[0];
		int index = 0;
		
		for(int i=0;i<population_size;i++)
		{
			if(temp_max<fitness_score[i])
			{
				temp_max = fitness_score[i];
				index = i;
			}
		}
		
		if(temp_max == 0)
		{
			return true;
		}
		final_winner = junta[index];
		return false;
	}
	/** \fn public void calculateFitness()
	 *\brief calculates the fitness score for each of the individual
	 */
	public void calculateFitness()
	{
		for(int i=0;i<population_size;i++)
		{
			fitness_score[i] = getFitness(junta[i]);
		}
	}
	
	/** \fn public void setChromosomeToGraph(Graph_chromosome indiv)
	 * \brief sets the colors in the graph to the chromosome value
	 * @param indiv chromosome whose colors are to be used
	 */
	public void setChromosomeToGraph(Graph_chromosome indiv)
	{
		GA_Graph_Node vertex_container[] = inp_graph.getNodes();
		for(int i=0;i<inp_graph.num_vertex;i++)
		{
			vertex_container[i].setColor(indiv.getChromosomeAt(i+1));
		}
	}
	
	

}
