#!/usr/bin/python
# -*- coding: utf-8 -*-

import socket
from protocol import *

RPORT = 1337
SERVER_ADD = '127.0.0.1'

def main():
	s = socket.socket()
	try:
		s.connect((SERVER_ADD,RPORT))
	except Exception, e:
		print e
		return

	err = s.send(Client.INIT_HEY)
	if err != 3:
		print 'ERROR! cannot Initiate Connection'
		s.close()
		return

	data = s.recv(BUFFER_SIZE)
	message_type = data[:3]

	if message_type == Server.INIT_OK:
		print 'Initiated connection with {0}:{1}!'.format(SERVER_ADD,RPORT)
	else:
		print 'Wrong protcol header!'
		
	try:
		while 1:
			direction = raw_input('Enter Direction [Straight,Back,Left,Right,Halt]:')
			direction = direction.lower()[1]
			if direction == 's':
				direction = Movements.Straight

			elif direction == 'b':
				direction = Movements.Back

			elif direction == 'l':
				direction = Movements.Left

			elif direction == 'r':
				direction = Movements.Right

			elif direction == 'h':
				direction = Movements.Halt
				
			else:
				print 'Invalid Input!'
				continue

			speed = '00'
			if direction != 'h':
				try:
					speed = raw_input('Enter speed [25-100]')
				except Exception, e:
					print 'Invalid Input!'
					continue

				if len(speed) > 3 or not 25 <= int(speed) <= 100:
					print 'Invalid Input!'
					continue

			next_header = Client.Move
			should_close = raw_input('Do you want to close the connection? [N/Y]')
			if should_close.lower()[1] == 'y':
				next_header = Client.KTHXBYE

			err = s.send(next_header + direction+' ' + speed)
			if err !=5:
				print 'ERROR! while sending movement message'
				s.close()
				return

			data = s.recv(BUFSIZE)
			message_type = data[:3]
			data = data[3:]

			if message_type == Server.ERROR:
				print 'Car got an error: {0}'.format(data)

			elif message_type == Server.MOVED:
				print 'Car Says: {0}'.format(data)

			elif message_type == Server.Close:
				print 'BYE!'
				s.close()
				break

			else:
				print 'Wrong protcol header!'

	except KeyboardInterrupt, e:
		s.close()
	return

if __name__ == '__main__':
	main()