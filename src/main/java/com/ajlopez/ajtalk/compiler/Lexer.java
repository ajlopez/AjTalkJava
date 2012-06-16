package com.ajlopez.ajtalk.compiler;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class Lexer {
	private Reader reader;
	
	public Lexer(String text) {
		this(new StringReader(text));
	}
	
	public Lexer(Reader reader) {
		this.reader = reader;
	}
	
	public Token nextToken() throws IOException {
		int ich = this.reader.read();
		
		while (ich != -1 && (Character.isSpaceChar((char) ich) || Character.isISOControl((char) ich)))
			ich = this.reader.read();
		
		if (ich == -1)
			return null;
		
		char ch = (char) ich;
		
		if (Character.isDigit(ch))
			return nextInteger(ch);
		
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
}
