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
	public void keywordMessageNodeMultiline() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("x \r\n  foo: 1\r\n bar: 2");
		
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
	public void literalArray() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("#(1 'Hello' #symbol $x)");
		
		Node node = parser.parseExpressionNode();
		
		assertNotNull(node);
		assertTrue(node instanceof LiteralArrayNode);
		
		LiteralArrayNode lanode = (LiteralArrayNode)node;
		
		assertNotNull(lanode.getElements());
		assertEquals(4, lanode.getElements().length);
		
		assertNull(parser.parseExpressionNode());
	}

	@Test
	public void expressionArray() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("{1+2. foo value. 'Hello' size. foo bar: 2}");
		
		Node node = parser.parseExpressionNode();
		
		assertNotNull(node);
		assertTrue(node instanceof ExpressionArrayNode);
		
		ExpressionArrayNode lanode = (ExpressionArrayNode)node;
		
		assertNotNull(lanode.getExpressions());
		assertEquals(4, lanode.getExpressions().length);
		
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
		assertNull(bnode.getArguments());
		assertNull(bnode.getLocals());
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
		
		String[] arguments = bnode.getArguments();
		assertEquals(2, arguments.length);
		assertEquals("x", arguments[0]);
		assertEquals("y", arguments[1]);
		
		assertNull(bnode.getLocals());

		assertNull(parser.parseExpressionNode());
	}

	@Test
	public void simpleBlockWithArgumentsAndLocals() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("[:x :y | |a b| foo do: bar. 1 + 2. bar do: foo]");
		
		Node node = parser.parseExpressionNode();
		
		assertNotNull(node);
		assertTrue(node instanceof BlockNode);
		
		BlockNode bnode = (BlockNode)node;
		
		assertNotNull(bnode.getExpression());
		assertTrue(bnode.getExpression() instanceof CompositeExpressionNode);
		assertNotNull(bnode.getArguments());
		
		String[] arguments = bnode.getArguments();
		assertEquals(2, arguments.length);
		assertEquals("x", arguments[0]);
		assertEquals("y", arguments[1]);
		
		assertNotNull(bnode.getLocals());
		
		String[] locals = bnode.getLocals();
		assertEquals(2, locals.length);
		assertEquals("a", locals[0]);
		assertEquals("b", locals[1]);

		assertNull(parser.parseExpressionNode());
	}

	@Test
	public void simpleAssignment() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("a := foo do: bar with: 2 + 1");
		
		Node node = parser.parseExpressionNode();
		
		assertNotNull(node);
		assertTrue(node instanceof AssignmentNode);
		
		AssignmentNode anode = (AssignmentNode)node;
		
		assertNotNull(anode.getExpression());
		assertNotNull(anode.getTarget());
		assertEquals("a", anode.getTarget());

		assertNull(parser.parseExpressionNode());
	}

	@Test
	public void simpleReturn() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("^a + 2 - 1");
		
		Node node = parser.parseExpressionNode();
		
		assertNotNull(node);
		assertTrue(node instanceof ReturnNode);

		assertNull(parser.parseExpressionNode());
	}

	@Test
	public void returnWithExpression() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("^ap >= ar ifTrue: [ap - ar] ifFalse: [Float pi * 2 - ar + ap]");
		
		Node node = parser.parseExpressionNode();
		
		assertNotNull(node);
		assertTrue(node instanceof ReturnNode);

		assertNull(parser.parseExpressionNode());
	}
	
	@Test
	public void unaryMethod() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("x ^x");
		
		Node node = parser.parseMethodNode();
		
		assertNotNull(node);
		assertTrue(node instanceof MethodNode);
		
		MethodNode mnode = (MethodNode)node;
		assertEquals("x", mnode.getSelector());
		assertNull(mnode.getArguments());
		assertNull(mnode.getLocals());
		
		assertNull(parser.parseExpressionNode());
	}

	@Test
	public void unaryMethodWithLocals() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("x |y| y := x. ^y");
		
		Node node = parser.parseMethodNode();
		
		assertNotNull(node);
		assertTrue(node instanceof MethodNode);
		
		MethodNode mnode = (MethodNode)node;
		assertEquals("x", mnode.getSelector());
		assertNull(mnode.getArguments());
		assertEquals(1, mnode.getLocals().length);
		assertEquals("y", mnode.getLocals()[0]);
		
		assertNull(parser.parseExpressionNode());
	}

	@Test
	public void binaryMethod() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("+ y ^x+y");
		
		Node node = parser.parseMethodNode();
		
		assertNotNull(node);
		assertTrue(node instanceof MethodNode);
		
		MethodNode mnode = (MethodNode)node;
		assertEquals("+", mnode.getSelector());
		assertNotNull(mnode.getArguments());
		assertEquals(1, mnode.getArguments().length);
		assertEquals("y", mnode.getArguments()[0]);
		assertNull(mnode.getLocals());
		
		assertNull(parser.parseExpressionNode());
	}

	@Test
	public void binaryMethodWithLocals() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("+ y |z| z := y. ^x+z");
		
		Node node = parser.parseMethodNode();
		
		assertNotNull(node);
		assertTrue(node instanceof MethodNode);
		
		MethodNode mnode = (MethodNode)node;
		assertEquals("+", mnode.getSelector());
		assertNotNull(mnode.getArguments());
		assertEquals(1, mnode.getArguments().length);
		assertEquals("y", mnode.getArguments()[0]);
		assertEquals(1, mnode.getLocals().length);
		assertEquals("z", mnode.getLocals()[0]);
		
		assertNull(parser.parseExpressionNode());
	}

	@Test
	public void keywordMethod() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("with: a with:b ^a+b");
		
		Node node = parser.parseMethodNode();
		
		assertNotNull(node);
		assertTrue(node instanceof MethodNode);
		
		MethodNode mnode = (MethodNode)node;
		assertEquals("with:with:", mnode.getSelector());
		assertNotNull(mnode.getArguments());
		assertEquals(2, mnode.getArguments().length);
		assertEquals("a", mnode.getArguments()[0]);
		assertEquals("b", mnode.getArguments()[1]);
		assertNull(mnode.getLocals());
		
		assertNull(parser.parseExpressionNode());
	}

	@Test
	public void keywordMethodWithLocals() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("with: a with:b |c d| c := a. d := b. ^c+d");
		
		Node node = parser.parseMethodNode();
		
		assertNotNull(node);
		assertTrue(node instanceof MethodNode);
		
		MethodNode mnode = (MethodNode)node;
		assertEquals("with:with:", mnode.getSelector());
		assertNotNull(mnode.getArguments());
		assertEquals(2, mnode.getArguments().length);
		assertEquals("a", mnode.getArguments()[0]);
		assertEquals("b", mnode.getArguments()[1]);
		assertEquals(2, mnode.getLocals().length);
		assertEquals("c", mnode.getLocals()[0]);
		assertEquals("d", mnode.getLocals()[1]);
		
		assertNull(parser.parseExpressionNode());
	}

	@Test
	public void primitiveNode() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("<primitive: 60>");
		
		Node node = parser.parseExpressionNode();
		
		assertNotNull(node);
		assertTrue(node instanceof PrimitiveNode);
		assertEquals(60, ((PrimitiveNode)node).getValue());
		
		assertNull(parser.parseExpressionNode());
	}

	@Test
	public void primitiveNodeAndExpression() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("<primitive: 60> ^2");
		
		Node node = parser.parseExpressionNode();
		
		assertNotNull(node);
		assertTrue(node instanceof CompositeExpressionNode);
		
		CompositeExpressionNode cenode = (CompositeExpressionNode)node;
		
		assertNotNull(cenode.getExpressions());
		assertEquals(2, cenode.getExpressions().length);
		assertTrue(cenode.getExpressions()[0] instanceof PrimitiveNode);
		
		assertEquals(60, ((PrimitiveNode)cenode.getExpressions()[0]).getValue());
		
		assertNull(parser.parseExpressionNode());
	}
}
