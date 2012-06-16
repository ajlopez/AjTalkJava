package com.ajlopez.ajtalk.compiler.ast;

public class IntegerNode extends Node {
	private int value;
	
	public IntegerNode(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
}
