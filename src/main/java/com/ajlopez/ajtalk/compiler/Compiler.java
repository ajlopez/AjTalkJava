package com.ajlopez.ajtalk.compiler;

import java.util.ArrayList;
import java.util.List;

import com.ajlopez.ajtalk.compiler.ast.*;
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
		this.compileNode(this.node);
		return makeBlock();
	}
	
	private void compileNode(Node node) {
		if (node instanceof IntegerNode) {
			this.compileNode((IntegerNode)node);
			return;
		}

		if (node instanceof StringNode) {
			this.compileNode((StringNode)node);
			return;
		}

		if (node instanceof IdNode) {
			this.compileNode((IdNode)node);
			return;
		}

		if (node instanceof ReturnNode) {
			this.compileNode((ReturnNode)node);
			return;
		}

		if (node instanceof UnaryMessageNode) {
			this.compileNode((UnaryMessageNode)node);
			return;
		}

		if (node instanceof BinaryMessageNode) {
			this.compileNode((BinaryMessageNode)node);
			return;
		}
	}
	
	private void compileNode(ReturnNode node) {
		this.compileNode(node.getExpression());
		this.compileBytecode((byte)Bytecodes.RETURN);
	}
	
	private void compileNode(IdNode node) {
		this.compileBytecode((byte)Bytecodes.GETGLOBAL);
		this.compileValue(node.getName());
		return;
	}
	
	private void compileNode(UnaryMessageNode node) {
		this.compileNode(node.getTarget());
		this.compileBytecode((byte)Bytecodes.SEND);
		this.compileValue(node.getSelector());
		this.compileBytecode((byte)0);
		return;
	}
	
	private void compileNode(BinaryMessageNode node) {
		this.compileNode(node.getTarget());
		this.compileNode(node.getArgument());
		this.compileBytecode((byte)Bytecodes.SEND);
		this.compileValue(node.getSelector());
		this.compileBytecode((byte)1);
		return;
	}
	
	private void compileNode(IntegerNode node) {
		int value = node.getValue();
		this.compileBytecode((byte)Bytecodes.GETVALUE);
		this.compileValue(value);
		return;
	}
	
	private void compileNode(StringNode node) {
		String value = node.getValue();
		this.compileBytecode((byte) Bytecodes.GETVALUE);
		this.compileValue(value);
		return;
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
	
	private void compileValue(Object value) {
		int position = this.values.indexOf(value);
		
		if (position < 0) {
			this.values.add(value);
			position = this.values.size() - 1;
		}
		
		this.compileBytecode((byte)position);
	}
}

