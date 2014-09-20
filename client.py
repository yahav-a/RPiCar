import socket
import protocol

RPORT = 4321
SERVER_ADD = '127.0.0.1'

s = socket.socket()
s.connect((SERVER_ADD,RPORT))


err = s.send(Client.INIT_HEY)
if err != 3:
	print 'ERROR! cannot Initiate Connection'
	s.close()
