package com.ajlopez.ajtalk.compiler;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.ajlopez.ajtalk.compiler.ast.IntegerNode;
import com.ajlopez.ajtalk.compiler.ast.Node;

public class ParserTests {
	@Test
	public void nullNode() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("");
		assertNull(parser.parseNode());
	}

	@Test
	public void integerNode() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("123");
		
		Node node = parser.parseNode();
		
		assertNotNull(node);
		assertTrue(node instanceof IntegerNode);
		assertEquals(123, ((IntegerNode)node).getValue());
		
		assertNull(parser.parseNode());
	}
}
