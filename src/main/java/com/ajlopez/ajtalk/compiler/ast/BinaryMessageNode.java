package com.ajlopez.ajtalk.compiler.ast;

public class BinaryMessageNode extends Node {
	private Node target;
	private String selector;
	private Node argument;
	
	public BinaryMessageNode(Node target, String selector, Node argument) {
		this.target = target;
		this.selector = selector;
		this.argument = argument;
	}
	
	public String getSelector() {
		return this.selector;
	}
	
	public Node getTarget() {
		return this.target;
	}
	
	public Node getArgument() {
		return this.argument;
	}
}
