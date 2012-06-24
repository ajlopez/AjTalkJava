package com.ajlopez.ajtalk.language;

import com.ajlopez.ajtalk.ExecutionException;
import com.ajlopez.ajtalk.Machine;

public class ExecutionBlock {
	private Block block;
	private Object[] locals;
	
	public ExecutionBlock(Block block) {
		this.block = block;
		
		if (block.nlocals > 0)
			this.locals = new Object[block.nlocals];
	}
	
	public Object execute(Object self, Object[] arguments, Machine machine) throws ExecutionException {
		// TODO set variable stack
		Object[] stack = new Object[10];
		int position = 0;
		int ip = 0;
		int bclength = this.block.bytecodes.length;
		
		while (ip < bclength) {
			byte bc = this.block.bytecodes[ip];
			switch (bc) {
			case Bytecodes.RETURN:
				return stack[position-1];
			case Bytecodes.GETARGUMENT:
				ip++;
				stack[position++] = arguments[this.block.bytecodes[ip]];
				break;
			case Bytecodes.GETLOCAL:
				ip++;
				stack[position++] = this.locals[this.block.bytecodes[ip]];
				break;
			case Bytecodes.GETVALUE:
				ip++;
				stack[position++] = this.block.values[this.block.bytecodes[ip]];
				break;
			case Bytecodes.GETGLOBAL:
				ip++;
				stack[position++] = machine.getValue((String) this.block.values[this.block.bytecodes[ip]]);
				break;
			case Bytecodes.SETLOCAL:
				ip++;
				this.locals[this.block.bytecodes[ip]] = stack[--position];
				break;
			case Bytecodes.SETGLOBAL:
				ip++;
				machine.setValue((String)this.block.values[this.block.bytecodes[ip]], stack[--position]);
				break;
			case Bytecodes.GETVARIABLE:
				ip++;
				stack[position++] = ((IObject)self).getVariable(this.block.bytecodes[ip]);
				break;
			case Bytecodes.SETVARIABLE:
				ip++;
				((IObject)self).setVariable(this.block.bytecodes[ip], stack[--position]);
				break;
			case Bytecodes.ADD:
				Object op2 = stack[--position];
				Object op1 = stack[--position];
				Object result = null;
				if (op1 instanceof Integer && op2 instanceof Integer)
					result = ((Integer) op1).intValue() + ((Integer) op2).intValue();
				stack[position++] = result;
				break;
			case Bytecodes.SEND:
				ip++;
				String selector = (String)this.block.values[this.block.bytecodes[ip]];
				ip++;
				int arity = this.block.bytecodes[ip];
				Object[] args = null;
				
				if (arity > 0) {
					args = new Object[arity];
					for (int k=0; k < arity; k++)
						args[arity-k-1] = stack[--position];
				}
				
				Object target = stack[--position];
				
				if (target instanceof IObject) {
					stack[position++] = ((IObject)target).send(selector, args, machine);
				}
				else
					throw new ExecutionException("Sending message to not an IObject");
				break;				
			default:
				throw new ExecutionException("Invalid Bytecode " + bc);
			}
			
			ip++;
		}
		
		return self;
	}
}
