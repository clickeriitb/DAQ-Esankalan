import Adafruit_BBIO.UART as UART
import serial
import sys
import json
import global_module
import datetime
import threading
from time import sleep

class Uart() :
	def __init__(self,jsonObj):
		json_string = json.dumps(jsonObj)
		decoded = json.loads(json_string)
		self.code = decoded["sensor_code"]
		self.quantity = decoded["quantity"]
		self.isLogging = decoded["isLogging"]
		self.rate =  decoded["rate"];
		self.protocol = decoded["protocol"];
		self.baudrate = decoded["baudrate"]
		self.byte = decoded["byte"]
		self.command = str(decoded["command"])
		self.pin = decoded["pin"]
	        self.log_file = open('./logs/' + decoded["sensor_code"] + "_" + decoded["quantity"] + ".txt", 'a')
		self.stop = threading.Event()
		self.sensor_thread = threading.Thread(target = self.task)
		self.sensor_thread.start()
		

	def task(self):
		UART.setup(self.pin)
		self.out=''
		self.serial_port = serial.Serial(port = "/dev/ttyO1", baudrate = self.baudrate)
		self.serial_port.open()
		while not self.stop.isSet():
			try:
				self.serial_port.write(''.join([chr(int(''.join(c), 16)) for c in zip(self.command[0::2],self.command[1::2])]))
				self.out = self.serial_port.read(self.byte)
				now = datetime.datetime.now()
				val = str(self.out) + " : " + str(now.minute) + " : " + str(now.second) + " : " + str(now.microsecond)
				print val
				if self.isLogging:
					log_file.write(val)
				time.sleep(self.rate)


	def stopRead(self):
		print "log closed"
		self.log_file.close();
		self.stop.set()
		if self.sensor_thread.isAlive():
			self.sensor_thread.join();
		
