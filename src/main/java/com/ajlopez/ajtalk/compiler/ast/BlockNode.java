package com.ajlopez.ajtalk.compiler.ast;

public class BlockNode extends Node {
	private Node expression;
	private String[] arguments;
	private String[] locals;
	
	public BlockNode(String[] arguments, String[] locals, Node expression) {
		this.arguments = arguments;
		this.locals = locals;
		this.expression = expression;
	}
	
	public Node getExpression() {
		return this.expression;
	}
	
	public String[] getArguments() {
		return this.arguments;
	}
	
	public String[] getLocals() {
		return this.locals;
	}
}
