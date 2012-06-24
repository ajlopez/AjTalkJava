package com.ajlopez.ajtalk.language;

import com.ajlopez.ajtalk.Machine;

public interface IMethod {
	Object execute(Object self, Object[] arguments, Machine machine);
}
