package com.ajlopez.ajtalk.compiler;

import java.util.ArrayList;
import java.util.List;

import com.ajlopez.ajtalk.compiler.ast.IntegerNode;
import com.ajlopez.ajtalk.compiler.ast.Node;
import com.ajlopez.ajtalk.language.Block;
import com.ajlopez.ajtalk.language.Bytecodes;

public class Compiler {
	private Node node;
	private List<Byte> codes = new ArrayList<Byte>();
	private List<Object> values = new ArrayList<Object>();
	
	public Compiler(Node node) {
		this.node = node;
	}
	
	public Block compileBlock() {		
		if (this.node instanceof IntegerNode) {
			IntegerNode inode = (IntegerNode)this.node;
			int value = inode.getValue();
			this.compileBytecode((byte) Bytecodes.GETVALUE);
			this.compileValue(value);
			return makeBlock();
		}
		
		return null;
	}
	
	private Block makeBlock()
	{
		byte[] bytecodes = new byte[this.codes.size()];
		Object[] objects = new Object[this.values.size()];
		int position = 0;
		
		for (Byte bt : this.codes)
			bytecodes[position++] = bt.byteValue();
				
		return new Block(0, 0, bytecodes, this.values.toArray(objects));
	}
	
	private void compileBytecode(byte code) {
		this.codes.add(code);
	}
	
	private void compileValue(int value) {
		int position = this.values.indexOf(value);
		
		if (position < 0) {
			this.values.add(value);
			position = this.values.size() - 1;
		}
		
		this.compileBytecode((byte)position);
	}
}

