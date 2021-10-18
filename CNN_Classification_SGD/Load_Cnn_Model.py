## sudo python3 -m pip install -U keras
## sudo python3 -m pip install -U tensorflow


import pandas as pd
from keras.preprocessing.text import Tokenizer
from keras.preprocessing.sequence import pad_sequences
from keras.models import load_model

#####################################
'''this project made by:
Erez Asmara '''
#####################################

######################################################################
# Pull data
train = pd.read_csv('./train.csv')
test = pd.read_csv('./test.csv')
list_classes = ["toxic", "severe_toxic", "obscene", "threat", "insult", "identity_hate"]
y = train[list_classes].values
list_sentences_train = train["comment_text"]
list_sentences_test = test["comment_text"]
max_features = 20000
tokenizer = Tokenizer(num_words=max_features)
tokenizer.fit_on_texts(list(list_sentences_train))
list_tokenized_train = tokenizer.texts_to_sequences(list_sentences_train)
list_tokenized_test = tokenizer.texts_to_sequences(list_sentences_test)
######################################################################

######################################################################
#load model
model=load_model('/Users/erezasmara/Desktop/Ruppin_School_project/CNN_Classification_SGD/CNN_Model_SGD.h5')

# Network Layers
print(model.summary())

#Function Predict
def Predict_Func(string):

    # Process string
    new_string = [string]
    new_string = tokenizer.texts_to_sequences(new_string)
    new_string = pad_sequences(new_string, 200, padding='post', truncating='post')

    # Predict
    prediction = model.predict(new_string)

    # Print output
    print("Toxicity levels for '{}':".format(string))
    print('Toxic:         {:.0%}'.format(prediction[0][0]))
    print('Severe Toxic:  {:.0%}'.format(prediction[0][1]))
    print('Obscene:       {:.0%}'.format(prediction[0][2]))
    print('Threat:        {:.0%}'.format(prediction[0][3]))
    print('Insult:        {:.0%}'.format(prediction[0][4]))
    print('Identity Hate: {:.0%}'.format(prediction[0][5]))
    print("\n")

    return



######################################################################


######################################################################
#check  some words

import time
import sys

print("---====Toxic Comment Filtering====---\n")

startText=""
while True:
  print(startText+"Please write Your Comment, else write 'exit'")
  comment = input()
  print("\n")

  animation = "|/-\\"

  for i in range(100):
      time.sleep(0.1)
      sys.stdout.write("\r"+"Loading" + animation[i % len(animation)])
      sys.stdout.flush()

  sys.stdout.write("")

  if comment == "exit":
    print("\nWe are sorry to hear that you are going, Good bye.")
    break

  if comment != "exit":
    Predict_Func(comment)
    print("End!\n")
    startText="Hi Again ,"
######################################################################


######################################################################