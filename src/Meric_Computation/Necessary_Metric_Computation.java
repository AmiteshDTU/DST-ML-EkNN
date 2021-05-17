/*
This is an implementation of metric-learning with elite clustering to solve imbalanced data problem.
If you want to use this in the research work, please site the paper below:

Susan, Seba, and Amitesh Kumar. "DST-ML-EkNN: data space transformation with metric learning and
elite k-nearest neighbor cluster formation for classification of imbalanced datasets."
In Advances in Artificial Intelligence and Data Engineering, pp. 319-328. Springer, Singapore, 2021.

link: https://link.springer.com/chapter/10.1007/978-981-15-3514-7_26

Caution: only files with *.arff extensions are used in this program

Method of evaluation:  We have used F-Score for evaluation purposse

You have to provide training and testing datasets seprately. 
First, execute NCA_Transformation.py to transform these datasets.
After that, execute DST_ML_EkNN.java over these transformed files.
Use, Java 9 and Python 3.6 with Eclipse IDE for execution.

NOTE: Your are free to modify this code and publish it.
*/
package Meric_Computation;

public class Necessary_Metric_Computation {
	
	public int TP;
	public int FP;
	public int TN;
	public int FN;
	
	public Necessary_Metric_Computation(double actual_labels[], double predicted_labels[]) {
		
		int positive_class=1;       // minority class
		int negative_class=0;       // majority class
		
		for(int i=0;i<predicted_labels.length;i++)
		{
			if(predicted_labels[i]==positive_class && actual_labels[i]==positive_class)
				TP++;
			else if(predicted_labels[i]==positive_class && actual_labels[i]==negative_class)
				FP++;
			else if(predicted_labels[i]==negative_class && actual_labels[i]==positive_class)
				FN++;
			else if(predicted_labels[i]==negative_class && actual_labels[i]==negative_class)
				TN++;
		}
	}
	
	public double Precision() {
		
		if(TP==0)
			return 0;
		else
		{
		 double tp1=TP;
		 double fp1=FP;
		 double precision=(tp1/(tp1+fp1));
		 return precision;
		} 
	}
	
	public double Recall() {
		
		if(TP==0)
			return 0;
		else
		{
		 double tp1=TP;
		 double fn1=FN;
		 double recall=(tp1/(tp1+fn1));
		 return recall;
		}
	}
	
	public double F_Score() {
		double precision=Precision();
		double recall=Recall();
		if(precision==0 && recall==0)
				return 0;
		else
		{
		 double f_score=(2*((precision*recall)/(precision+recall)));
		 return f_score;
		}
	}
	
	public double Accuracy() {
		
		if(TP==0 && TN==0)
			return 0;
		else
		{	
		 double tp1=TP;
		 double tn1=TN;
		 double fp1=FP;
		 double fn1=FN;
		 double accuracy=((tp1+tn1)/(tp1+fp1+tn1+fn1));
		 return accuracy;
		} 
	}	
}
