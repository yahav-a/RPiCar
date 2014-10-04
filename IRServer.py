#!/usr/bin/python
#from MotorController import *
from multiprocessing import Lock
from protocol import *
from time import sleep
#print("This is the IR Server!")
#print("I'll have to do it in the RPi..")

def MoveMotor(data,lock):
	formattedData = data.split(",")
	direction = formattedData[1]
	left = formattedData[2]
	right = formattedData[3]
	response = "URRAY"
	lock.aquire()
	print("IR: Aquiring lock!")
	#response = customSpeed(direction,left,right)
	lock.release()
	print("IR: Releasing lock!")
	conn.send((str(response)+"\n").encode())


#main main
def StartIRServer(lock):
	#some waiting for data ...
	data = "somedatahere,dsa,dsa,dsa" #delete this!!!
	#while 1:
	#	print("IR: here will be a while:true loop for IR")
	#	sleep(2)
	if data[:3] == Client.CUSTOM_MOVE:
		MoveMotor(data,lock)