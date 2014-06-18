#to run lsusb within the python script
import subprocess

#modules for communication
import wifi_server
import usb_python
import usb.core
import usb.util

# module of global variables
import global_module


import time
import threading
import os
import socket
import struct

#lsusb command that returns product ids' and vendor ids' of all connected usb devices
findUsb = subprocess.Popen(["lsusb"],stdout = subprocess.PIPE)
 
usb_list_str = findUsb.communicate()[0]
usb_list = usb_list_str.split('\n')
ids = []

for x in range(len(usb_list) - 1):
	fields = usb_list[x].split(' ')
    point = fields[5]
	ids = point.split(':')
	print ids[0]
	print ids[1]
	vid, pid =0, 0
	vid = convertToInt(ids[0])
	print(vid)
	pid = convertToInt(ids[1])
	print(pid)
	dev = usb.core.find(idVendor=vid,idProduct=pid)
	version = None
	try:
		#command ctrl_transfer 51 to detect whether OAAP is supported
		version=dev.ctrl_transfer(usb.util.CTRL_TYPE_VENDOR | usb.util.CTRL_IN,51, 0, 0, 2)
		if len(version) == 0:
			#wifi mode
			global_module.mode = 0
		elif not version[0]:
			#wifi mode
			global_module.mode = 0
		else:
			#usb mode
			global_module.mode = 1
	       		v = dev.ctrl_transfer(usb.util.CTRL_TYPE_VENDOR | usb.util.CTRL_OUT, 53, 0, 0, None)

	       	#wait for the request to be processed by the android device and change its prduct id
			time.sleep(2)
			global_module.right_vid = vid
			break	
	except Exception as e:
		print e;

if global_module.mode == 0:
	print "wifi"
	wifi_server.activate()
else:
	print "usb"
	#run lsusb to extract the changed pid of the android device
	
	findUsb = subprocess.Popen(["lsusb"],stdout = subprocess.PIPE)
	usb_list_str = findUsb.communicate()[0]
	usb_list = usb_list_str.split('\n')
	ids = []

	for x in range(len(usb_list) - 1):
		fields = usb_list[x].split(' ')
		point = fields[5]
		ids = point.split(':')
		curr_vid = convertToInt(ids[0])
		if curr_vid == global_module.right_vid:
			usb_python.activate(ids)
        
   
	 
	

