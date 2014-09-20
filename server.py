import RPi.GPIO as GPIO
import protocol
import socket
from time import sleep

def checkSpeed(speed):
	print("Checking the speed")
	if speed < 25:
		speed = 25
	if speed > 100:
		speed = 100
	print("Speed is: "+str(speed))
	return speed

def goStraight(speed):
	speed = checkSpeed(speed)
	print("Going straigh with speed: "+str(speed))
	GPIO.output(Motor1A, GPIO.HIGH)
	GPIO.output(Motor1B, GPIO.LOW)
	GPIO.output(Motor2A, GPIO.HIGH)
	GPIO.output(Motor2B, GPIO.LOW)
	motor1.ChangeDutyCycle(speed)
	motor2.ChangeDutyCycle(speed)

def stop():
	print("Stoping")
	motor1.ChangeDutyCycle(0)
	motor2.ChangeDutyCycle(0)

def exitAndClean():
	print("Exiting")
	motor1.stop()
	motor2.stop()
	GPIO.cleanup()
	exit()

def goBack(speed):
	speed = checkSpeed(speed)
	print("Going back with speed: "+str(speed))
	GPIO.output(Motor1A, GPIO.LOW)
	GPIO.output(Motor1B, GPIO.HIGH)
	GPIO.output(Motor2A, GPIO.LOW)
	GPIO.output(Motor2B, GPIO.HIGH)
	motor1.ChangeDutyCycle(speed)
	motor2.ChangeDutyCycle(speed)

def turnRight(speed):
	speed = checkSpeed(speed)
	print("Turning right with speed: "+str(speed))
	GPIO.output(Motor1A, GPIO.HIGH)
	GPIO.output(Motor1B, GPIO.LOW)
	GPIO.output(Motor2A, GPIO.HIGH)
	GPIO.output(Motor2B, GPIO.LOW)
	motor1.ChangeDutyCycle(speed/3)
	motor2.ChangeDutyCycle(speed)

def turnLeft(speed):
	speed = checkSpeed(speed)
	print("Turning left with speed: "+str(speed))
	GPIO.output(Motor1A, GPIO.HIGH)
	GPIO.output(Motor1B, GPIO.LOW)
	GPIO.output(Motor2A, GPIO.HIGH)
	GPIO.output(Motor2B, GPIO.LOW)
	motor1.ChangeDutyCycle(speed)
	motor2.ChangeDutyCycle(speed/3)



GPIO.setmode(GPIO.BOARD)
Motor1A = 16
Motor1B = 18
Motor1E = 22

Motor2A = 19
Motor2B = 21
Motor2E = 23

print("Setting up GPIO pins")
GPIO.setup(Motor1A, GPIO.OUT)
GPIO.setup(Motor1B, GPIO.OUT)
GPIO.setup(Motor1E, GPIO.OUT)
GPIO.setup(Motor2A, GPIO.OUT)
GPIO.setup(Motor2B, GPIO.OUT)
GPIO.setup(Motor2E, GPIO.OUT)

print("Warming up engines")
motor1 = GPIO.PWM(Motor1E,100)
motor2 = GPIO.PWM(Motor2E,100)
print("Starting motors")
motor1.start(0)
motor2.start(0)

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