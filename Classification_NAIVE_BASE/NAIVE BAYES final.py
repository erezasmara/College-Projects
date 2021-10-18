
## sudo python3 -m pip install -U pandas
## sudo python3 -m pip install -U scikit-learn
## sudo python3 -m pip install -U termcolor
import pandas as pd
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.naive_bayes import MultinomialNB
from sklearn import metrics
from termcolor import colored

#####################################
'''this project made by: Erez Asmara '''
#####################################

######################################################################
#Load all the csv files
train = pd.read_csv('./train.csv')
test = pd.read_csv('./test.csv')
######################################################################


#######################################################################
#Split the data & transform
d = train
split = 0.7
d_train = d[:int(split*len(d))]
d_test = d[int((1-split)*len(d)):]
vectorizer = CountVectorizer()
features = vectorizer.fit_transform(d_train.comment_text)
test_features = vectorizer.transform(d_test.comment_text)
######################################################################



######################################################################
#BUILD NAÏVE BAYES CLASSIFIER MODEL
model1 = MultinomialNB()
model1.fit(features, d_train.toxic)


#Model Performence
expected = d_train.toxic
predicted = model1.predict(features)
print("======>Performence Report & Accuracy<=======")
print(metrics.classification_report(expected, predicted)+"\n")
######################################################################



######################################################################
#Build a predict function
def Predict_Func(string):
 answer=model1.predict(vectorizer.transform([string]))[0]
 if(answer==1):
  print(colored('\nYour response is offensive and toxic Please use appropriate language!!!!!\n','red'))
 else:
  print(colored('\nYour response was successful We will post this in the next few hours :) :)\n','green'))
 return
######################################################################



######################################################################
#check  some words

import time
import sys

print("--==Toxic Comment Filtering With NAÏVE BAYES ==--\n")

startText=""
while True:
  print(startText+"Please write Your Comment, else write 'exit'")
  commment = input()
  print("\n")

  animation = "|/-\\"

  for i in range(100):
      time.sleep(0.1)
      sys.stdout.write("\r"+"Wait For Answer Please" + animation[i % len(animation)])
      sys.stdout.flush()



  if commment == "exit":
    print("\nWe are sorry to hear that you are going, Good bye.")
    break

  if commment != "exit":
    Predict_Func(commment)
    startText="Hi Again ,"
######################################################################
