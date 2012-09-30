package com.ajlopez.ajtalk.compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ajlopez.ajtalk.compiler.ast.*;
import com.ajlopez.ajtalk.language.*;

public class Compiler {
	private Node node;
	private List<String> argnames;
	private List<String> localnames;
	private List<Byte> codes = new ArrayList<Byte>();
	private List<Object> values = new ArrayList<Object>();
	
	public Compiler(Node node) {
		this.node = node;
	}
	
	public IMethod compileMethod() throws CompilerException {
		MethodNode node = (MethodNode)this.node;
		String[] locals = node.getLocals();
		if (locals != null)
			this.localnames = Arrays.asList(node.getLocals());
		String[] arguments = node.getArguments();
		if (arguments != null)
			this.argnames = Arrays.asList(node.getArguments());
		this.compileNode(node);
		return makeMethod();
	}
	
	public IBlock compileBlock() throws CompilerException {
		this.compileNode(this.node);
		return makeBlock();
	}
	
	private void compileNode(Node node) throws CompilerException {
		if (node instanceof IntegerNode) {
			this.compileNode((IntegerNode)node);
			return;
		}

		if (node instanceof StringNode) {
			this.compileNode((StringNode)node);
			return;
		}

		if (node instanceof SymbolNode) {
			this.compileNode((SymbolNode)node);
			return;
		}

		if (node instanceof CharacterNode) {
			this.compileNode((CharacterNode)node);
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

		if (node instanceof KeywordMessageNode) {
			this.compileNode((KeywordMessageNode)node);
			return;
		}

		if (node instanceof CompositeExpressionNode) {
			this.compileNode((CompositeExpressionNode)node);
			return;
		}

		if (node instanceof AssignmentNode) {
			this.compileNode((AssignmentNode)node);
			return;
		}
		
		throw new CompilerException("Unexpected Node");
	}
	
	private void compileNode(CompositeExpressionNode node) throws CompilerException {
		int nexpr = 0;
		for (Node expression : node.getExpressions()) {
			if (nexpr > 0)
				this.compileBytecode((byte) Bytecodes.CLEAR);
			nexpr++;
			this.compileNode(expression);
		}
	}
	
	private void compileNode(MethodNode node) throws CompilerException {
		this.compileNode(node.getExpression());
	}
	
	private void compileNode(ReturnNode node) throws CompilerException {
		this.compileNode(node.getExpression());
		this.compileBytecode((byte)Bytecodes.RETURN);
	}
	
	private void compileNode(AssignmentNode node) throws CompilerException {
		String name = node.getTarget();
		this.compileNode(node.getExpression());
		this.compileBytecode((byte)Bytecodes.SETGLOBAL);
		this.compileValue(name);
	}
	
	private void compileNode(IdNode node) {
		int position;
		
		if (this.localnames != null) {
			position = this.localnames.indexOf(node.getName());
			if (position >= 0) {
				this.compileBytecode((byte)Bytecodes.GETLOCAL);
				this.compileBytecode((byte)position);
				return;
			}
		}
		if (this.argnames != null) {
			position = this.argnames.indexOf(node.getName());
			if (position >= 0) {
				this.compileBytecode((byte)Bytecodes.GETARGUMENT);
				this.compileBytecode((byte)position);
				return;
			}
		}
		this.compileBytecode((byte)Bytecodes.GETGLOBAL);
		this.compileValue(node.getName());
		return;
	}
	
	private void compileNode(UnaryMessageNode node) throws CompilerException {
		this.compileNode(node.getTarget());
		this.compileBytecode((byte)Bytecodes.SEND);
		this.compileValue(node.getSelector());
		this.compileBytecode((byte)0);
		return;
	}
	
	private void compileNode(BinaryMessageNode node) throws CompilerException {
		this.compileNode(node.getTarget());
		this.compileNode(node.getArgument());
		this.compileBytecode((byte)Bytecodes.SEND);
		this.compileValue(node.getSelector());
		this.compileBytecode((byte)1);
		return;
	}
	
	private void compileNode(KeywordMessageNode node) throws CompilerException {
		this.compileNode(node.getTarget());
		for (Node argument: node.getArguments())
			this.compileNode(argument);
		this.compileBytecode((byte)Bytecodes.SEND);
		this.compileValue(node.getSelector());
		this.compileBytecode((byte)node.getArguments().length);
		return;
	}
	
	private void compileNode(CharacterNode node) {
		char value = node.getValue();
		this.compileBytecode((byte)Bytecodes.GETVALUE);
		this.compileValue(value);
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
	
	private void compileNode(SymbolNode node) {
		String value = node.getValue();
		this.compileBytecode((byte) Bytecodes.GETVALUE);
		this.compileValue(value);
		return;
	}
	
	private IBlock makeBlock()
	{
		byte[] bytecodes = new byte[this.codes.size()];
		Object[] objects = new Object[this.values.size()];
		int position = 0;
		
		for (Byte bt : this.codes)
			bytecodes[position++] = bt.byteValue();
				
		return new Block(0, 0, bytecodes, this.values.toArray(objects));
	}
	
	private IMethod makeMethod()
	{
		byte[] bytecodes = new byte[this.codes.size()];
		Object[] objects = new Object[this.values.size()];
		int position = 0;
		
		for (Byte bt : this.codes)
			bytecodes[position++] = bt.byteValue();
		
		int argsize = this.argnames == null ? 0 : this.argnames.size();
		int locsize = this.localnames == null ? 0 : this.localnames.size();
				
		return new Method(argsize, locsize, bytecodes, this.values.toArray(objects));
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
	
	private void compileValue(char value) {
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

