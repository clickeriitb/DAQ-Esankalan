from Adafruit_I2C import Adafruit_I2C

from time import sleep
import sys
import json
import global_module
import datetime
import threading
import threading
import ast

class I2c() :
	def __init__(self,jsonObj):
		json_string = json.dumps(jsonObj)
		decoded = json.loads(json_string)
		self.protocol = decoded["protocol"]
		self.i2c_id = decoded["i2c_id"]
		self.config_cmd = ast.literal_eval(decoded["config_cmd"])
		self.cmd = ast.literal_eval(decoded["cmd"])
		self.sensor_code = decoded["sensor_code"]
		self.quantity = deocoded["quantity"]
		self.isLogging = decoded["isLogging"]

		try:	
		        self.log_file = open('./logs/' + decoded["sensor_code"] + "_" + decoded["quantity"] + ".txt", 'a')
		except:
			e_msg = "Sorry! Unable to request logging for this I2C protocol sensor"
			print e_msg
			if global_mode == 0:
			    global_module.wifi_namespace_reference.on_error(e_msg)
			else:
			    global_module.ep_out.write(e_msg.encode('utf-8'),timeout=0)
		self.stop = threading.Event()
		self.sensor_thread = threading.Thread(target = self.task)
		self.sensor_thread.start()

	def task(self):
		#set the i2c variable for the right sensor id
		try:
		    i2c = Adafruit_I2C(int(self.i2c_id))
		except:
	            e_msg = "Sorry! Unable to recognise and enable the I2C address you have entered"
	            print e_msg
		    if global_mode == 0:
			global_module.wifi_namespace_reference.on_error(e_msg)
		    else:
			global_module.ep_out.write(e_msg.encode('utf-8'),timeout=0)
		    
		#set the configuration based on config_cmd
		try:
		    self.setConfiguration()
		except:
	            e_msg = "Sorry! Unable to set up the initial configirations!! "
	            print e_msg
		    if global_mode == 0:
			global_module.wifi_namespace_reference.on_error(e_msg)
		    else:
			global_module.ep_out.write(e_msg.encode('utf-8'),timeout=0)
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
			try:
			    for str_cmd in cmd:
			      params = str_cmd.split(':')
			      print params[0]
			      if params[0] == 'w' :
				i2c.write8(int(params[1],16),int(params[2],10))
			      elif params[0] == 'rs' :
				now = datetime.datetime.now()
				rs = i2c.readS8(int(params[1],16))
				val = str(params[1]) + ":" + str(rs) + ":" + str(now.minute) + ":" + str(now.second) + ":" + str(now.microsecond)
				#create json Object
				data = [{'data':val,'sensor_code':self.sensor_code}]
				string_data = json.dumps(data)
				if global_module.mode == 0:
			            global_module.wifi_namespace_reference.on_send(data)
				else:
				    #print "usb is sending"
				    global_module.ep_out.write(string_data.encode('utf-8'),timeout=0)
				if self.isLogging:
					self.log_file.write(val)
			      elif params[0] == 'ru' :
				now = datetime.datetime.now()
				ru = i2c.readS8(int(params[1],16))
				val = str(params[1]) + ":" + str(ru) + ":" + str(now.minute) + ":" + str(now.second) + ":" + str(now.microsecond)
				#create json Object
				data = [{'data':val,'sensor_code':self.sensor_code}]
				string_data = json.dumps(data)
				if global_module.mode == 0:
			            global_module.wifi_namespace_reference.on_send(data)
				else:
				    #print "usb is sending"
				    global_module.ep_out.write(string_data.encode('utf-8'),timeout=0)
				if self.isLogging:
					self.log_file.write(val)
			      else:
				sleep(int(params[1],10))
			except:
			    e_msg = "Sorry! Error while running the commands in loop!! "
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
			e_msg = "Sorry! Unable to end the read thread for this I2C protocol sensor"
			print e_msg
			if global_mode == 0:
			    global_module.wifi_namespace_reference.on_error(e_msg)
			else:
			    global_module.ep_out.write(e_msg.encode('utf-8'),timeout=0)


