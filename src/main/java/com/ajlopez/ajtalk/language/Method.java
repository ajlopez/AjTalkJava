package com.ajlopez.ajtalk.language;

public class Method extends Block implements IMethod {
	public Method(int arity, int nlocals, byte[] bytecodes) {
		super(arity, nlocals, bytecodes);
	}
	
	@Override
	public Object execute(Object self, Object[] arguments) {
		ExecutionBlock exeblock = new ExecutionBlock(this);
		return exeblock.execute(self, arguments);
	}	
}
