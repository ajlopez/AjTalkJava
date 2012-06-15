package com.ajlopez.ajtalk.language;

public class BaseObject implements IObject {
	private IBehavior klass;
	
	public BaseObject(IBehavior klass) {
		this.klass = klass;
	}

	@Override
	public IBehavior getBehavior() {
		return this.klass;
	}

	@Override
	public Object send(String selector, Object[] arguments) {
		IMethod method = this.klass.getMethod(selector);
		return method.execute(this, arguments);
	}
}
