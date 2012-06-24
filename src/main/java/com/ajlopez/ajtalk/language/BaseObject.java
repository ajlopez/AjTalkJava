package com.ajlopez.ajtalk.language;

import com.ajlopez.ajtalk.Machine;

public class BaseObject implements IObject {
	private IBehavior behavior;
	private Object[] values;
	
	public BaseObject(IBehavior behavior) {
		this.behavior = behavior;
		
		if (behavior instanceof IClass) {
			IClass klass = (IClass)behavior;
			int size = klass.getObjectSize();
			if (size > 0)
				this.values = new Object[size];
		}
	}

	@Override
	public IBehavior getBehavior() {
		return this.behavior;
	}

	@Override
	public Object send(String selector, Object[] arguments, Machine machine) {
		IMethod method = this.behavior.getMethod(selector);
		return method.execute(this, arguments, machine);
	}

	@Override
	public Object getVariable(int offset) {
		return this.values[offset];
	}

	@Override
	public void setVariable(int offset, Object value) {
		this.values[offset] = value;
	}
}
