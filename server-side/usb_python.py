#usb modules
import usb.core
import usb.util

import time
import threading
import os
import socket
import struct
import subprocess
import json

#module to create a general object for sensors
from request_class import Request_Class

from gevent.event import Event
import global_module

# the module handles logging of sensor data when there is no client connection
import logging_module as log

#function to convert hex address extracted from the lsusb command to base 10 integer
def convertToInt(hexAddr):
	position,value,ch = 1,0,0
	
	for i in reversed(hexAddr):
		ch = ord(i)
		if ch > 57 :
			value += (ch-97+10)*position
		else : 
			value += (ch-48)*position
		position = position*16
		
	return value

def read_request(sensor_code,quantity):
	for x in global_module.logged:
	    #destroy the logging if it is happening for that sensor
	    if x.code == sensor_code and x.quantity == quantity:
		try:
		    x.stop()
		    global_module.logged.pop(pos)
		except:
		    e_msg = "Sorry! Unable to stop the ongoing logging of sensor" + sensor_code
		    print e_msg
		    writer(e_msg)
            pos = pos + 1; 
		#generate a request Class object and append it to the current session       
		global_module.current_session.append(Request_Class(msg))

def stop_read(sensor_code):
	for x in global_module.current_session:
	    try:
			x.stop()
			print "Data acquisition stopped for : " + sensor_code
	    except:
			e_msg = "Sorry! Unable to stop the Data Aquisition of sensor : " + sensor_code
			print e_msg
			writer(e_msg)
				
def stop_log(sensor_code,quantity):
	pos = 0;
	for x in global_module.logged:
	    if x.code == sensor_code and x.quantity == quantity:
		try:
			x.stop()
			global_module.logged.pop(pos)
		except:
			e_msg = "Sorry! Unable to stop the Data Logging of this sensor"
			print e_msg
			writer(e_msg)
	    pos = pos + 1
    
def writer(data):

	try:
		#encoded data to be sent as a byte array
		global_module.ep_out.write(data.encode('utf-8'),timeout=0)
		
	except usb.core.USBError:
		#equivalent to disconnect method in wifi_server
		print("Disconnection: Logging started!!")
		for x in global_module.current_session:
		    if x.isLogging:
		        log.createLog(x)
		    x.stop()
		del global_module.current_session[:] 
		if global_module.reader_thread.isAlive():
		    global_module.reader_thread.join();

def reader():
	while True:
		try:
			data = global_module.ep_in.read(size=2000,timeout=0)
			#generate a string from byte array
			msg = ''.join(['%c' % x for x in data])
			print(msg)
			#interpret string message as json and follow the same pattern as wifi_module
			global_module.isConnected = True;
			decoded = json.loads(msg)
			if decoded["objId"] == "start":
				print decoded["protocol"]
				print decoded["sensor_code"]
				read_request(decoded["sensor_code"],decoded["quantity"]);
			elif decoded["objId"] == "stop":
				stop_read(decoded["sensor_code"])
			elif decoded["objId"] == "logStop":
			    stop_log(decoded["sensor_code"],decoded["quantity"])		
		except usb.core.USBError:
			print("Error in recieving data from the client")
			break

def activate(array):
	vid, pid =0, 0
	vid = convertToInt(array[0])
	print vid
	pid = convertToInt(array[1])
	print pid
	dev = usb.core.find(idVendor=vid,idProduct=pid)
	dev.set_configuration()
	cfg = dev.get_active_configuration()
	if_num = cfg[(0,0)].bInterfaceNumber
	print(if_num)
	intf = usb.util.find_descriptor(cfg,bInterfaceNumber = if_num)         
	#define ep_out channel
	global_module.ep_out = usb.util.find_descriptor(intf,custom_match = lambda e: usb.util.endpoint_direction(e.bEndpointAddress)==usb.util.ENDPOINT_OUT)
	print "ep_out setup!!"
	#define ep_in
	global_module.ep_in = usb.util.find_descriptor(intf,custom_match = lambda e: usb.util.endpoint_direction(e.bEndpointAddress)==usb.util.ENDPOINT_IN)
	print "ep_in setup!!"

	global_module.isConnected = True
	#database connectivity
        try:
            log.connect_database()
	except:
	    e_msg = "Unable to connect to the logging database"
	    print e_msg 
	    writer(e_msg)

	#clearing the present logged sensor array
	
	#defining the reader thread
	global_module.reader_thread = threading.Thread(target = reader)
	global_module.reader_thread.start()
	
	
		
	






















