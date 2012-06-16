package com.ajlopez.ajtalk.compiler;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class LexerTests {

	@Test
	public void nullTokenWhenEmptyString() throws IOException {
		Lexer lexer = new Lexer("");
		assertNull(lexer.nextToken());
	}

	@Test
	public void nullTokenWhenBlanckString() throws IOException {
		Lexer lexer = new Lexer("   ");
		assertNull(lexer.nextToken());
	}

	@Test
	public void simpleName() throws IOException {
		Lexer lexer = new Lexer("foo");
		Token token = lexer.nextToken();
		assertNotNull(token);
		assertEquals(TokenType.NAME, token.getType());
		assertEquals("foo", token.getValue());
		assertNull(lexer.nextToken());
	}

	@Test
	public void simpleNameWithSpaces() throws IOException {
		Lexer lexer = new Lexer("  foo  ");
		Token token = lexer.nextToken();
		assertNotNull(token);
		assertEquals(TokenType.NAME, token.getType());
		assertEquals("foo", token.getValue());
		assertNull(lexer.nextToken());
	}
}
