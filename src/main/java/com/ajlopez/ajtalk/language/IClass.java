package com.ajlopez.ajtalk.language;

public interface IClass extends IBehavior {
	int getObjectSize();
	int getVariableOffset(String name);
}
