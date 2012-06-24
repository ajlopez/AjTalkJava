package com.ajlopez.ajtalk.language;

import com.ajlopez.ajtalk.ExecutionException;
import com.ajlopez.ajtalk.Machine;

public class Method extends Block implements IMethod {
	public Method(int arity, int nlocals, byte[] bytecodes) {
		this(arity, nlocals, bytecodes, null);
	}
	
	public Method(int arity, int nlocals, byte[] bytecodes, Object[] values) {
		super(arity, nlocals, bytecodes, values);
	}
	
	@Override
	public Object execute(Object self, Object[] arguments, Machine machine) throws ExecutionException {
		ExecutionBlock exeblock = new ExecutionBlock(this);
		return exeblock.execute(self, arguments, machine);
	}	
}
