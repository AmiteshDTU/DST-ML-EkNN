package Link_List_Source;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import weka.core.Instance;
import weka.core.Instances;

public class Link_List_8_Operations {
	
	public Node8 head;
	public static int NumActive;
	public static Instance k[];
	
	public Link_List_8_Operations()
      {
    	  k=new Instance[2];
      }
	
	public void create_link_list(Object data[])
	{
		if(NumActive==0)
		{
		  Instance instance=(Instance)data[1];
		  double distance=(Double)data[0];	
			
		  NumActive++;
          Node8 Node8=new Node8();
          
          Node8.centroid=instance;
          Node8.cluster_members.add(new Object[] {distance,instance});
          Node8.cluster_ID=NumActive;
          Node8.next=null;
          head=Node8;
		}
		else if(NumActive!=0)
		{
			Instance instance=(Instance)data[1];
			double distance=(Double)data[0];
			
			Node8 traverse=head;
			while(traverse.next!=null)
			     traverse=traverse.next;
			
			Node8 node=new Node8();
			NumActive++;
			node.centroid=instance;
	        node.cluster_members.add(new Object[] {distance,instance});
	        node.cluster_ID=NumActive;
	        node.next=null;
			traverse.next=node;
		}
		
	}

	public void create_centroid(int size) {
		Node8 node=find_cluster(1);
		k[0]=node.centroid;
		node=find_cluster(size);
		k[1]=node.centroid;
	}
	
public void k_means(int size) {
		
		Node8 node1=head;
		while(node1.next!=null)
		{
		    double diff[]=new double[k.length];
		    
		    if(node1.cluster_ID==1 || node1.cluster_ID==size)
		    {
		    	node1=node1.next;
		    	continue;
		    }
		    else if(node1.cluster_ID!=1 || node1.cluster_ID!=size)
		    {
		     for(int i=0;i<k.length;i++)
		     {
		    	Instance node_centroid=node1.centroid;
		    	Instance centroid=k[i];
		    	double difference=0.0d;
		    	for(int attribute_number=0;attribute_number<centroid.numAttributes();attribute_number++)
		    	{
		    		if(attribute_number==centroid.classIndex())
		    			continue;
		    		else
		    		    difference+=Math.pow(node_centroid.value(attribute_number)-centroid.value(attribute_number), 2);
		    	}
		    	difference=Math.pow(difference, 0.5);
		    	diff[i]=difference;
		     }
		    
		     if(diff[0]<diff[1])                 // merge upper
		     {
		           Node8 node=find_cluster(1);
		           List merger=node1.cluster_members;
		           Iterator itr=merger.iterator();
		           while(itr.hasNext())
		           {
		        	   Object data[]=(Object [])itr.next();
		        	   node.cluster_members.add(data);
		           }
		           
		           // deletion of a node under considration
		           Node8 node2=head;
		           while(node2.next!=node1)
		        	   node2=node2.next;
		           Node8 temp=node1;
		           node2.next=node1.next;
		           node1=node2;
		           temp.next=null;
		    }
		    else                                // merge lower
		    {
		    	   Node8 node=find_cluster(size);
		           List merger=node1.cluster_members;
		           Iterator itr=merger.iterator();
		           while(itr.hasNext())
		           {
		        	   Object data[]=(Object [])itr.next();
		        	   node.cluster_members.add(data);
		           }
		           
		        // deletion of a node under considration
		           Node8 node2=head;
		           while(node2.next!=node1)
		        	   node2=node2.next;
		           Node8 temp=node1;
		           node2.next=node1.next;
		           node1=node2;
		           temp.next=null;
		           
		    }
		}
		    node1=node1.next;
		}
	}

public Node8 find_cluster(int cluster_ID)
{
	Node8 node8=head;
	Node8 desired_node=null;
	while(node8.next!=null)
	{
		if(node8.cluster_ID==cluster_ID)
		{	
			desired_node=node8;
		    break;
		}
		node8=node8.next;
	}
	if(node8.next==null)
	{
		if(node8.cluster_ID==cluster_ID)
		{	
			desired_node=node8;
		}
	}
	return desired_node;
}

public void traverse_link_list()
{
	Node8 node=head;
	System.out.println("The Link List Is : ");
	while(node!=null)
	{
		System.out.println("\n\n");
		System.out.println("Cluster ID         : "+node.cluster_ID);
		System.out.println("Centroid           : "+node.centroid);
		System.out.println("Similar Cluster ID : "+node.similar_cluster_id);
		System.out.println("Similarity         : "+node.similarity);
		System.out.println("Cluster members : ");
		Iterator itr=node.cluster_members.iterator();
		while(itr.hasNext())
		{
			Object data[]=(Object [])itr.next();
			System.out.println("Instance : "+(Instance)data[1]);
			System.err.println("Distance : "+(Double)data[0]);
		}
		node=node.next;
	}
}

public void Compute_Simlarity(int size) {
	Node8 node1=head;
	while(node1!=null)
	{
		if(node1.cluster_ID==1 || node1.cluster_ID==size)
		{
			node1=node1.next;
			continue;
		}
	    else if(node1.cluster_ID!=1 || node1.cluster_ID!=size)
		{
			List neighbourhood=new LinkedList();
		    Node8 node2=head;
		while(node2.next!=null)
		{
			
			if(node1.cluster_ID!=node2.cluster_ID)
			{
				Instance node_1_centroid=node1.centroid;
				Instance node_2_centroid=node2.centroid;
				double difference=0.0d;
				for(int attribute_number=0;attribute_number<node1.centroid.numAttributes();attribute_number++)
				{
					if(attribute_number==node1.centroid.classIndex())
						continue;
					else
						difference+=Math.pow(node_1_centroid.value(attribute_number)-node_2_centroid.value(attribute_number), 2);
				}
				difference=Math.pow(difference, 0.5);
			   neighbourhood.add(new Object[] {difference,node2.cluster_ID});	
			}
			node2=node2.next;
		}
		
		// sort the neighbors clusters difference according to distance
		  Collections.sort(neighbourhood, new Comparator() {
				public int compare(Object o1, Object o2) {
				  double distance1 = (Double) ((Object[]) o1)[0];
				  double distance2 = (Double) ((Object[]) o2)[0];
			          return Double.compare(distance1, distance2);
				}
			      });
		  
		  Iterator itr=neighbourhood.iterator();
		  Object data[]=(Object [])itr.next();
		  node1.similar_cluster_id=(Integer)data[1];
		  node1.similarity=(Double)data[0];
		}
		node1=node1.next;
	}
	
}
	

}
