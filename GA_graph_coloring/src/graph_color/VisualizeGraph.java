package graph_color;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JApplet;
import javax.swing.JFrame;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.ListenableDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableUndirectedGraph;

/**
 * A demo applet that shows how to use JGraph to visualize JGraphT graphs.
 *
 * @author Barak Naveh
 *
 * @since Aug 3, 2003
 */
public class VisualizeGraph extends JApplet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Color     DEFAULT_BG_COLOR = Color.decode( "#FAFBFF" );
    private static final Dimension DEFAULT_SIZE = new Dimension( 1200, 800 );

    // 
    private JGraphModelAdapter m_jgAdapter;
    /**
     * @see java.applet.Applet#init().
     */
    public void init(  ) {
        // create a JGraphT graph
    	
    	Graph_GA obj = new Graph_GA();
    	try {
			obj.readData("../dataset/myciel3.col");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	ListenableGraph g = obj.graph_inp;
        // create a visualization using JGraph, via an adapter
        m_jgAdapter = new JGraphModelAdapter( g );

        JGraph jgraph = new JGraph( m_jgAdapter );

        adjustDisplaySettings( jgraph );
        getContentPane(  ).add( jgraph );
        resize( DEFAULT_SIZE );

        // add some sample data (graph manipulated via JGraphT)

        // position vertices nicely within JGraph component
        int len = (int) Math.sqrt(obj.num_vertex);
        int width = (int)(obj.num_vertex-1)/len+1;
        int index = 1;
        
        System.out.println(len+ " "+ width);
        
        GA_Graph_Node vertex_container[] = obj.getNodes();
        
        for(int i=0;i<len;i++)
        {
        	for(int j=0;j<width;j++)
        	{   
        		System.out.println(i+ " "+ j);
        		if(obj.num_vertex>=index)
        			positionVertexAt(vertex_container[index-1],
        					(int)(i*(1280.0/len)), (int)(j*(800.0/width)));
        		index++;
        	}
        }
//        positionVertexAt( 1, 130, 40 );
//        positionVertexAt( 2, 60, 200 );
//        positionVertexAt( 3, 310, 230 );
//        positionVertexAt( 4, 380, 70 );

        // that's all there is to it!...
    }


    private void adjustDisplaySettings( JGraph jg ) {
        jg.setPreferredSize( DEFAULT_SIZE );

        Color  c        = DEFAULT_BG_COLOR;
        String colorStr = null;

        try {
            colorStr = getParameter( "bgcolor" );
        }
         catch( Exception e ) {}

        if( colorStr != null ) {
            c = Color.decode( colorStr );
        }

        jg.setBackground( c );
    }


    private void positionVertexAt( Object vertex, int x, int y ) {
        DefaultGraphCell cell = m_jgAdapter.getVertexCell( vertex);
        Map              attr = cell.getAttributes(  );
        Rectangle2D        b    = GraphConstants.getBounds( attr );

        GraphConstants.setBounds( attr, new Rectangle( x, y, (int)b.getWidth(), (int)b.getHeight() ) );

        Map cellAttr = new HashMap(  );
        cellAttr.put( cell, attr );
        m_jgAdapter.edit( cellAttr, null, null, null );
    }
}

