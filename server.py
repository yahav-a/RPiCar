import protocol
from MotorController import *
import socket

TCP_IP = '192.168.100.109'
TCP_PORT = 1337
BUFFER_SIZE = 128
print("Configuring connection")
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((TCP_IP,TCP_PORT))
print("Waiting for connection")
try:
	s.listen(1)
	
	conn,addr = s.accept()
	print("Connected to: " + addr)
	while 1:
		data = conn.recv(BUFFER_SIZE)
		if not data: break
		print("Data received: "+data)
		formattedData = data.split(" ")
		action = formattedData[0]
		speed = int(formattedData[1])
		response = Server.MOVED + ": "
		if action == Movements.Straight:
			goStraight(speed)
			response += "Moved Straight"
		elif action == Movements.Back:
			goBack(speed)
			response += "Moved Back"
		elif action == Movements.Right:
			turnRight(speed)
			response += "Turned Right"
		elif action == Movements.Left:
			turnLeft(speed)
			response += "Turned Left"
		elif action == Movements.Halt:
			stop()
			response += "Stopped"
		elif action == Client.KTHXBYE:
			conn.send(Server.CLOSE)
			exitAndClean()
		conn.send(response)
	conn.send(Server.CLOSE)
	exitAndClean()
except KeyboardInterrupt:
	print("Rage Quit")
	exitAndClean()