package com.ajlopez.ajtalk.compiler.ast;

public class MethodNode extends BlockNode {
	private String selector;
	
	public MethodNode(String selector, String[] arguments, String[] locals, Node expression) {
		super(arguments, locals, expression);
		this.selector = selector;
	}
	
	public String getSelector() {
		return this.selector;
	}
}
