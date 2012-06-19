package com.ajlopez.ajtalk.compiler.ast;

public class BlockNode extends Node {
	private Node expression;
	
	public BlockNode(Node expression) {
		this.expression = expression;
	}
	
	public Node getExpression() {
		return this.expression;
	}
}
