'''
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

NOTE: Your are free to modify this code and publish it.

'''

import numpy as np
''' Reading From Arff Files '''
from scipy.io import arff
from io import StringIO
from numpy import size

dir_name=input("Enter directory path to file : ")
training_file_name=input("Enter training file name (training file ends with *_even.arff) : ")
testing_file_name=input("Enter testing file name   (testing file ends with *_odd.arff)   : ")

f1 = open(dir_name+training_file_name, "r")
f1 = StringIO(f1.read())
first_line=f1.readline()
data_set_name=first_line[10:len(first_line)-1]
data1, meta1 = arff.loadarff(f1)
X1=[]
Y1=[]
for i in range(len(data1)):
    dataX=data1[i]
    row=[]
    for j in range(len(dataX)):
        if j==len(dataX)-1:
            Y1.append(dataX[j])
        else:
            row.append(dataX[j])
    X1.append(row)
K=[]

for i in range(len(Y1)):
    if Y1[i].decode("utf-8")=="0." or Y1[i].decode("utf-8")=="0":
        K.append(0)
    elif Y1[i].decode("utf-8")=="1." or Y1[i].decode("utf-8")=="1" :    
        K.append(1)     
Y1=K
X1=np.array(X1)


f2 = open(dir_name+testing_file_name, "r")
f2 = StringIO(f2.read())
data2, meta2 = arff.loadarff(f2)
X2=[]
Y2=[]
for i in range(len(data2)):
    dataX=data2[i]
    row=[]
    for j in range(len(dataX)):
        if j==len(dataX)-1:
            Y2.append(dataX[j])
        else:
            row.append(dataX[j])
    X2.append(row)
K=[]
for i in range(len(Y2)):
    if Y2[i].decode("utf-8")=="0." or Y2[i].decode("utf-8")=="0":
        K.append(0)
    elif Y2[i].decode("utf-8")=="1." or Y2[i].decode("utf-8")=="1" :    
        K.append(1)     
Y2=K
X2=np.array(X2)

f1.close()
f2.close()

''' Preparing Format For NCA'''
'''Importing Metric Learning'''
from metric_learn import NCA
nca = NCA(max_iter=1000)
Xz=nca.fit(X1, Y1)
X_Matrix=Xz.get_mahalanobis_matrix()
''' Transformed to Data '''
K1=np.dot(X1,X_Matrix.T)
K2=np.dot(X2,X_Matrix.T)

attr_names=meta1.names()
print("Attributes Name  : ",attr_names)
attr_types=meta1.types()
print("Attributes Types : ",attr_types)

'''Writing Transformed data back into the file'''
meta_data_for_file="@relation "+data_set_name+"\n"
for i in range(len(attr_names)):
    if (attr_names[i]=="class" and attr_types[i]=="nominal"):
        meta_data_for_file+="@attribute "+attr_names[i]+" {0,1} \n@data \n"
    else:    
        meta_data_for_file+="@attribute "+attr_names[i]+" "+attr_types[i]+"\n"

data_samples_collection_training=""
for i in range(len(K1)):
    row=K1[i]
    cols=""
    for j in range(len(row)):
        if j!=len(row)-1:
           cols+=str(row[j])+","
        else:
           cols+=str(row[j])+","+str(Y1[i])+"\n"    
    data_samples_collection_training+=cols

f1 = open(training_file_name, "w")
f1.write(meta_data_for_file+data_samples_collection_training)
f1.close()

data_samples_collection_testing=""
for i in range(len(K2)):
    row=K2[i]
    cols=""
    for j in range(len(row)):
        if j!=len(row)-1:
           cols+=str(row[j])+","
        else:
           cols+=str(row[j])+","+str(Y2[i])+"\n"    
    data_samples_collection_testing+=cols

f2 = open(testing_file_name, "w")
f2.write(meta_data_for_file+data_samples_collection_testing)
f2.close()

print("Transformation of Training Set has been done sucessfully !!!!!")
