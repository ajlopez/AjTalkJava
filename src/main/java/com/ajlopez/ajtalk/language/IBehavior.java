package com.ajlopez.ajtalk.language;

public interface IBehavior {
	IBehavior getSuperBehavior();
	IMethod getMethod(String selector);
	void defineMethod(String selector, IMethod method);
}
