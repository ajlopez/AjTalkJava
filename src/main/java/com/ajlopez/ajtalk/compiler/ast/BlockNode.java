package com.ajlopez.ajtalk.compiler.ast;

public class BlockNode extends Node {
	private Node expression;
	private String[] arguments;
	
	public BlockNode(String[] arguments, Node expression) {
		this.arguments = arguments;
		this.expression = expression;
	}
	
	public Node getExpression() {
		return this.expression;
	}
	
	public String[] getArguments() {
		return this.arguments;
	}
}
