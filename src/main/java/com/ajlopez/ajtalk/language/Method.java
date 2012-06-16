package com.ajlopez.ajtalk.language;

public class Method implements IMethod {
	int arity;
	int nlocals;
	byte[] bytecodes;
	
	public Method(int arity, int nlocals, byte[] bytecodes) {
		this.arity = arity;
		this.nlocals = nlocals;
		this.bytecodes = bytecodes;
	}
	
	@Override
	public Object execute(Object self, Object[] arguments) {
		ExecutionBlock exeblock = new ExecutionBlock(this);
		return exeblock.execute(self, arguments);
	}	
}
