package com.ajlopez.ajtalk.compiler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

		return this.parseKeywordExpression();
	}
	
	private Node parseKeywordExpression() throws ParserException, IOException, LexerException {
		Node expression = this.parseBinaryExpression();
		Token token = null;
		List<Node> arguments = null;
		String selector = null;
		
		for (token = this.nextToken(); token != null && token.getType() == TokenType.KEYSELECTOR; token = this.nextToken()) {
			if (selector == null) {
				arguments = new ArrayList<Node>();
				selector = token.getValue();
			}
			else
				selector += token.getValue();
			arguments.add(this.parseBinaryExpression());
		}
		
		this.pushToken(token);
		
		if (selector != null) {
			Node[] args = new Node[arguments.size()];
			expression = new KeywordMessageNode(expression, selector, arguments.toArray(args));
		}
		
		return expression;
	}
	
	private Node parseBinaryExpression() throws ParserException, IOException, LexerException {
		Node expression = this.parseUnaryExpression();
		Token token = null;
		
		for (token = this.nextToken(); token != null && token.getType() == TokenType.BINSELECTOR; token = this.nextToken())
			expression = new BinaryMessageNode(expression, token.getValue(), this.parseUnaryExpression());
		
		this.pushToken(token);
		
		return expression;
	}
	
	private Node parseUnaryExpression() throws ParserException, IOException, LexerException {
		Node expression = this.parseTerm();
		Token token = null;
		
		for (token = this.nextToken(); token != null && token.getType() == TokenType.ID; token = this.nextToken())
			expression = new UnaryMessageNode(expression, token.getValue());
		
		this.pushToken(token);
		
		return expression;
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
