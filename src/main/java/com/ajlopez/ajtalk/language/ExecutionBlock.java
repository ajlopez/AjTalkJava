package com.ajlopez.ajtalk.language;

public class ExecutionBlock {
	private Block block;
	private Object[] locals;
	
	public ExecutionBlock(Block block) {
		this.block = block;
		
		if (block.nlocals > 0)
			this.locals = new Object[block.nlocals];
	}
	
	public Object execute(Object self, Object[] arguments) {
		Object[] stack = new Object[5];
		int position = 0;
		int ip = 0;
		int bclength = this.block.bytecodes.length;
		
		while (ip < bclength) {
			switch (this.block.bytecodes[ip]) {
			case Bytecodes.RETURN:
				return stack[position-1];
			case Bytecodes.LOADARG:
				ip++;
				stack[position++] = arguments[this.block.bytecodes[ip]];
			}
			
			ip++;
		}
		
		return self;
	}
}
