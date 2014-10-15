#! /usr/bin/python
import serial
from protocol import *
from MotorControllerP import *

def startBluetoothServer():
	bluetoothSerial = serial.Serial("/dev/rfcomm1",baudrate=9600)
	print("Bluetooth connected")
	try:
		while 1:
			data = bluetoothSerial.readLine()
			if not data: break
			data = data.decode()
			print("Data received: "+data)
			if data[:3] == Client.INIT_HEY:
				print("Initiallizing connection")
				bluetoothSerial.write((Server.INIT_OK+"\n").encode())
				print("Connection initiallized")
			elif data[:3] == Client.KTHXBYE:
				bluetoothSerial.write(Server.CLOSE.encode())
				exitAndClean()
			elif data[:3] == Client.CUSTOM_MOVE:
				data = str(data)
				formattedData = data.split(",")
				direction = formattedData[1]
				left = formattedData[2]
				right = formattedData[3]
				response = customSpeed(direction,left,right)
				print(direction+","+left+","+right)
				bluetoothSerial.write((str(response)+"\n").encode())
			else:
				print("Command not understood: "+data)
		bluetoothSerial.write(Server.CLOSE.encode())
	except KeyboardInterrupt:
		print("Rage Quit")
	except:
		print("Error happened:",sys.exc_info())
	finally:
		exitAndClean()