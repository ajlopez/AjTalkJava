package com.ajlopez.ajtalk.language;

public interface IObject {
	IBehavior getBehavior();
	Object getVariable(int offset);
	void setVariable(int offset, Object value);
	Object send(String selector, Object[] arguments);
}
