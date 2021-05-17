package Link_List_Source;

import java.util.LinkedList;
import java.util.List;

import weka.core.Instance;

public class Node8 {
	
	public int cluster_ID;
	public Instance centroid;
	public List cluster_members;
	public int similar_cluster_id;
	public double similarity;
	public Node8 next;
	
	Node8()
	{
		cluster_members=new LinkedList();
	}

}
