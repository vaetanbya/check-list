import tensorflow as tf
import numpy as np
from tensorflow.python.keras.models import Sequential
from tensorflow.python.keras.layers import Dense

# Create random arrays x1,y1 as training data

x1=np.array([50,100,150,200,250,300,350])
y1=np.array([1,2,3,4,5,6,7])

# Create a model with activation function as linear for last layer

model=Sequential()
model.add(Dense(10,input_shape=[1],activation='relu'))
model.add(Dense(1,activation='linear'))

# Compile the model with loss function as Mean Squared Error

model.compile(loss='mean_squared_error',optimizer='Adam',metrics=['mse'])

# Fitting the model to 1000 epochs

model.fit(x1,y1,epochs=1000)

# Predicting the model 
 
model.predict([1])

tf.keras.models.save_model(model,'saved_model.pbtxt')

converter = tf.lite.TFLiteConverter.from_keras_model(model=model)

tfmodel = converter.convert()

# Save TFLite model into a .tflite file 

open("degree.tflite","wb").write(tfmodel)