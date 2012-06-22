package com.ajlopez.ajtalk.compiler;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import com.ajlopez.ajtalk.compiler.ast.MethodNode;
import com.ajlopez.ajtalk.compiler.ast.Node;

public class ParserFileTests {
	@Test
	public void parseMethod1() throws IOException, ParserException, LexerException {
		Node node = this.parseMethodFromResource("Method1.st");
		assertNotNull(node);
		assertTrue(node instanceof MethodNode);
		
		MethodNode mnode = (MethodNode)node;
		assertEquals("directionToLineFrom:to:", mnode.getSelector());
		assertNotNull(mnode.getArguments());
		assertEquals(2, mnode.getArguments().length);
		assertEquals("p1", mnode.getArguments()[0]);
		assertEquals("p2", mnode.getArguments()[1]);
	}
	
	@Test
	public void parseMethodAngle() throws IOException, ParserException, LexerException {
		Node node = this.parseMethodFromResource("MethodAngle.st");
		assertNotNull(node);
		assertTrue(node instanceof MethodNode);
		
		MethodNode mnode = (MethodNode)node;
		assertEquals("angle", mnode.getSelector());
		assertNull(mnode.getArguments());
	}
	
	private Node parseMethodFromResource(String name) throws IOException, ParserException, LexerException {
		Lexer lexer = new Lexer(new InputStreamReader(getClass().getResourceAsStream(name)));
		Parser parser = new Parser(lexer);
		return parser.parseMethodNode();
	}
}
