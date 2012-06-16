package com.ajlopez.ajtalk.language;

public interface IBehavior {
	IMethod getMethod(String selector);
	void defineMethod(String selector, IMethod method);
}
