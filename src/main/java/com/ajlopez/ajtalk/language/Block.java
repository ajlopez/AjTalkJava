package com.ajlopez.ajtalk.language;

public class Block implements IBlock {
	int arity;
	int nlocals;
	byte[] bytecodes;
	
	public Block(int arity, int nlocals, byte[] bytecodes) {
		this.arity = arity;
		this.nlocals = nlocals;
		this.bytecodes = bytecodes;
	}

	@Override
	public Object execute(Object[] arguments) {
		ExecutionBlock exeblock = new ExecutionBlock(this);
		return exeblock.execute(null, arguments);
	}		
}
