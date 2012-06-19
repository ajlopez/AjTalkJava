package com.ajlopez.ajtalk.compiler.ast;

public class CompositeExpressionNode extends Node {
	private Node[] expressions;
	
	public CompositeExpressionNode(Node[] expressions) {
		this.expressions = expressions;
	}
	
	public Node[] getExpressions() {
		return this.expressions;
	}
}
