package com.ajlopez.ajtalk.language;

public class ExecutionBlock {
	private Method method;
	private Object[] locals;
	
	public ExecutionBlock(Method method) {
		this.method = method;
		
		if (method.nlocals > 0)
			this.locals = new Object[method.nlocals];
	}
	
	public Object execute(Object self, Object[] arguments) {
		Object[] stack = new Object[5];
		int position = 0;
		int ip = 0;
		int bclength = this.method.bytecodes.length;
		
		while (ip < bclength) {
			switch (this.method.bytecodes[ip]) {
			case Bytecodes.RETURN:
				return stack[position-1];
			case Bytecodes.LOADARG:
				ip++;
				stack[position++] = arguments[this.method.bytecodes[ip]];
			}
			
			ip++;
		}
		
		return self;
	}
}
