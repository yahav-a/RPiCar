#!/usr/bin/python
# -*- coding: utf-8 -*-

class Client:

# Messages
	INIT_HEY  = 100
	MOVE	  = 200
	KTHXBYE   = 300

class Server:

# Message
	INIT_OK  = 105
	MOVED	 = 205
	ERROR	 = 204
	CLOSE	 = 305


class Movements:

	Straight = 11
	Back 	 = 10
	Left 	 = 01
	Right 	 = 10
	Stop 	 = 12
