package com.ajlopez.ajtalk;

import java.util.*;

import com.ajlopez.ajtalk.language.*;

public class Machine {
	private Map<String,Object> values = new HashMap<String,Object>();
	
	public void initialize()
	{
		IClass metaclass = new BaseClass(null, null);
		IClass classProtoObject = new BaseClass(metaclass, null);
		this.setValue("ProtoObject", classProtoObject);
		IMethod subclass = new ClassSubclassMethod();
		metaclass.defineMethod("subclass:", subclass);
		IClass classObject = new BaseClass(metaclass, classProtoObject);
		this.setValue("Object", classObject);
	}
	
	public void setValue(String name, Object value) {
		values.put(name, value);
	}
	
	public Object getValue(String name) {
		return values.get(name);
	}
}

class ClassSubclassMethod implements IMethod {

	@Override
	public Object execute(Object self, Object[] arguments, Machine machine) throws ExecutionException {
		IBehavior behavior = (IBehavior) self;
		// TODO Review metaclass
		Object result = new BaseClass(((IObject)behavior).getBehavior(), behavior);
		machine.setValue((String)arguments[0], result);
		return result;
	}	
}
