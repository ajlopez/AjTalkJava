package com.ajlopez.ajtalk;

import java.util.*;

import com.ajlopez.ajtalk.language.*;

public class Machine {
	private Map<String,Object> values = new HashMap<String,Object>();
	
	public void initialize()
	{
		IClass metaclass = new BaseClass(null, null);

		IMethod subclass = new ClassSubclassMethod();
		metaclass.defineMethod("subclass:", subclass);
		metaclass.defineMethod("subclass:instanceVariableNames:", subclass);
		metaclass.defineMethod("subclass:instanceVariableNames:classVariableNames:", subclass);
		
		IClass classProtoObject = new BaseClass(metaclass, null);
		this.setValue("ProtoObject", classProtoObject);
		
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
		String[] instanceVariableNames = null;
		if (arguments.length > 1)
			instanceVariableNames = ((String)arguments[1]).split(" ");
		
		// TODO Review metaclass
		Object result = new BaseClass(((IObject)behavior).getBehavior(), behavior, instanceVariableNames);
		machine.setValue((String)arguments[0], result);
		return result;
	}	
}
