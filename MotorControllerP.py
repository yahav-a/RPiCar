import pigpio
from time import sleep

def exitAndClean():
	print("Exiting")
	pi.write(Motor1E,pigpio.LOW)
	pi.write(Motor2E,pigpio.LOW)
	pi.stop()
	exit()

def cleanNoExit():
	print("Exiting")
	pi.write(Motor1E,pigpio.LOW)
	pi.write(Motor2E,pigpio.LOW)
	pi.stop()
	GPIO.cleanup()

def customSpeed(direction,left, right):
	right = int(right)
	left = int(left)
	if direction == "f":
		pi.write(Motor1A, pigpio.HIGH)
		pi.write(Motor1B, pigpio.LOW)
		pi.write(Motor2A, pigpio.HIGH)
		pi.write(Motor2B, pigpio.LOW)
		pi.set_PWM_dutycycle(Motor1E, right)
		pi.set_PWM_dutycycle(Motor2E, left)
		#print("Got to forward")
	else:
		pi.write(Motor1A, pigpio.LOW)
		pi.write(Motor1B, pigpio.HIGH)
		pi.write(Motor2A, pigpio.LOW)
		pi.write(Motor2B, pigpio.HIGH)
		pi.set_PWM_dutycycle(Motor1E, right)
		pi.set_PWM_dutycycle(Motor2E, left)
		#print("Got to back")
	ans = "Going "+ ("Forward" if direction == "f" else "Back") +". Left: "+str(left)+". Right: "+str(right)
	print(ans)


Motor1A = 23 #PIN 16
Motor1B = 24 #PIN 18
Motor1E = 25 #PIN 22

Motor2A = 10 #PIN 19
Motor2B = 9  #PIN 21
Motor2E = 11 #PIN 23
pi = pigpio.pi()
print("Setting up GPIO pins")
pi.set_mode(Motor1A, pigpio.OUTPUT)
pi.set_mode(Motor1B, pigpio.OUTPUT)
pi.set_mode(Motor1E, pigpio.OUTPUT)
pi.set_mode(Motor2A, pigpio.OUTPUT)
pi.set_mode(Motor2B, pigpio.OUTPUT)
pi.set_mode(Motor2E, pigpio.OUTPUT)
print("Setting PWM range to 100")
pi.set_PWM_range(Motor1E,100)
pi.set_PWM_range(Motor2E,100)
print("Warming up engines")
print("Starting motors")

