package com.ajlopez.ajtalk.language;

import com.ajlopez.ajtalk.Machine;

public interface IBlock {
	Object execute(Object[] arguments, Machine machine);
}
