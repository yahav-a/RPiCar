import RPi.GPIO as GPIO
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
	motorR.ChangeDutyCycle(speed)
	motorL.ChangeDutyCycle(speed)

def stop():
	print("Stoping")
	motorR.ChangeDutyCycle(0)
	motorL.ChangeDutyCycle(0)

def exitAndClean():
	print("Exiting")
	motorR.stop()
	motorL.stop()
	GPIO.cleanup()
	exit()

def cleanNoExit():
	print("Exiting")
	motorR.stop()
	motorL.stop()
	GPIO.cleanup()
	
def goBack(speed):
	speed = checkSpeed(speed)
	print("Going back with speed: "+str(speed))
	GPIO.output(Motor1A, GPIO.LOW)
	GPIO.output(Motor1B, GPIO.HIGH)
	GPIO.output(Motor2A, GPIO.LOW)
	GPIO.output(Motor2B, GPIO.HIGH)
	motorR.ChangeDutyCycle(speed)
	motorL.ChangeDutyCycle(speed)

def turnRight(speed):
	speed = checkSpeed(speed)
	print("Turning right with speed: "+str(speed))
	GPIO.output(Motor1A, GPIO.HIGH)
	GPIO.output(Motor1B, GPIO.LOW)
	GPIO.output(Motor2A, GPIO.HIGH)
	GPIO.output(Motor2B, GPIO.LOW)
	motorR.ChangeDutyCycle(speed/3)
	motorL.ChangeDutyCycle(speed)

def turnLeft(speed):
	speed = checkSpeed(speed)
	print("Turning left with speed: "+str(speed))
	GPIO.output(Motor1A, GPIO.HIGH)
	GPIO.output(Motor1B, GPIO.LOW)
	GPIO.output(Motor2A, GPIO.HIGH)
	GPIO.output(Motor2B, GPIO.LOW)
	motorR.ChangeDutyCycle(speed)
	motorL.ChangeDutyCycle(speed/3)

def customSpeed(direction,left, right):
	ans = "Going "+"Forward" if direction == "f" else "Back" +". Left: "+str(left)+". Right: "+str(right)
	print(ans)
	right = float(right)
	left = float(left)
	if direction == "f":
		#Forward
		GPIO.output(Motor1A, GPIO.HIGH)
		GPIO.output(Motor1B, GPIO.LOW)
		GPIO.output(Motor2A, GPIO.HIGH)
		GPIO.output(Motor2B, GPIO.LOW)
		motorR.ChangeDutyCycle(right)
		motorL.ChangeDutyCycle(left)
	else:
		#Back
		GPIO.output(Motor1A, GPIO.LOW)
		GPIO.output(Motor1B, GPIO.HIGH)
		GPIO.output(Motor2A, GPIO.LOW)
		GPIO.output(Motor2B, GPIO.HIGH)
		motorR.ChangeDutyCycle(right)
		motorL.ChangeDutyCycle(left)

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
motorR = GPIO.PWM(Motor1E,100)
motorL = GPIO.PWM(Motor2E,100)
print("Starting motors")
motorR.start(0)
motorL.start(0)