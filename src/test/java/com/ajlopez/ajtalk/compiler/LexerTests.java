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
	public void simpleId() throws IOException {
		Lexer lexer = new Lexer("foo");
		Token token = lexer.nextToken();
		assertNotNull(token);
		assertEquals(TokenType.ID, token.getType());
		assertEquals("foo", token.getValue());
		assertNull(lexer.nextToken());
	}

	@Test
	public void simpleIdWithSpaces() throws IOException {
		Lexer lexer = new Lexer("  foo  ");
		Token token = lexer.nextToken();
		assertNotNull(token);
		assertEquals(TokenType.ID, token.getType());
		assertEquals("foo", token.getValue());
		assertNull(lexer.nextToken());
	}

	@Test
	public void simpleInteger() throws IOException {
		Lexer lexer = new Lexer("123");
		Token token = lexer.nextToken();
		assertNotNull(token);
		assertEquals(TokenType.INTEGER, token.getType());
		assertEquals("123", token.getValue());
		assertNull(lexer.nextToken());
	}
}
