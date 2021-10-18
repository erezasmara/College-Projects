# Toxic Comments Classification CNN SGD
Project Structure:
```
├─ CNN_Classification
│  ├─ CNN_Model_SGD.h5
│  ├─ Final_Code_Toxic.py
│  ├─ Load_Cnn_Model.py
│  ├─ Loss&Acc.png
│  ├─ TrainData_Word_length.png
│  └─ TrainData_toxic_type.png
```
Description:
model that’s capable of detecting different types of of toxicity comments like threats, obscenity,  hate . base on CNN model

Requirement:
- Python 3.8 or above
- pip module
- data set train.csv & test.csv
- keras module
- tensorflow module


Installation with pip
```
## sudo python3 -m pip install -U keras
## sudo python3 -m pip install -U tensorflow
```

Final_Code_Toxic.py
```py
## sudo python3 -m pip install -U keras
## sudo python3 -m pip install -U tensorflow

import numpy as np, pandas as pd
import matplotlib.pyplot as plt
from tensorflow.keras.preprocessing.text import Tokenizer
from tensorflow.keras.preprocessing.sequence import pad_sequences
from tensorflow.keras.layers import Conv1D, MaxPooling1D
from tensorflow.keras.layers import Dropout, GlobalMaxPooling1D, BatchNormalization
from keras.layers.embeddings import Embedding
from tensorflow.keras.optimizers import SGD

from tensorflow.keras.models import Sequential
from keras.layers import Dense



#####################################
'''this project made by:
Erez Asmara 
'''
#####################################



######################################################################
#Loading the train & test set

train = pd.read_csv('./train.csv')
test = pd.read_csv('./test.csv')

#fill null values
train.isnull().any(),test.isnull().any()


######################################################################


######################################################################
#split data
list_classes = ["toxic", "severe_toxic", "obscene", "threat", "insult", "identity_hate"]
y = train[list_classes].values
list_sentences_train = train["comment_text"]
list_sentences_test = test["comment_text"]


max_features = 20000
tokenizer = Tokenizer(num_words=max_features)
tokenizer.fit_on_texts(list(list_sentences_train))
list_tokenized_train = tokenizer.texts_to_sequences(list_sentences_train)
list_tokenized_test = tokenizer.texts_to_sequences(list_sentences_test)




maxlen = 200
X_t = pad_sequences(list_tokenized_train, maxlen=maxlen)
X_te = pad_sequences(list_tokenized_test, maxlen=maxlen)
#Word Length data Graph
totalNumWords = [len(one_comment) for one_comment in list_tokenized_train]
plt.hist(totalNumWords,bins = np.arange(0,410,10))
#[0,50,100,150,200,250,300,350,400])#,450,500,550,600,650,700,750,800,850,900])
plt.show()
######################################################################


######################################################################

#  build our model
model = Sequential()
model.add(Embedding(max_features,128, input_length=maxlen))
# Convolutional layer
model.add(Conv1D(filters=128, kernel_size=5, padding='same', activation='relu'))
model.add(MaxPooling1D(3))
model.add(GlobalMaxPooling1D())
model.add(BatchNormalization())

#  fully connected
model.add(Dense(50, activation='relu'))
model.add(Dropout(0.3))
model.add(Dense(6, activation='sigmoid'))

# network looks
model.summary()
######################################################################


######################################################################
##Start train the model

#learning rate
lr = .01
model.compile(loss='binary_crossentropy', optimizer=SGD(lr=lr, clipnorm=1.0,),
              metrics=['accuracy'])

batch_size = 32
epochs =35
train_history = model.fit(X_t,y, batch_size=batch_size, epochs=epochs, verbose=1, shuffle=False,validation_split=0.03)
print(model.get_weights())

#Save the model
model.save("CNN_Model.h5")
print("Saved model to disk")
######################################################################


######################################################################
#LOST&ACC PLOT
loss = train_history.history['loss']
val_loss = train_history.history['val_loss']
acc = train_history.history['acc']
val_acc = train_history.history['val_acc']

plt.figure('1')

plt.subplot('210')
plt.plot(loss)
plt.plot(val_loss)
plt.legend(['loss', 'val_loss'])

plt.subplot('211')
plt.plot(acc)
plt.plot(val_acc)
plt.legend(['acc', 'val_acc'])

plt.show()

######################################################################


######################################################################
#Build a predict function
def Predict_Func(string):

    # Process string
    new_string = [string]
    new_string = tokenizer.texts_to_sequences(new_string)
    new_string = pad_sequences(new_string, maxlen, padding='post', truncating='post')

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
    print()

    return
######################################################################


######################################################################
#check  some words

import time
import sys

print("---====Toxic Comment Filtering====---\n\n")

startText=""
while True:
  print(startText+"Please write Your Comment, else write 'exit'\n")
  commment = input()

  animation = "|/-\\"

  for i in range(100):
      time.sleep(0.1)
      sys.stdout.write("\r"+"Loading" + animation[i % len(animation)])
      sys.stdout.flush()

  sys.stdout.write("")

  if commment == "exit":
    print("We are sorry to hear that you are going, Good bye.")
    break

  if commment != "exit":
    Predict_Func(commment)
    print("End!\n")
    startText="Hi Again ,"
######################################################################

```
Load_Cnn_Model.py
```py
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
model=load_model('path to model we created(h5 file)')

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
```

### Pictures:
![TrainData_Word_length](https://user-images.githubusercontent.com/33747218/137753240-f9cecaef-e8b9-4bd1-923d-912e344e290f.png)
![Loss Acc](https://user-images.githubusercontent.com/33747218/137753228-106a34fb-f981-43b1-a0a1-8b1b6076b1ed.png)
![TrainData_toxic_type](https://user-images.githubusercontent.com/33747218/137753235-911b5d86-b84c-41cc-bdc3-db3fa2bd0b74.png)


![](https://user-images.githubusercontent.com/33747218/137751284-2267f869-cb1f-44af-8ac9-a6b812bf94d7.png)

![](https://user-images.githubusercontent.com/33747218/137751283-a5222ff4-aa16-4646-9a38-9271d9cb8e65.png)

![](https://user-images.githubusercontent.com/33747218/137751274-f4f1f8c5-5cf0-4ab5-8a52-d1c9a80c4bcf.png)
