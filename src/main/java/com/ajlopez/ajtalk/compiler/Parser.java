package com.ajlopez.ajtalk.compiler;

import java.io.IOException;

import com.ajlopez.ajtalk.compiler.ast.IntegerNode;
import com.ajlopez.ajtalk.compiler.ast.Node;

public class Parser {
	private Lexer lexer;
	
	public Parser(String text) {
		this(new Lexer(text));
	}
	
	public Parser(Lexer lexer) {
		this.lexer = lexer;
	}
	
	public Node parseNode() throws ParserException, IOException, LexerException {
		Token token = this.nextToken();
		
		if (token == null)
			return null;
		if (token.getType() == TokenType.INTEGER)
			return new IntegerNode(Integer.parseInt(token.getValue()));
		
		throw new ParserException("Unexpected '" + token.getValue() + "'");
	}
	
	private Token nextToken() throws IOException, LexerException {
		return this.lexer.nextToken();
	}
}
