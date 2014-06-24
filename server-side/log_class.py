import json

#from uart_log import Uart
from adc_log import Adc
#from i2c_log import I2c

class Log_Class:

	def __init__(self,reqObj):
		self.protocol = reqObj.protocol
		self.json = reqObj.json;
		self.quantity = reqObj.quantity
		self.code = reqObj.code
		self.isLogging = reqObj.isLogging
		self.pin = reqObj.pin
		self.log_sensor();
	
	def log_sensor(self):
		if self.protocol == "adc":
			self.obj = Adc(self.json);
		#elif self.protocol == "uart":
			#self.obj = Uart(self.json);
		#elif self.protocol == "i2c":
			#self.obj = I2c(self.json);
	
	def stop(self):
		self.obj.stopRead();

