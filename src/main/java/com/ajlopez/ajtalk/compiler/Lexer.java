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
		
		StringBuilder builder = new StringBuilder();
		builder.append((char)ich);
		
		ich = this.reader.read();
		
		while (ich != -1 && !(Character.isSpaceChar((char) ich) || Character.isISOControl((char) ich))) {
			builder.append((char)ich);
			ich = this.reader.read();
		}
		
		return new Token(builder.toString(), TokenType.NAME);
	}
}
