package com.ajlopez.ajtalk.language;

public interface IObject {
	IBehavior getBehavior();
	Object send(String selector, Object[] arguments);
}
