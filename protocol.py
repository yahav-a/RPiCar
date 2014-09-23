#!/usr/bin/python
# -*- coding: utf-8 -*-

BUFFER_SIZE = 128

class Client:

	""" 
		Client message codes for controlling the car
	"""

# Messages
	INIT_HEY  = '100'
	MOVE	  = '200'
	KTHXBYE   = '300'
	CUSTOM_MOVE = '400'
#CUSTOME_MOVE = 400,direction[f,b],left,right

class Server:
	
	""" 
		Server message codes for controlling the car
	"""

# Messages
	INIT_OK  = '105'
	MOVED	 = '205'
	ERROR	 = '204'
	CLOSE	 = '305'


class Movements:
	
	""" 
		possible movements for the car
	"""

	Straight = '11'
	Back 	 = '00'
	Left 	 = '01'
	Right 	 = '10'
	Halt 	 = '12'
