package com.ajlopez.ajtalk.compiler.ast;

public class ExpressionArrayNode extends Node {
	private Node[] expressions;
	
	public ExpressionArrayNode(Node[] expressions) {
		this.expressions = expressions;
	}
	
	public Node[] getExpressions() {
		return this.expressions;
	}
}
