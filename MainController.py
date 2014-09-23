#!/usr/bin/python
from multiprocessing import Process
from IRServer import *
from TcpServer import *
from time import sleep

def mainProcessFunc():
    while 1:
    	print ("Main still working")
    	sleep(2)


if __name__ == '__main__':
    irProcess = Process(target=StartIRServer)
    tcpProcess = Process(target=StartTCPServer)
    mainProcess = Process(target=mainProcessFunc)
    irProcess.start()
    tcpProcess.start()
    mainProcess.start()