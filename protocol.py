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
	Back 	 = '10'
	Left 	 = '01'
	Right 	 = '10'
	Halt 	 = '12'
