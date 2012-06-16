package com.ajlopez.ajtalk.compiler;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class Lexer {
	private static final char StringDelimeter = '\''; 
	private Reader reader;
	
	public Lexer(String text) {
		this(new StringReader(text));
	}
	
	public Lexer(Reader reader) {
		this.reader = reader;
	}
	
	public Token nextToken() throws IOException, LexerException {
		int ich = this.reader.read();
		
		while (ich != -1 && (Character.isSpaceChar((char) ich) || Character.isISOControl((char) ich)))
			ich = this.reader.read();
		
		if (ich == -1)
			return null;
		
		char ch = (char) ich;
		
		if (Character.isDigit(ch))
			return this.nextInteger(ch);
		
		if (ch == StringDelimeter)
			return this.nextString();
		
		StringBuilder builder = new StringBuilder();
		builder.append(ch);
		
		ich = this.reader.read();
		
		while (ich != -1 && Character.isLetterOrDigit((char) ich)) {
			builder.append((char)ich);
			ich = this.reader.read();
		}
		
		return new Token(builder.toString(), TokenType.ID);
	}
	
	private Token nextInteger(char ch) throws IOException {
		StringBuilder builder = new StringBuilder();
		builder.append(ch);
		
		int ich = this.reader.read();
		
		while (ich != -1 && Character.isDigit((char)ich)) {
			builder.append((char)ich);
			ich = this.reader.read();
		}
		
		return new Token(builder.toString(), TokenType.INTEGER);		
	}
	
	private Token nextString() throws IOException, LexerException {
		StringBuilder builder = new StringBuilder();
		
		int ich = this.reader.read();
		
		while (ich != -1 && (char)ich != StringDelimeter) {
			builder.append((char)ich);
			ich = this.reader.read();
		}
		
		if (ich == -1)
			throw new LexerException("Unclosed string");
		
		return new Token(builder.toString(), TokenType.STRING);		
	}
}
