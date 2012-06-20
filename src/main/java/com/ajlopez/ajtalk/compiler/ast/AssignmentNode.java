package com.ajlopez.ajtalk.compiler.ast;

public class AssignmentNode extends Node {
	private String target;
	private Node expression;
	
	public AssignmentNode(String target, Node expression) {
		this.target = target;
		this.expression = expression;
	}
	
	public String getTarget() {
		return this.target;
	}
	
	public Node getExpression() {
		return this.expression;
	}
}
