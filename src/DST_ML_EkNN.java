

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import Link_List_Source.Link_List_8_Operations;
import Link_List_Source.Node8;
import Meric_Computation.Necessary_Metric_Computation;
import weka.core.Instance;
import weka.core.Instances;

public class DST_ML_EkNN {
	
	Instances m_trainSet;
	Instances m_intrain_set;
	Instances m_intest_set;
	public DST_ML_EkNN(String dirString,String file_name) {
		try {
			
			m_trainSet = new Instances(new BufferedReader(new FileReader(dirString+file_name)));
			m_trainSet.setClassIndex(m_trainSet.numAttributes() - 1);
			
			m_intrain_set=new Instances(m_trainSet);
			m_intrain_set.delete();
			
			m_intest_set=new Instances(m_trainSet);
			m_intest_set.delete();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		
		Scanner reader = new Scanner(System.in); 
		String file_name_train=null,file_name_test=null;
		Scanner in = new Scanner(System.in);
		System.out.println("Enter directory path name : ");
		String directory_name = in.nextLine(); 
		System.out.println("Enter input file (.arff) format (train file) : ");
		file_name_train=reader.nextLine();
		System.out.println("Enter input file (.arff) format (test file)  : ");
		file_name_test=reader.nextLine();
		in.close();
		
		DST_ML_EkNN auc_Calculation=new DST_ML_EkNN(directory_name,file_name_train);
		Instances copydata=new Instances(auc_Calculation.m_trainSet);
		copydata.stratify(2);
		
		int k=7;
		for(int fold=0;fold<2;fold++)
		{
			Instances test = null;
			if(fold==0) {
				System.out.println("If is satisfied");
			try {
				auc_Calculation.m_intrain_set=new Instances(new BufferedReader(new FileReader(directory_name+file_name_train)));
		        auc_Calculation.m_intrain_set.setClassIndex(auc_Calculation.m_trainSet.numAttributes() - 1);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				test=new Instances(new BufferedReader(new FileReader(directory_name+file_name_test)));
			    test.setClassIndex(auc_Calculation.m_trainSet.numAttributes()-1);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			}
			else {
				System.out.println("Else is satisfied");
				try {
					auc_Calculation.m_intrain_set=new Instances(new BufferedReader(new FileReader(directory_name+file_name_test)));
			        auc_Calculation.m_intrain_set.setClassIndex(auc_Calculation.m_trainSet.numAttributes() - 1);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					test=new Instances(new BufferedReader(new FileReader(directory_name+file_name_train)));
				    test.setClassIndex(auc_Calculation.m_trainSet.numAttributes()-1);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
			auc_Calculation.m_intest_set = new Instances(test);
			
			Instances inTrainMinor = new Instances(auc_Calculation.m_intrain_set);
			Instances inTrainMajor = new Instances(auc_Calculation.m_intrain_set);
			inTrainMinor.delete();
			inTrainMajor.delete();
			
			for (int i = 0; i < auc_Calculation.m_intrain_set.numInstances(); i++) {
				if (auc_Calculation.m_intrain_set.instance(i).classValue() == 1) {
					inTrainMinor.add(auc_Calculation.m_intrain_set.instance(i));
				} else {
					inTrainMajor.add(auc_Calculation.m_intrain_set.instance(i));
				}
			}
			
			System.out.println("Number of minority samples : "+inTrainMinor.numInstances());
			System.out.println("Number of majority samples : "+inTrainMajor.numInstances());
			
			
			for (int i = 0; i < test.numInstances(); i++) {
				if (test.instance(i).classValue() == 1) {
					auc_Calculation.m_intest_set.add(test.instance(i));
					auc_Calculation.m_intest_set.add(test.instance(i));
				}
			}
			
			//polling starts from here
			double 
			distance=0.0d;
			double actual_labels[]=new double[auc_Calculation.m_intest_set.numInstances()];                   
			double predicted_labels[]=new double[auc_Calculation.m_intest_set.numInstances()];                
			
			for(int i=0;i<auc_Calculation.m_intest_set.numInstances();i++)
			{
				List neighbour_list_for_testing_data_sample=new LinkedList();
				
				Instance instanceX=auc_Calculation.m_intest_set.instance(i);
				actual_labels[i]=instanceX.classValue();                                                     
				
				for(int j=0;j<auc_Calculation.m_intrain_set.numInstances();j++)
				{
					Instance instanceY=auc_Calculation.m_intrain_set.instance(j);
					for(int attribute_number=0;attribute_number<auc_Calculation.m_intrain_set.numAttributes();attribute_number++)
					{
	                  if(attribute_number==(auc_Calculation.m_intrain_set.classIndex()))
	                	  continue;
	                  else
	                  {
	                	  distance+=Math.pow(instanceX.value(attribute_number)-instanceY.value(attribute_number), 2);
	                  }
					}
					
					distance=Math.pow(distance,0.5);
	                neighbour_list_for_testing_data_sample.add(new Object[]{distance,instanceY});
	                distance=0.0d;
				}
				
				// sort the neighbors according to distance
				  Collections.sort(neighbour_list_for_testing_data_sample, new Comparator() {
						public int compare(Object o1, Object o2) {
						  double distance1 = (Double) ((Object[]) o1)[0];
						  double distance2 = (Double) ((Object[]) o2)[0];
					          return Double.compare(distance1, distance2);
						}
					      });
				  
				  
				  Iterator entryIterator = neighbour_list_for_testing_data_sample.iterator();
				  Link_List_8_Operations link_List_8_Operations=new Link_List_8_Operations();
				  int neighbors=0;
				  while(entryIterator.hasNext() && neighbors<k)
				  {
					   Object data[]=(Object [])entryIterator.next();
					   link_List_8_Operations.create_link_list(data);
					   neighbors++;
				  }
				  link_List_8_Operations.create_centroid(k);
				  link_List_8_Operations.Compute_Simlarity(k);
				  
				  link_List_8_Operations.k_means(k);
				  link_List_8_Operations.NumActive=0;
				  
				  
         		  Node8 desired_node=link_List_8_Operations.find_cluster(1);
				  Iterator itr=desired_node.cluster_members.iterator();
				  int minority=0;
				  int majority=0;
				  if(itr.hasNext())
				  {
					  Object data[]=(Object [])itr.next();
					  Instance instance=(Instance)data[1];
					  if(instance.classValue()==1)
						  minority++;
					  else
						  majority++;
				  }
				  if(majority<minority)      // assign minority class label
					  predicted_labels[i]=1;
				  else                       // assign majority class label
					  predicted_labels[i]=0; 
			}
			
			
			Necessary_Metric_Computation computation=new Necessary_Metric_Computation(actual_labels, predicted_labels);
			
			System.out.println("\nK         : "+k);
			System.out.println("\nFold      : "+fold);
			System.out.println("TP        : "+computation.TP);
			System.out.println("FP        : "+computation.FP);
			System.out.println("TN        : "+computation.TN);
			System.out.println("FN        : "+computation.FN);
			System.out.println("Precision : "+ computation.Precision());
			System.out.println("Recall    : "+computation.Recall());
			System.out.println("F-Score   : "+computation.F_Score());
			System.out.println("Accuracy  : "+computation.Accuracy());
			
			//polling ends here
			
	}
	
}
}

