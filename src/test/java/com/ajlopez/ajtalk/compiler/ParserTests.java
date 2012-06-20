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
		
		Node target = ((UnaryMessageNode)node).getTarget();
		
		assertNotNull(target);
		assertTrue(target instanceof IdNode);
		assertEquals("foo", ((IdNode)target).getName());
		
		assertNull(parser.parseExpressionNode());
	}

	@Test
	public void twoUnaryMessageNodes() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("1 foo bar");
		
		Node node = parser.parseExpressionNode();
		
		assertNotNull(node);
		assertTrue(node instanceof UnaryMessageNode);
		assertEquals("bar", ((UnaryMessageNode)node).getSelector());
		
		Node target = ((UnaryMessageNode)node).getTarget();
		
		assertNotNull(target);
		assertTrue(target instanceof UnaryMessageNode);
		assertEquals("foo", ((UnaryMessageNode)target).getSelector());
		
		target = ((UnaryMessageNode)target).getTarget();
		
		assertNotNull(target);
		assertTrue(target instanceof IntegerNode);
		assertEquals(1, ((IntegerNode)target).getValue());
		
		assertNull(parser.parseExpressionNode());
	}

	@Test
	public void keywordMessageNodeWithOneArgument() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("foo bar: 1");
		
		Node node = parser.parseExpressionNode();
		
		assertNotNull(node);
		assertTrue(node instanceof KeywordMessageNode);
		assertEquals("bar:", ((KeywordMessageNode)node).getSelector());
		
		KeywordMessageNode knode = (KeywordMessageNode)node;		
		Node target = knode.getTarget();
		
		assertNotNull(target);
		assertTrue(target instanceof IdNode);
		assertEquals("foo", ((IdNode)target).getName());
		
		assertNotNull(knode.getArguments());
		assertEquals(1, knode.getArguments().length);
		
		Node argument = knode.getArguments()[0];
		
		assertNotNull(argument);
		assertTrue(argument instanceof IntegerNode);
		assertEquals(1, ((IntegerNode)argument).getValue());
		
		assertNull(parser.parseExpressionNode());
	}

	@Test
	public void keywordMessageNodeWithTwoArguments() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("x foo: 1 bar: 2");
		
		Node node = parser.parseExpressionNode();
		
		assertNotNull(node);
		assertTrue(node instanceof KeywordMessageNode);
		assertEquals("foo:bar:", ((KeywordMessageNode)node).getSelector());
		
		KeywordMessageNode knode = (KeywordMessageNode)node;		
		Node target = knode.getTarget();
		
		assertNotNull(target);
		assertTrue(target instanceof IdNode);
		assertEquals("x", ((IdNode)target).getName());
		
		assertNotNull(knode.getArguments());
		assertEquals(2, knode.getArguments().length);
		
		assertNull(parser.parseExpressionNode());
	}

	@Test
	public void idNodeInParentheses() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("(foo)");
		
		Node node = parser.parseExpressionNode();
		
		assertNotNull(node);
		assertTrue(node instanceof IdNode);
		assertEquals("foo", ((IdNode)node).getName());
		
		assertNull(parser.parseExpressionNode());
	}

	@Test
	public void keywordAndBinaryMessageInParentheses() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("(foo with: bar) + 2");
		
		Node node = parser.parseExpressionNode();
		
		assertNotNull(node);
		assertTrue(node instanceof BinaryMessageNode);
		
		BinaryMessageNode bnode = (BinaryMessageNode)node;
		
		assertTrue(bnode.getTarget() instanceof KeywordMessageNode);
		assertEquals("+", bnode.getSelector());
		
		assertNull(parser.parseExpressionNode());
	}

	@Test
	public void simpleComposite() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("foo do: bar. 1 + 2");
		
		Node node = parser.parseExpressionNode();
		
		assertNotNull(node);
		assertTrue(node instanceof CompositeExpressionNode);
		
		CompositeExpressionNode cenode = (CompositeExpressionNode)node;
		
		assertNotNull(cenode.getExpressions());
		assertEquals(2, cenode.getExpressions().length);
		
		assertNull(parser.parseExpressionNode());
	}

	@Test
	public void threeExpressionsInComposite() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("foo do: bar. 1 + 2. bar do: foo");
		
		Node node = parser.parseExpressionNode();
		
		assertNotNull(node);
		assertTrue(node instanceof CompositeExpressionNode);
		
		CompositeExpressionNode cenode = (CompositeExpressionNode)node;
		
		assertNotNull(cenode.getExpressions());
		assertEquals(3, cenode.getExpressions().length);
		
		assertNull(parser.parseExpressionNode());
	}

	@Test
	public void simpleBlock() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("[foo do: bar. 1 + 2. bar do: foo]");
		
		Node node = parser.parseExpressionNode();
		
		assertNotNull(node);
		assertTrue(node instanceof BlockNode);
		
		BlockNode bnode = (BlockNode)node;
		
		assertNotNull(bnode.getExpression());
		assertTrue(bnode.getExpression() instanceof CompositeExpressionNode);
		
		CompositeExpressionNode cenode = (CompositeExpressionNode)bnode.getExpression();
		
		assertNotNull(cenode.getExpressions());
		assertEquals(3, cenode.getExpressions().length);
		
		assertNull(parser.parseExpressionNode());
	}

	@Test
	public void simpleBlockWithArguments() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("[:x :y | foo do: bar. 1 + 2. bar do: foo]");
		
		Node node = parser.parseExpressionNode();
		
		assertNotNull(node);
		assertTrue(node instanceof BlockNode);
		
		BlockNode bnode = (BlockNode)node;
		
		assertNotNull(bnode.getExpression());
		assertTrue(bnode.getExpression() instanceof CompositeExpressionNode);
		assertNotNull(bnode.getArguments());
		assertEquals(2, bnode.getArguments().length);
		
		assertNull(parser.parseExpressionNode());
	}
}
