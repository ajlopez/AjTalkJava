package com.ajlopez.ajtalk.compiler.ast;

public class KeywordMessageNode extends Node {
	private Node target;
	private String selector;
	private Node[] arguments;
	
	public KeywordMessageNode(Node target, String selector, Node[] arguments) {
		this.target = target;
		this.selector = selector;
		this.arguments = arguments;
	}
	
	public String getSelector() {
		return this.selector;
	}
	
	public Node getTarget() {
		return this.target;
	}
	
	public Node[] getArguments() {
		return this.arguments;
	}
}
