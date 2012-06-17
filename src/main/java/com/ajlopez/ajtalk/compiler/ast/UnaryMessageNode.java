package com.ajlopez.ajtalk.compiler.ast;

public class UnaryMessageNode extends Node {
	private Node target;
	private String selector;
	
	public UnaryMessageNode(Node target, String selector) {
		this.target = target;
		this.selector = selector;
	}
	
	public String getSelector() {
		return this.selector;
	}
	
	public Node getTarget() {
		return this.target;
	}
}
