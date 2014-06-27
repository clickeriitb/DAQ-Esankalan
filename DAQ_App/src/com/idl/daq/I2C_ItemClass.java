package com.idl.daq;

public class I2C_ItemClass {

	String type;
	String addr;
	String val;
	int delay;

	public I2C_ItemClass(){
		type = "";
		addr = "";
		val = "";
		delay = 1000;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getAddr() {
		return addr;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getVal() {
		return val;
	}

	public void setDelay(String delay) {
		this.delay = Integer.parseInt(delay);
	}
	public int getDelay(){
		return this.delay;
	}
	
	public String getInfo(){
		if(type.equals("read")){
			return "r:"+addr;
		}else if(type.equals("write")){
			return "w:"+addr+":"+val;
		}else{
			return "d:"+delay;
		}
	}
}
