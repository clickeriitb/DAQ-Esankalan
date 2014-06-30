import Adafruit_BBIO.ADC as ADC
import sys
import json
import global_module
import datetime
import threading
from time import sleep

class Adc() :
	def __init__(self,jsonObj):
		if global_module.mode == 0:
			json_string  = json.dumps(jsonObj)
			decoded = json.loads(json_string)
		else:
			decoded = json.loads(jsonObj)
		self.rate = decoded["rate"]/1000;
		self.protocol = decoded["protocol"]
		self.pin = decoded["pin"];
		self.sensor_code = decoded["sensor_code"]
		self.quantity = decoded["quantity"]
		self.isLogging = decoded["isLogging"]
		
		try:	
		        self.log_file = open('./logs/' + decoded["sensor_code"] + "_" + decoded["quantity"] + ".txt", 'a')
		except:
			e_msg = "Sorry! Unable to request logging for this Analog output(ADC) sensor"
			print e_msg
			if global_module.mode == 0:
			    global_module.wifi_namespace_reference.on_error(e_msg)
			else:
			    global_module.ep_out.write(e_msg.encode('utf-8'),timeout=0)
		self.stop = threading.Event()
		self.sensor_thread = threading.Thread(target = self.task)
		self.sensor_thread.start()
		
		
	def task(self):
		ADC.setup()
		while not self.stop.isSet():
			try: 				        
				value = ADC.read_raw(self.pin) 
				now = datetime.datetime.now()
				print  self.protocol + "%f : %d : %d : %d "%(value,now.minute,now.second,now.microsecond)
				val = str(value)
				date = str(now.minute) + ":" + str(now.second) + ":" + str(now.microsecond)
				
				data = {'data':val,'sensor_code':self.sensor_code, 'date':date}
				string_data = json.dumps(data)
				
				if global_module.mode == 0:
			            global_module.wifi_namespace_reference.on_send(data)
				else:
				    #print "usb is sending"
				    global_module.ep_out.write(string_data.encode('utf-8'),timeout=0)
				if self.isLogging:
					self.log_file.write(val + " : " + date + "\n")
				sleep(self.rate);
				
			except Exception as e:
				print e;
				self.log_file.write(e);
				self.log_file.close();
				e_msg = "Sorry! Unable to read Analog Data"
				print e_msg
				if global_module.mode == 0:
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
			e_msg = "Sorry! Unable to end the read thread for this Analog output(ADC) sensor"
			print e_msg
			if global_module.mode == 0:
			    global_module.wifi_namespace_reference.on_error(e_msg)
			else:
			    global_module.ep_out.write(e_msg.encode('utf-8'),timeout=0)

