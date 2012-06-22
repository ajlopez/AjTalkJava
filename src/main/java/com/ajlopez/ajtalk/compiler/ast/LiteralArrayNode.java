package com.ajlopez.ajtalk.compiler.ast;

public class LiteralArrayNode extends Node {
	private Node[] elements;
	
	public LiteralArrayNode(Node[] elements) {
		this.elements = elements;
	}
	
	public Node[] getElements() {
		return this.elements;
	}
}
