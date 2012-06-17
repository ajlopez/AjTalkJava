package com.ajlopez.ajtalk.compiler.ast;

public class IdNode extends Node {
	private String name;
	
	public IdNode(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
