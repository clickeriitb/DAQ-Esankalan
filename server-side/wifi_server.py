# Libraries imported for socket connection using socket.io library of gevent
from socketio import socketio_manage
from socketio.server import SocketIOServer
from socketio.namespace import BaseNamespace
from gevent.event import Event


#Import the class to create general objects
from request_class import Request_Class 

import os
import json
import time

# global module contains all the global variables accessible to all scripts
import global_module

# this import implements the threads used for data acquisition from the system
import threading

# the module handles logging of sensor data when there is no client connection
import logging_module as log
# The Namespace class inherits the BaseNamespace class which implements the function of the sockets
class Wifi_Namespace(BaseNamespace):
    #registry to keep a note of all the wifiNamespace objects
    _registry = {}

    # static namespace object created to enable other modules to interact with it and be able to send data to the client
    static_wifi_namespace = 0
   
    @staticmethod
    def on_send(msg_string):
        Wifi_Namespace.static_wifi_namespace.emit('reading',msg_string)

    @staticmethod
    #function to print errors
    def on_error(error_msg):
	Wifi_Namespace.static_wifi_namespace.emit('error',error_msg)

    # initialize method called right after __init__ of the class. The instance is created when the client requests for a connection
    def initialize(self):
        # referrring the self object to the static object so that it can be used to send data to client without request
    	Wifi_Namespace.static_wifi_namespace  = self

        #once a client logs in register him
        print 'Connection Established'
        self._registry[id(self)] = self
        
        #message to client
        Wifi_Namespace.static_wifi_namespace.emit('connect','hi client')

        global_module.isConnected = True

        # connect to the database to make it available to read for all objects
        try:
            log.connect_database()
	except:
	    e_msg = "Unable to connect to the logging database"
	    print e_msg
	    global_module.wifi_namespace_reference.on_error(e_msg);
        
    # the method is called when the connection with the client is broken in any manner whatsoever
    def disconnect(self, *args, **kwargs):
        print 'disconnect'

        #on disconnect remove that client from the chat
        del self._registry[id(self)]
        
        # stop reading of read-only sensors and start offline logging of sensors whose logging is demanded
        for x in global_module.current_session:
            if x.isLogging:
                log.createLog(x)
            x.stop()
        del global_module.current_session[:]

        #-------------------# thinking of deleting the objects as well
        super(Wifi_Namespace, self).disconnect(*args, **kwargs)

    # callback methods that react to client requests of various types
    def on_start(self, jsonObj):

        global_module.isConnected = True;
        json_string  = json.dumps(jsonObj)
        decoded = json.loads(json_string)
        print decoded["protocol"]
        print decoded["sensor_code"]
        pos = 0;
        for x in global_module.logged:
            if x.code == decoded["sensor_code"] and x.quantity == decoded["quantity"]:
                try:
                    x.stop()
                    global_module.logged.pop(pos)
		except:
		    e_msg = "Sorry! Unable to stop the ongoing logging of sensor" + decoded["sensor_code"]
		    print e_msg
		    global_module.wifi_namespace_reference.on_error(e_msg);
            pos = pos + 1;        
        global_module.current_session.append(Request_Class(jsonObj))

    def on_receive(self,msg):
        print "received"

    def on_stop(self,jsonObj):
        global_module.isConnected = False
        pos = 0;
        for x in global_module.current_session:
            try:
                x.stop()
		print "Data acquisition stopped"
	    except:
		e_msg = "Sorry! Unable to stop the Data Aquisition of this sensor"
		print e_msg
		global_module.wifi_namespace_reference.on_error(e_msg);
        print "Data acquisition stopped"

    def on_logStop(self,jsonObj):
        json_string  = json.dumps(jsonObj)
        decoded = json.loads(json_string)
        pos = 0;
        for x in global_module.logged:
            if x.code == decoded["sensor_code"] and x.quantity == decoded["quantity"]:
                try:
		        x.stop()
		        global_module.logged.pop(pos)
		except:
			e_msg = "Sorry! Unable to stop the Data Logging of this sensor"
			print e_msg
			global_module.wifi_namespace_reference.on_error(e_msg)
            pos = pos + 1

def connect_wifi(environ, start_response):
    global_module.wifi_namespace_reference = Wifi_Namespace
    if environ['PATH_INFO'].startswith('/socket.io'):
        return socketio_manage(environ, { '/wifi': global_module.wifi_namespace_reference })


# socket io object made which starts a server at port 3000
def activate():
	try:
    	    global_module.sio_server = SocketIOServer(('', 3000),connect_wifi,policy_server=False)
	except:
	    e_msg = "An error occured in starting the server on port 3000"
	    print e_msg
		#made to listen
	try:
		global_module.sio_server.serve_forever()
	except KeyboardInterrupt:
		global_module.sio_server.stop()

 
