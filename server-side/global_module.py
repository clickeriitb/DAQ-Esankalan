# the module contains all the global variables that can be different files at the same time 

# Mode defines the mode of ccommunication between client and server. Default mode is wifi
mode = 0
isConnected = False

#defining namespace object to be used globally for write operation in wifi mode
wifi_namespace_reference = 0

# thread object used for reading requests from client via usb
reader_thread = 0

# session variables 
current_session = []
logged = []

#database cursor object used for accessing the logging database
db_cursor =  None

# The vendor id of the identified android device connected to the beaglebone
right_vid = 0

#defining write and read stream objects for usb mode
ep_in = 0
ep_out = 0

