package graph_color;

import java.util.ArrayList;
import java.util.List;

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
	
	private float mutation_index;
	/** <the maximum number of mutations per chromosome = mutation_index*chromosome.size */
	
	private int max_iter;
	/** < mximum number of iterations for which the GA is to be run*/
	
	public int[] fitness_score;
	/** <array storing the fitness score for each of the individuals of the generation*/
	
	public int generation_fitness;
	/** < fitness of the entire generation*/
	
	public Graph_chromosome[] junta;
	/** <the population of the GA in form  of an array*/
	
	Graph_chromosome final_winner;
	/** <stores the final best chromosome */
	
	public boolean flag_STOPPING_CONDITION;
	/** <raised to true when the GA objective is fulfilled*/
	
	/** \fn GA_KGraph(Graph_GA obj)
	 * 
	 * @param obj the input graph for which GA is to be run
	 */
	GA_KGraph(Graph_GA obj,int population, int iter, float mutation)
	{
		inp_graph = obj;
		population_size = population;
		max_iter = iter;
		mutation_index = mutation;
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
	public Graph_chromosome[] parentSelection()
	{
		Graph_chromosome[] parents = new Graph_chromosome[2];
		List<Integer> list_num = new ArrayList<Integer>();
		Integer[] shuffled_list = null;
		
		
		for(int i=0;i<population_size;i++)
		{
			list_num.add(i+1);
		}
		java.util.Collections.shuffle(list_num);
		shuffled_list = list_num.toArray(new Integer[list_num.size()]);
	
		Graph_chromosome parent1A = junta[shuffled_list[0]-1];
		Graph_chromosome parent1B = junta[shuffled_list[1]-1];
		Graph_chromosome parent2A = junta[shuffled_list[2]-1];
		Graph_chromosome parent2B = junta[shuffled_list[3]-1];
				
		if(getFitness(parent1A)>getFitness(parent1B))
			parents[0] = parent1A;
		else
			parents[0] = parent1B;
		
		if(getFitness(parent2A)>getFitness(parent2B))
			parents[1] = parent2A;
		else
			parents[1] = parent2B;
		return parents;	
	}
	/** \fn public Graph_chromosome[] generateChildren()
	 * \brief generates the next generation based on the current generation
	 * @return array of chromosomes of size population_size
	 */
	public Graph_chromosome[] generateChildren(int strategy)
	{
		Graph_chromosome[] parents = null;
		Graph_chromosome[] children = new Graph_chromosome[population_size/2];
		
		for(int i=0;i<population_size/2;i++)
		{
			if(strategy==1)
				parents = parentSelection();
			else
				parents = parentSelection2();
			
			children[i] = Graph_chromosome.generateChild(parents[0], parents[1]);
		}
		return children;
	}
	
	/** \fn public int runGAGenerations()
	 * 	 * \brief main function executing the main logic of the GA
	 * return: number of iterations for which the GA was run
	 */
	public int runGAGenerations(int print_state)
	{
		Graph_chromosome[] children;
		int iter = 1;
	
		while(iter<= max_iter)
		{
			if(print_state == 1)
				System.out.print("Running GA for iteration ->" + iter+".....");
			
			calculateFitness();
			
			sortPopulation();
			
			if(print_state == 1)
				System.out.println(" || Fittest chromosome " + fitness_score[population_size-1]+
						"|| generation fitness value = " + generation_fitness);
			
			if(checkFinalCondition())
				return iter;
			
			if(fitness_score[population_size-1]>-3)
			{
				children = generateChildren(1);
				mutation_index = 1.0f;
			}
			else
			{
				children = generateChildren(1);
			}
			
			for(int i=0;i<population_size/2;i++)
			{
				children[i].mutation(inp_graph, mutation_index);
				junta[i] = children[i];
			}
			iter++;
		}
		return iter;
	}
	
	/** \fn public void generateInitialPopulation()
	 * \brief generates the initial population with random chromosomes
	 */
	public void generateInitialPopulation()
	{
		System.out.println("Starting GA with initial population = " +population_size+
				" , maximum_iterations = "+max_iter + " , mutation index = " +
				mutation_index + " , chromatic_number = "+ inp_graph.K);
		for(int i=0;i<population_size;i++)
		{
			junta[i].random_init();
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
		final_winner = junta[index];
		if(temp_max == 0)
		{
			flag_STOPPING_CONDITION = true;
		}
		return flag_STOPPING_CONDITION;
	}
	/** \fn public void calculateFitness()
	 *\brief calculates the fitness score for each of the individual
	 */
	public void calculateFitness()
	{
		generation_fitness = 0;
		for(int i=0;i<population_size;i++)
		{
			fitness_score[i] = getFitness(junta[i]);
			generation_fitness += fitness_score[i]; 
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
	
	/**\fn public Graph_chromosome[] parentSelection2()
	 * \brief returns top two parents according to the fitness score
	 * @return array of two chromosomes
	 */
	public Graph_chromosome[] parentSelection2()
	{
		Graph_chromosome[] parents = new Graph_chromosome[2];
		
		parents[0] = final_winner;
		
		int temp_max = fitness_score[0];
		int index_max = 0;
		
		int index_max2 = 0;
		
		for(int i=0;i<population_size;i++)
		{
			if(temp_max<fitness_score[i])
			{
				temp_max = fitness_score[i];
				index_max2 = index_max;
				index_max = i;
			}
		}
		parents[0] = junta[index_max];
		parents[1] = junta[index_max2];
		
		return parents;
	}
	/** \fn public void sortPopulation()
	 * \brief sort the population according to their fitness score
	 */
	public void sortPopulation()
	{
		int[] hash_table = new int[population_size];
		int[] temp_fitness = new int[population_size];
		Graph_chromosome[] temp_junta = new Graph_chromosome[population_size];
		for(int i=0;i<population_size;i++)
		{
			hash_table[i] = i;
			temp_fitness[i] = fitness_score[i];
		}
		for(int i=0;i<population_size-1;i++)
		{
			int min = i;
			for(int j=i+1; j<population_size ;j++)
			{
				if(temp_fitness[j]<temp_fitness[min])
					min = j;
			}
			int temp_index = hash_table[i];
			hash_table[i]  = hash_table[min];
			hash_table[min] = temp_index;
			int temp = temp_fitness[i];
			temp_fitness[i] = temp_fitness[min];
			temp_fitness[min] = temp;
		}
		for(int i=0;i<population_size;i++)
		{
			temp_fitness[i] = fitness_score[i];
			temp_junta[i] = junta[i];
		}
		for(int i=0;i<population_size;i++)
		{
			fitness_score[i] = temp_fitness[hash_table[i]];
			junta[i] = temp_junta[hash_table[i]];
		}
	}

}
