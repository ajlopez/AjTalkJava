package com.ajlopez.ajtalk.compiler;

import java.io.IOException;

import com.ajlopez.ajtalk.compiler.ast.*;

public class Parser {
	private Lexer lexer;
	private Token next;
	
	public Parser(String text) {
		this(new Lexer(text));
	}
	
	public Parser(Lexer lexer) {
		this.lexer = lexer;
	}
	
	public Node parseExpressionNode() throws ParserException, IOException, LexerException {
		Token token = this.nextToken();
		
		if (token == null)
			return null;

		this.pushToken(token);
		
		Node term = this.parseTerm();
		
		token = this.nextToken();
		
		if (token == null)
			return term;
		if (token.getType() == TokenType.ID)
			return new UnaryMessageNode(term, token.getValue());
		
		throw new ParserException("Unexpected '" + token.getValue() + "'");
	}
	
	private Node parseTerm() throws ParserException, IOException, LexerException
	{
		Token token = this.nextToken();
		
		if (token == null)
			return null;
		if (token.getType() == TokenType.INTEGER)
			return new IntegerNode(Integer.parseInt(token.getValue()));
		if (token.getType() == TokenType.STRING)
			return new StringNode(token.getValue());
		if (token.getType() == TokenType.ID)
			return new IdNode(token.getValue());
		
		throw new ParserException("Unexpected '" + token.getValue() + "'");
	}
	
	private Token nextToken() throws IOException, LexerException {
		if (next != null) {
			Token token = next;
			next = null;
			return token;
		}
		
		return this.lexer.nextToken();
	}
	
	private void pushToken(Token token) {
		this.next = token;
	}
}
