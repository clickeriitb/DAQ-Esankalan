package com.idl.daq;

import android.util.Log;

public class I2C_ItemClass {

	String type;
	String addr;
	String val;
	boolean signed;
	int delay;

	public I2C_ItemClass(){
		type = "";
		addr = "";
		val = "";
		delay = 1000;
		signed = false;
	}
	
	
	public boolean isSigned() {
		return signed;
	}


	public void setSigned(boolean signed) {
		this.signed = signed;
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
			if(signed){
				return "rs:"+addr;
			}else{
				return "ru:"+addr;
			}
		}else if(type.equals("write")){
			Log.e("write val",val);
			return "w:"+addr+":"+val;
		}else{
			return "d:"+delay;
		}
	}
}
