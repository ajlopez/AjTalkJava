package com.ajlopez.ajtalk.language;

import java.util.HashMap;
import java.util.Map;

public class BaseClass extends BaseObject implements IClass {
	private String[] instanceVariableNames;
	private Map<String, IMethod> methods = new HashMap<String, IMethod>();
	
	public BaseClass(IBehavior klass) {
		this(klass, null);
	}
	
	public BaseClass(IBehavior klass, String[] instanceVariableNames)
	{
		super(klass);
		this.instanceVariableNames = instanceVariableNames;
	}

	@Override
	public IMethod getMethod(String selector) {
		return methods.get(selector);
	}

	@Override
	public void defineMethod(String selector, IMethod method) {
		methods.put(selector, method);
	}
	
	@Override
	public int getObjectSize() {
		int basesize = this.getBaseObjectSize();
		
		if (this.instanceVariableNames == null)
			return basesize;
		
		return this.instanceVariableNames.length + basesize;
	}

	@Override
	public int getVariableOffset(String name) {
		int baseoffset = this.getBaseVariableOffset(name);
		
		if (baseoffset >= 0 || this.instanceVariableNames == null)
			return baseoffset;
		
		for (int k = 0; k < this.instanceVariableNames.length; k++)
			if (this.instanceVariableNames[k] == name)
				return k + this.getBaseObjectSize();
		
		return -1;
	}
	
	@Override
	public IObject createInstance() {
		return new BaseObject(this);
	}
	
	private int getBaseObjectSize() {
		IBehavior behavior = this.getBehavior();
		
		return (behavior instanceof IClass ? ((IClass)behavior).getObjectSize() : 0);
	}
	
	private int getBaseVariableOffset(String name) {
		IBehavior behavior = this.getBehavior();
		
		return (behavior instanceof IClass ? ((IClass)behavior).getVariableOffset(name) : -1);
	}
}
