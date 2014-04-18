package graph_color;


import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;

import org.jgrapht.graph.DefaultEdge;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class DisplayGraph extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2707712944901661771L;

	public DisplayGraph(Graph_GA obj,int num_colors)
	{
		super("GRAPH CLOURING");

		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();
		
		String[] colors = getColors(num_colors);
		
		graph.getModel().beginUpdate();
		try
		{
			Object vertex[] = new Object[obj.num_vertex];
			GA_Graph_Node vertex_container[] = obj.getNodes();
			
			int len = (int) Math.sqrt(obj.num_vertex);
	        int width = (int)(obj.num_vertex-1)/len+1;
	        int index = 1;
	        
	        System.out.println("length : "+len+", width :"+width);
	                
	        for(int i=0;i<len;i++)
	        {
	        	for(int j=0;j<width;j++)
	        	{   
	        		if(obj.num_vertex>=index)
	        		{
	        			String colour = "defaultVertex;fillColor=#"+
	        						colors[vertex_container[index-1].colorID-1];
	        			vertex[index-1] = graph.insertVertex(parent, null,
	        					vertex_container[index-1].colorID,
	        					(int)(i*(1280.0/len)), (int)(j*(800.0/width)), 30,30,
	    						colour);
	        		}
	        		index++;
	        	}
	        }
			
	        Set<DefaultEdge> edges= obj.graph_inp.edgeSet();
	        Iterator<DefaultEdge> edges_list = edges.iterator();
	        while(edges_list.hasNext())
	        {
	        	DefaultEdge new_edge = edges_list.next();
	        	GA_Graph_Node source = obj.graph_inp.getEdgeSource(new_edge);
	        	GA_Graph_Node target = obj.graph_inp.getEdgeTarget(new_edge);
	        	graph.insertEdge(parent, null, "", vertex[source.numID-1], vertex[target.numID-1]);
	        }
	        
		}
		finally
		{
			graph.getModel().endUpdate();
		}

		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		getContentPane().add(graphComponent);
	}
	public String[] getColors(int num_colors)
	{
		String[] colors = new String[num_colors];
		for(int i=0;i<num_colors;i++)
		{
			int color = (int) (1.0*(i+1)/num_colors*4096-1);
			int dig1_num,dig2_num,dig3_num;
			char dig1,dig2,dig3;
			
			dig1_num = (int)(color/256.0);
			if(dig1_num<=9)
				dig1 = (char) ('0'+dig1_num);
			else
				dig1 = (char) ('A'+dig1_num-10);
			color = color%256;
			
			dig2_num = (int)(color/16.0);
			if(dig2_num<=9)
				dig2 = (char) ('0'+dig2_num);
			else
				dig2 = (char) ('A'+dig2_num-10);
			color = color%16;
			
			dig3_num = color;
			if(dig3_num<=9)
				dig3 = (char) ('0'+dig3_num);
			else
				dig3 = (char) ('A'+dig3_num-10);
			
			if(i%3==0)
				colors[i] = dig1+"F"+"000D";
			if(i%3==1)
				colors[i] = "00"+dig2+"F0F";
			if(i%3==2)
				colors[i] = "00F0"+dig3+"0";
		}
		return colors;
	}

	public static void main(String[] args)
	{
	}

}

