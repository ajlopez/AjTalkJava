package com.ajlopez.ajtalk;

import java.util.*;

public class Machine {
	private Map<String,Object> values = new HashMap<String,Object>();
	
	public void setValue(String name, Object value) {
		values.put(name, value);
	}
	
	public Object getValue(String name) {
		return values.get(name);
	}
}
