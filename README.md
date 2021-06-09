# DST-ML-EkNN

This is a smart clustering method which relies on metric learning eg. Neighbourhood Component Analysis (NCA) to solve class imbalanced data problem. To know about it, please follow the link: https://link.springer.com/chapter/10.1007/978-981-15-3514-7_26. Cite our paper, if you are using it in your research work.

The citation of our paper is:

[1] Susan, Seba, and Amitesh Kumar. "DST-ML-EkNN: data space transformation with metric learning and elite k-nearest neighbor cluster formation for classification of     imbalanced datasets." In Advances in Artificial Intelligence and Data Engineering, pp. 319-328. Springer, Singapore, 2021.

To know more about imbalanced data, please follow this link: https://onlinelibrary.wiley.com/doi/full/10.1002/eng2.12298. The citation of this paper is:

[2] Susan, Seba, and Amitesh Kumar. "The balancing trick: Optimized sampling of imbalanced datasets—A brief survey of the recent State of the Art." Engineering Reports 3, no. 4 (2021): e12298.

Important !!!!!

Caution: only files with *.arff extensions are used in this program.

Method of evaluation:  We have used F-Score for evaluation purposse.

You have to provide training and testing datasets seprately. 

First, execute NCA_Transformation.py to transform these datasets.

After that, execute DST_ML_EkNN.java over these transformed files.

Use, Java 9 and Python 3.6 with Eclipse IDE for execution.

