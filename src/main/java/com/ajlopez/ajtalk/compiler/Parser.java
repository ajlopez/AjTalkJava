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

		Node node = this.parseKeywordExpression();
		List<Node> list = null;
		
		for (token = this.nextToken(); token != null && token.getType() == TokenType.SEPARATOR && token.getValue().equals("."); token = this.nextToken()) {
			if (list == null) {
				list = new ArrayList<Node>();
				list.add(node);
			}
			
			list.add(this.parseKeywordExpression());
		}
		
		this.pushToken(token);
		
		if (list == null)		
			return node;
		
		Node[] nodes = new Node[list.size()];
		nodes = list.toArray(nodes);
		
		return new CompositeExpressionNode(nodes);
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
		
		switch (token.getType()) {
		case INTEGER:
			return new IntegerNode(Integer.parseInt(token.getValue()));
		case STRING:
			return new StringNode(token.getValue());
		case ID:
			return new IdNode(token.getValue());
		}
		
		if (token.getType() == TokenType.SEPARATOR && token.getValue().equals("(")) {
			Node expr = this.parseExpressionNode();
			this.parseToken(")", TokenType.SEPARATOR);
			return expr;
		}
		
		if (token.getType() == TokenType.SEPARATOR && token.getValue().equals("[")) {
			String[] arguments = this.parseArgumentNames();
			Node expr = this.parseExpressionNode();
			this.parseToken("]", TokenType.SEPARATOR);
			return new BlockNode(arguments, expr);
		}
		
		throw new ParserException("Unexpected '" + token.getValue() + "'");
	}
	
	private String[] parseArgumentNames() throws IOException, LexerException, ParserException {
		Token token = this.nextToken();
		
		if (!(token != null && token.getType()==TokenType.SEPARATOR && token.getValue().equals(":"))) {
			this.pushToken(token);
			return null;
		}
		
		List<String> names = new ArrayList<String>();
		
		names.add(this.parseId());
		
		for (token = this.nextToken(); token != null && token.getType()==TokenType.SEPARATOR && token.getValue().equals(":"); token = this.nextToken())
			names.add(this.parseId());
		
		if (token == null || token.getType() != TokenType.SEPARATOR || !token.getValue().equals("|"))
			throw new ParserException("Expected '|'");
		
		String[] argnames = new String[names.size()];
		return names.toArray(argnames);
	}
	
	private String parseId() throws IOException, LexerException, ParserException {
		Token token = this.nextToken();
		
		if (token == null || token.getType() != TokenType.ID)
			throw new ParserException("Expected name");
		
		return token.getValue();
	}
	
	private void parseToken(String value, TokenType type) throws IOException, LexerException, ParserException {
		Token token = this.nextToken();
		
		if (token == null || token.getType() != type || !token.getValue().equals(value))
			throw new ParserException("Expected '" + value + "'");
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
