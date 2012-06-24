package com.ajlopez.ajtalk.compiler.ast;

public class PrimitiveNode extends Node {
	private int value;
	
	public PrimitiveNode(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
}
