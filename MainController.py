#!/usr/bin/python
from multiprocessing import Process, Lock
from IRServer import *
from TcpServer import *
from time import sleep

def mainProcessFunc():
    while 1:
    	print ("Main still working")
    	sleep(2)


if __name__ == '__main__':
	lock = Lock()
	irProcess = Process(target=StartIRServer, args=(lock,))
	tcpProcess = Process(target=StartTCPServer,args=(lock,))
	mainProcess = Process(target=mainProcessFunc)
	#irProcess.start()
	tcpProcess.start()
	#mainProcess.start()