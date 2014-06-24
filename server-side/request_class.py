import json

#imports for respective protocol classes
from uarty import Uart
from adc import Adc
from i2c import I2c

import global_module

class Request_Class:

	def __init__(self,jsonReq):
		if global_module.mode == 0:
			json_string  = json.dumps(jsonReq)
			decoded = json.loads(json_string)
		else:
			decoded = json.loads(jsonReq)
		self.protocol = decoded["protocol"]
		self.json = jsonReq;
		self.quantity = decoded["quantity"]
		self.code = decoded["sensor_code"]
		self.isLogging = decoded["isLogging"]
		self.pin = decoded["pin"]
		self.read_sensor();
	
	def read_sensor(self):
		if self.protocol == "adc":
			self.obj = Adc(self.json);
		elif self.protocol == "uart":
			self.obj = Uart(self.json);
		elif self.protocol == "i2c":
			self.obj = I2c(self.json);
	
	def stop(self):
		self.obj.stopRead();

