from Adafruit_I2C import Adafruit_I2C

from time import sleep
import sys
import json
import global_module
import datetime
import threading
import threading

class I2c() :
	def __init__(self,jsonObj):
		json_string = json.dumps(jsonObj)
		decoded = json.loads(json_string)
		self.protocol = decoded["protocol"]
		self.i2c_id = decoded["i2c_id"]
		self.config_cmd = decoded["config_cmd"]
		self.cmd = decoded["cmd"]
		self.sensor_code = decoded["sensor_code"]
		self.quantity = deocoded["quantity"]
		self.isLogging = decoded["isLogging"]
		self.log_file = open('./logs/' + decoded["sensor_code"] + "_" + decoded["quantity"] + ".txt", 'a')
		self.stop = threading.Event()
		self.sensor_thread = threading.Thread(target = self.task)
		self.sensor_thread.start()

	def task(self):
		#set the i2c variable for the right sensor id
		i2c = Adafruit_I2C(int(self.i2c_id))
		#set the configuration based on config_cmd
		self.setConfiguration()
		#run the commands in a loop
		self.executeCommands()
		

	def setConfiguration(self):
		for str_cmd in config_cmd:
			params = str_cmd.split(':')
			if params[0] == 'w':
			    i2c.write8(int(params[1],16),int(params[2],10))
			else:
			    sleep(int(params[1],10))

	
	def executeCommands():
		  while not self.stop:
		  	for str_cmd in cmd:
			      params = str_cmd.split(':')
			      print params[0]
			      if params[0] == 'w' :
				i2c.write8(int(params[1],16),int(params[2],10))
			      elif params[0] == 'rs' :
				now = datetime.datetime.now()
				rs = i2c.readS8(int(params[1],16))
				val = str(params[1]) + ":" + str(rs) + ":" + str(now.minute) + ":" + str(now.second) + ":" + str(now.microsecond)
				if self.isLogging:
					self.log_file.write(val)
			      elif params[0] == 'ru' :
				now = datetime.datetime.now()
				ru = i2c.readS8(int(params[1],16))
				val = str(params[1]) + ":" + str(ru) + ":" + str(now.minute) + ":" + str(now.second) + ":" + str(now.microsecond)
				if self.isLogging:
					self.log_file.write(val)
			      else:
				sleep(int(params[1],10))
			


	def stopRead(self):
		self.log_file.close();
		self.stop.set();
		if self.sensor_thread.isAlive():
			self.sensor_thread.join();
