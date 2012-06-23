package com.ajlopez.ajtalk.language;

public class Block implements IBlock {
	int arity;
	int nlocals;
	byte[] bytecodes;
	Object[] values;
	
	public Block(int arity, int nlocals, byte[] bytecodes) {
		this(arity, nlocals, bytecodes, null);
	}
	
	public Block(int arity, int nlocals, byte[] bytecodes, Object[] values) {
		this.arity = arity;
		this.nlocals = nlocals;
		this.bytecodes = bytecodes;
		this.values = values;
	}
	
	public int getArity() {
		return this.arity;
	}
	
	public int getNoLocals() {
		return this.nlocals;
	}
	
	public byte[] getBytecodes() {
		return this.bytecodes;
	}
	
	public Object[] getValues() {
		return this.values;
	}

	@Override
	public Object execute(Object[] arguments) {
		ExecutionBlock exeblock = new ExecutionBlock(this);
		return exeblock.execute(null, arguments);
	}		
}
