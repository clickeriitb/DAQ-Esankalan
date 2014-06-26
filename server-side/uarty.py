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
		if global_module.mode == 0:
			json_string  = json.dumps(jsonObj)
			decoded = json.loads(json_string)
		else:
			decoded = json.loads(jsonObj)
		self.code = decoded["sensor_code"]
		self.quantity = decoded["quantity"]
		self.isLogging = decoded["isLogging"]
		self.rate =  decoded["rate"];
		self.protocol = decoded["protocol"];
		self.baudrate = decoded["baudrate"]
		self.byte = decoded["byte"]
		self.command = str(decoded["command"])
		self.pin = decoded["pin"]

		try:	
		        self.log_file = open('./logs/' + decoded["sensor_code"] + "_" + decoded["quantity"] + ".txt", 'a')
		except:
			e_msg = "Sorry! Unable to request logging for this Uart protocol sensor"
			print e_msg
			if global_mode == 0:
			    global_module.wifi_namespace_reference.on_error(e_msg)
			else:
			    global_module.ep_out.write(e_msg.encode('utf-8'),timeout=0)
		
		self.stop = threading.Event()
		self.sensor_thread = threading.Thread(target = self.task)
		self.sensor_thread.start()
		

	def task(self):
		UART.setup(self.pin)
		self.out=''
		if self.pin == "UART1":
			path = "/dev/ttyO1"
		elif self.pin == "UART2":
			path = "/dev/ttyO2"
		elif self.pin == "UART4":
			path = "/dev/ttyO4"
		elif self.pin == "UART5":
			path = "/dev/ttyO5"

		self.serial_port = serial.Serial(port = path, baudrate = self.baudrate)
		self.serial_port.open()
		while not self.stop.isSet():
			try:
				self.serial_port.write(''.join([chr(int(''.join(c), 16)) for c in zip(self.command[0::2],self.command[1::2])]))
				self.out = self.serial_port.read(self.byte)
				now = datetime.datetime.now()
				val = str(self.out) + ":" + str(now.minute) + ":" + str(now.second) + ":" + str(now.microsecond)
				print val
				data = [{'data':val,'sensor_code':self.sensor_code}]
				string_data = json.dumps(data)
				if global_mode == 0:
			            global_module.wifi_namespace_reference.on_send(data)
				else:
				    global_module.ep_out.write(string_data.encode('utf-8'),timeout=0)
				if self.isLogging:
					log_file.write(val)
				time.sleep(0.25)
			except:
				e_msg = "Sorry! Unable to read UART data"
				print e_msg
				if global_mode == 0:
			            global_module.wifi_namespace_reference.on_error(e_msg)
				else:
				    global_module.ep_out.write(e_msg.encode('utf-8'),timeout=0)
		 

	def stopRead(self):
		self.log_file.close();
		self.stop.set();
		try: 
			if self.sensor_thread.isAlive():
				self.sensor_thread.join();
		except:
			e_msg = "Sorry! Unable to end the read thread for this UART protocol sensor"
			print e_msg
			if global_mode == 0:
			    global_module.wifi_namespace_reference.on_error(e_msg)
			else:
			    global_module.ep_out.write(e_msg.encode('utf-8'),timeout=0)
