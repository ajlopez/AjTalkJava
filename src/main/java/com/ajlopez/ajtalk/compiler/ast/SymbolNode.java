package com.ajlopez.ajtalk.compiler.ast;

public class SymbolNode extends Node {
	private String value;
	
	public SymbolNode(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
