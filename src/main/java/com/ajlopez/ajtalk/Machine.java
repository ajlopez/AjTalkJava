package com.ajlopez.ajtalk;

import java.util.*;

import com.ajlopez.ajtalk.language.*;

public class Machine {
	private Map<String,Object> values = new HashMap<String,Object>();
	
	public void initialize()
	{
		IClass classProtoObject = new BaseClass(null);
		this.setValue("ProtoObject", classProtoObject);
		IClass classObject = new BaseClass(classProtoObject);
		this.setValue("Object", classObject);
	}
	
	public void setValue(String name, Object value) {
		values.put(name, value);
	}
	
	public Object getValue(String name) {
		return values.get(name);
	}
}
