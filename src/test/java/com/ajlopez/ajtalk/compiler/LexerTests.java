package com.ajlopez.ajtalk.compiler;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class LexerTests {

	@Test
	public void nullTokenWhenEmptyString() throws IOException, LexerException {
		Lexer lexer = new Lexer("");
		assertNull(lexer.nextToken());
	}

	@Test
	public void nullTokenWhenBlanckString() throws IOException, LexerException {
		Lexer lexer = new Lexer("   ");
		assertNull(lexer.nextToken());
	}

	@Test
	public void skipComment() throws IOException, LexerException {
		Lexer lexer = new Lexer(" \"This is a comment\"  ");
		assertNull(lexer.nextToken());
	}

	@Test(expected=LexerException.class)
	public void unclosedComment() throws IOException, LexerException {
		Lexer lexer = new Lexer(" \"This is a comment  ");
		assertNull(lexer.nextToken());
	}

	@Test
	public void simpleId() throws IOException, LexerException {
		Lexer lexer = new Lexer("foo");
		Token token = lexer.nextToken();
		assertNotNull(token);
		assertEquals(TokenType.ID, token.getType());
		assertEquals("foo", token.getValue());
		assertNull(lexer.nextToken());
	}

	@Test
	public void simpleArgumentName() throws IOException, LexerException {
		Lexer lexer = new Lexer(":foo");
		
		Token token = lexer.nextToken();
		assertNotNull(token);
		assertEquals(TokenType.SEPARATOR, token.getType());
		assertEquals(":", token.getValue());
		
		token = lexer.nextToken();
		assertNotNull(token);
		assertEquals(TokenType.ID, token.getType());
		assertEquals("foo", token.getValue());
		assertNull(lexer.nextToken());
	}

	@Test
	public void assignment() throws IOException, LexerException {
		Lexer lexer = new Lexer(":=");
		
		Token token = lexer.nextToken();
		assertNotNull(token);
		assertEquals(TokenType.BINSELECTOR, token.getType());
		assertEquals(":=", token.getValue());
		
		assertNull(lexer.nextToken());
	}

	@Test
	public void simpleIdWithSpaces() throws IOException, LexerException {
		Lexer lexer = new Lexer("  foo  ");
		Token token = lexer.nextToken();
		assertNotNull(token);
		assertEquals(TokenType.ID, token.getType());
		assertEquals("foo", token.getValue());
		assertNull(lexer.nextToken());
	}

	@Test
	public void simpleInteger() throws IOException, LexerException {
		Lexer lexer = new Lexer("123");
		Token token = lexer.nextToken();
		assertNotNull(token);
		assertEquals(TokenType.INTEGER, token.getType());
		assertEquals("123", token.getValue());
		assertNull(lexer.nextToken());
	}

	@Test
	public void simpleString() throws IOException, LexerException {
		Lexer lexer = new Lexer("'bar'");
		Token token = lexer.nextToken();
		assertNotNull(token);
		assertEquals(TokenType.STRING, token.getType());
		assertEquals("bar", token.getValue());
		assertNull(lexer.nextToken());
	}

	@Test(expected=LexerException.class)
	public void simpleUnclosedString() throws IOException, LexerException {
		Lexer lexer = new Lexer("'bar");
		lexer.nextToken();
	}

	@Test
	public void separators() throws IOException, LexerException {
		Lexer lexer = new Lexer("()[].:{}|#(");
		Token token = lexer.nextToken();
		
		assertNotNull(token);
		assertEquals(TokenType.SEPARATOR, token.getType());
		assertEquals("(", token.getValue());

		token = lexer.nextToken();		

		assertNotNull(token);
		assertEquals(TokenType.SEPARATOR, token.getType());
		assertEquals(")", token.getValue());

		token = lexer.nextToken();
		
		assertNotNull(token);
		assertEquals(TokenType.SEPARATOR, token.getType());
		assertEquals("[", token.getValue());

		token = lexer.nextToken();		

		assertNotNull(token);
		assertEquals(TokenType.SEPARATOR, token.getType());
		assertEquals("]", token.getValue());
		
		token = lexer.nextToken();		

		assertNotNull(token);
		assertEquals(TokenType.SEPARATOR, token.getType());
		assertEquals(".", token.getValue());
		
		token = lexer.nextToken();		

		assertNotNull(token);
		assertEquals(TokenType.SEPARATOR, token.getType());
		assertEquals(":", token.getValue());
		
		token = lexer.nextToken();		

		assertNotNull(token);
		assertEquals(TokenType.SEPARATOR, token.getType());
		assertEquals("{", token.getValue());
		
		token = lexer.nextToken();		

		assertNotNull(token);
		assertEquals(TokenType.SEPARATOR, token.getType());
		assertEquals("}", token.getValue());
		
		token = lexer.nextToken();		

		assertNotNull(token);
		assertEquals(TokenType.SEPARATOR, token.getType());
		assertEquals("|", token.getValue());
		
		token = lexer.nextToken();		

		assertNotNull(token);
		assertEquals(TokenType.SEPARATOR, token.getType());
		assertEquals("#(", token.getValue());
		
		assertNull(lexer.nextToken());
	}

	@Test
	public void separatorsAndId() throws IOException, LexerException {
		Lexer lexer = new Lexer("(foo)");
		Token token = lexer.nextToken();
		
		assertNotNull(token);
		assertEquals(TokenType.SEPARATOR, token.getType());
		assertEquals("(", token.getValue());

		token = lexer.nextToken();		

		assertNotNull(token);
		assertEquals(TokenType.ID, token.getType());
		assertEquals("foo", token.getValue());

		token = lexer.nextToken();		

		assertNotNull(token);
		assertEquals(TokenType.SEPARATOR, token.getType());
		assertEquals(")", token.getValue());
		
		assertNull(lexer.nextToken());
	}

	@Test
	public void simpleKeySelector() throws IOException, LexerException {
		Lexer lexer = new Lexer("foo:");
		Token token = lexer.nextToken();
		
		assertNotNull(token);
		assertEquals(TokenType.KEYSELECTOR, token.getType());
		assertEquals("foo:", token.getValue());
		
		assertNull(lexer.nextToken());
	}

	@Test
	public void twoKeySelectors() throws IOException, LexerException {
		Lexer lexer = new Lexer("foo:bar:");
		Token token = lexer.nextToken();
		
		assertNotNull(token);
		assertEquals(TokenType.KEYSELECTOR, token.getType());
		assertEquals("foo:", token.getValue());
		
		token = lexer.nextToken();
		
		assertNotNull(token);
		assertEquals(TokenType.KEYSELECTOR, token.getType());
		assertEquals("bar:", token.getValue());
		
		assertNull(lexer.nextToken());
	}

	@Test
	public void simpleBinSelector() throws IOException, LexerException {
		Lexer lexer = new Lexer("+");
		Token token = lexer.nextToken();
		
		assertNotNull(token);
		assertEquals(TokenType.BINSELECTOR, token.getType());
		assertEquals("+", token.getValue());
		
		assertNull(lexer.nextToken());
	}

	@Test
	public void twoCharBinSelector() throws IOException, LexerException {
		Lexer lexer = new Lexer("++");
		Token token = lexer.nextToken();
		
		assertNotNull(token);
		assertEquals(TokenType.BINSELECTOR, token.getType());
		assertEquals("++", token.getValue());
		
		assertNull(lexer.nextToken());
	}

	@Test
	public void twoBinSelectors() throws IOException, LexerException {
		Lexer lexer = new Lexer("+ -");
		Token token = lexer.nextToken();
		
		assertNotNull(token);
		assertEquals(TokenType.BINSELECTOR, token.getType());
		assertEquals("+", token.getValue());

		token = lexer.nextToken();
		
		assertNotNull(token);
		assertEquals(TokenType.BINSELECTOR, token.getType());
		assertEquals("-", token.getValue());
		
		assertNull(lexer.nextToken());
	}

	@Test
	public void integersWithBinSelectors() throws IOException, LexerException {
		Lexer lexer = new Lexer("1+2");
		Token token = lexer.nextToken();
		
		assertNotNull(token);
		assertEquals(TokenType.INTEGER, token.getType());
		assertEquals("1", token.getValue());

		token = lexer.nextToken();
		
		assertNotNull(token);
		assertEquals(TokenType.BINSELECTOR, token.getType());
		assertEquals("+", token.getValue());

		token = lexer.nextToken();
		
		assertNotNull(token);
		assertEquals(TokenType.INTEGER, token.getType());
		assertEquals("2", token.getValue());
		
		assertNull(lexer.nextToken());
	}

	@Test
	public void simpleSymbol() throws IOException, LexerException {
		Lexer lexer = new Lexer("#foo");
		Token token = lexer.nextToken();
		assertNotNull(token);
		assertEquals(TokenType.SYMBOL, token.getType());
		assertEquals("foo", token.getValue());
		assertNull(lexer.nextToken());
	}

	@Test
	public void binarySymbol() throws IOException, LexerException {
		Lexer lexer = new Lexer("#<>");
		Token token = lexer.nextToken();
		assertNotNull(token);
		assertEquals(TokenType.SYMBOL, token.getType());
		assertEquals("<>", token.getValue());
		assertNull(lexer.nextToken());
	}

	@Test
	public void keywordSymbol() throws IOException, LexerException {
		Lexer lexer = new Lexer("#foo:bar:");
		Token token = lexer.nextToken();
		assertNotNull(token);
		assertEquals(TokenType.SYMBOL, token.getType());
		assertEquals("foo:bar:", token.getValue());
		assertNull(lexer.nextToken());
	}
}
