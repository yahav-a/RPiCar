from multiprocessing import Process
import sys
from protocol import *
from MotorControllerP import *
import socket
def StartTCPServer(lock):
	userAddress = ""
	firstConn = 1
	l = lock
	TCP_IP = '0.0.0.0'
	TCP_PORT = 1337
	print("Starting TCP Server on {0}:{1}".format(TCP_IP,TCP_PORT))
	print("Configuring connection")
	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	s.bind((TCP_IP,TCP_PORT))
	print("Waiting for connection")
	try:
		s.listen(1)
		conn,addr = s.accept()
		if firstConn == 1:
			userAddress = str(addr[0])
			firstConn = 0
		print("Connected to: " + str(addr[0]) +":"+ str(addr[1]))
		while 1:
			if(userAddress == str(addr[0]):
				data = conn.recv(BUFFER_SIZE)
				if not data: break
				data = data.decode()
				print("Data received: "+data)
				if data[:3] == Client.INIT_HEY:
					print("Initiallizing connection")
					conn.send((Server.INIT_OK+"\n").encode())
					print("Connection initiallized")
				elif data[:3] == Client.KTHXBYE:
					conn.send(Server.CLOSE.encode())
					exitAndClean()
				elif data[:3] == Client.CUSTOM_MOVE:
					data = str(data)
					formattedData = data.split(",")
					direction = formattedData[1]
					left = formattedData[2]
					right = formattedData[3]
					response = "URRAY"
					response = customSpeed(direction,left,right)
					print(direction+","+left+","+right)
					conn.send((str(response)+"\n").encode())
				else:
					print("Command not understood: "+data)
			else:
				print("Somebody else was trying to connect!!")
		conn.send(Server.CLOSE.encode())
	except KeyboardInterrupt:
		print("Rage Quit")
	except:
		print("Error happened:",sys.exc_info())
	finally:
		s.close()
		exitAndClean()
