package com.ajlopez.ajtalk.compiler;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.ajlopez.ajtalk.compiler.ast.*;

public class ParserTests {
	@Test
	public void nullNode() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("");
		assertNull(parser.parseExpressionNode());
	}

	@Test
	public void integerNode() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("123");
		
		Node node = parser.parseExpressionNode();
		
		assertNotNull(node);
		assertTrue(node instanceof IntegerNode);
		assertEquals(123, ((IntegerNode)node).getValue());
		
		assertNull(parser.parseExpressionNode());
	}

	@Test
	public void stringNode() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("'foo'");
		
		Node node = parser.parseExpressionNode();
		
		assertNotNull(node);
		assertTrue(node instanceof StringNode);
		assertEquals("foo", ((StringNode)node).getValue());
		
		assertNull(parser.parseExpressionNode());
	}

	@Test
	public void idNode() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("foo");
		
		Node node = parser.parseExpressionNode();
		
		assertNotNull(node);
		assertTrue(node instanceof IdNode);
		assertEquals("foo", ((IdNode)node).getName());
		
		assertNull(parser.parseExpressionNode());
	}

	@Test
	public void unaryMessageNode() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("foo bar");
		
		Node node = parser.parseExpressionNode();
		
		assertNotNull(node);
		assertTrue(node instanceof UnaryMessageNode);
		assertEquals("bar", ((UnaryMessageNode)node).getSelector());
		
		assertNull(parser.parseExpressionNode());
	}
}
