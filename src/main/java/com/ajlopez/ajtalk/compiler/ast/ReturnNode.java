package com.ajlopez.ajtalk.compiler.ast;

public class ReturnNode extends Node {
	private Node expression;
	
	public ReturnNode(Node expression) {
		this.expression = expression;
	}
	
	public Node getExpression() {
		return this.expression;
	}
}
