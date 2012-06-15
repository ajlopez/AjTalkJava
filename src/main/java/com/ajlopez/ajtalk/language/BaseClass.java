package com.ajlopez.ajtalk.language;

import java.util.HashMap;
import java.util.Map;

public class BaseClass extends BaseObject implements IBehavior {
	Map<String, IMethod> methods = new HashMap<String, IMethod>();
	
	public BaseClass(IBehavior klass) {
		super(klass);
	}

	@Override
	public IMethod getMethod(String selector) {
		return methods.get(selector);
	}
	
	public void defineMethod(String selector, IMethod method) {
		methods.put(selector, method);
	}
}
