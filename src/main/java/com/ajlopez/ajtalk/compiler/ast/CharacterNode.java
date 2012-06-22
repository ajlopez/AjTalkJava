package com.ajlopez.ajtalk.compiler.ast;

public class CharacterNode extends Node {
	private char value;
	
	public CharacterNode(char value) {
		this.value = value;
	}
	
	public char getValue() {
		return this.value;
	}
}
