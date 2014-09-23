#!/usr/bin/python
#from MotorController import *
from protocol import *
print("This is the IR Server!")
print("I'll have to do it in the RPi..")

def MoveMotor(data):
	formattedData = data.split(",")
	direction = formattedData[1]
	left = formattedData[2]
	right = formattedData[3]
	response = "URRAY"
	#response = customSpeed(direction,left,right)
	conn.send((str(response)+"\n").encode())


#main main
def StartIRServer():
	#some waiting for data ...
	data = "somedatahere,dsa,dsa,dsa" #delete this!!!
	if data[:3] == Client.CUSTOM_MOVE:
		MoveMotor(data)