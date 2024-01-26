# Measures engine rotations

Project Structure:

```
├─ Measures_engine_rotations
│  ├─ Debug
│  │  ├─ Exe
│  │  │  └─ FinalProject.d43
│  │  ├─ List
│  │  │  └─ FinalProject.map
│  │  └─ Obj
│  │     ├─ FinalProject.pbd
│  │     ├─ FinalProject.pbd.browse
│  │     ├─ FinalProject.pbd.linf
│  │     ├─ LCDutilities.pbi
│  │     ├─ LCDutilities.pbi.xcl
│  │     ├─ LCDutilities.r43
│  │     ├─ Projecton.pbi.xcl
│  │     ├─ Projecton.r43
│  │     ├─ Untitled1.pbi.xcl
│  │     ├─ motor.pbi
│  │     └─ motor.r43
│  ├─ FinalProject.dep
│  ├─ FinalProject.ewd
│  ├─ FinalProject.ewp
│  ├─ FinalProject.ewt
│  ├─ LCDutilities.c
│  ├─ LCDutilities.h
│  ├─ motor.c
│  ├─ problem.docx
│  ├─ settings
│  │  ├─ FinalProject.Debug.cspy.bat
│  │  ├─ FinalProject.Debug.cspy.ps1
│  │  ├─ FinalProject.Debug.driver.xcl
│  │  ├─ FinalProject.Debug.general.xcl
│  │  ├─ FinalProject.dbgdt
│  │  ├─ FinalProject.dnx
│  │  ├─ FinalProject.reggroups
│  │  └─ workspace.wsdt
│  └─ workspace.eww
```

Description:
code written with c programming, and developed on msp430 and his timers, the goal its control and measures engine rotations

Requirement:

- ms430FR6989
- motor dc
- wires
- Trem cool communication over USB

Pictures:
![](https://user-images.githubusercontent.com/33747218/137758886-5566ee9c-ae31-4aa8-8707-05f892986234.png)

Start:

1. download IDE IAR system.
2. connect your ms430FR6989
3. download and debug motor.c file to the micro controller
4. send commands via trem cool

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
model that’s capable of detecting different types of of toxicity comments like threats, obscenity, hate . base on CNN model

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

Start:

python3 Final_Code_Toxic.py

### Pictures:

![TrainData_Word_length](https://user-images.githubusercontent.com/33747218/137753240-f9cecaef-e8b9-4bd1-923d-912e344e290f.png)
![Loss Acc](https://user-images.githubusercontent.com/33747218/137753228-106a34fb-f981-43b1-a0a1-8b1b6076b1ed.png)
![TrainData_toxic_type](https://user-images.githubusercontent.com/33747218/137753235-911b5d86-b84c-41cc-bdc3-db3fa2bd0b74.png)

![](https://user-images.githubusercontent.com/33747218/137751284-2267f869-cb1f-44af-8ac9-a6b812bf94d7.png)

![](https://user-images.githubusercontent.com/33747218/137751283-a5222ff4-aa16-4646-9a38-9271d9cb8e65.png)

![](https://user-images.githubusercontent.com/33747218/137751274-f4f1f8c5-5cf0-4ab5-8a52-d1c9a80c4bcf.png)

# Toxic Comments Classification Naive base

Project Structure:

```
├─ Classification_NAIVE_BASE
│  ├─ NAIVE_BAYES.py
│  ├─ test.csv
│  ├─ train.csv
```

Description:
model that’s capable of detecting if comment is a toxic. base on naive base models

Requirement:

- Python 3.8 or above
- pip module
- pandas module
- sklearn module
- termcolor module
- data set train.csv & test.csv

Installation with pip

```
## sudo python3 -m pip install -U pandas
## sudo python3 -m pip install -U scikit-learn
## sudo python3 -m pip install -U termcolor
```

Start:

python3 NAIVE_BAYES.py

### Pictures:

Accuracy

![](https://user-images.githubusercontent.com/33747218/137727248-6aeefd12-c15a-4a24-ad08-823fd69b88bc.png)

![](https://user-images.githubusercontent.com/33747218/137727253-bf56190a-1286-4fe4-9af4-ac4eb82c1ed8.png)

![](https://user-images.githubusercontent.com/33747218/137727256-0e514ce1-9c3e-417f-a280-ce89b0d0428f.png)

detect a toxic comment
![](https://user-images.githubusercontent.com/33747218/137727258-c82727f4-b16d-4dc8-852a-d3b6cce4f772.png)

zx

# Rocket space ship Game

### Description:

Web application game , hit approaching enemy spaceships

#### Pictures:

![''](https://user-images.githubusercontent.com/33747218/137724188-c7b33d89-3dc0-47e2-bb63-9cdfd28d2589.png)
![''](https://user-images.githubusercontent.com/33747218/137724203-7188b1ff-a5de-42e0-b773-2272f7b79bad.png)

### Project structure

```
Rocket_Ship
├─ .gitignore
├─ README.MD
├─ package-lock.json
├─ package.json
├─ public
│  ├─ css
│  │  ├─ StyleSheet.css
│  │  ├─ font-awesome.min.css
│  │  └─ w3.css
│  ├─ img
│  │  ├─ Fightspace.jpg
│  │  ├─ Space.jpg
│  │  ├─ boom.jpg
│  │  ├─ generalShip.bmp
│  │  ├─ keyboard.png
│  │  ├─ mothership.bmp
│  │  ├─ regship.bmp
│  │  ├─ shipFighter.jpg
│  │  └─ spaceship.png
│  ├─ js
│  │  ├─ app.js
│  │  ├─ jquery.color-2.1.2.min.js
│  │  └─ jquery.min.js
│  └─ sound
│     ├─ Explosion.mp3
│     ├─ SpaceLaserShot.mp3
│     └─ spaceGameLoop_mp3.mp3
├─ src
│  └─ app.js
└─ templates
   └─ views
      ├─ 404.hbs
      └─ index.hbs

```

Start :

- npm install
- npm run start

start play!. on port 3000
