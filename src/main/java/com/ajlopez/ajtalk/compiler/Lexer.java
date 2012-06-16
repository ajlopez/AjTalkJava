package com.ajlopez.ajtalk.compiler;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class Lexer {
	private static final char StringDelimeter = '\'';
	private static final char CommentDelimeter = '"';
	private static final String separators = "().{}";
	private Reader reader;
	private int nextich = -1;
	
	public Lexer(String text) {
		this(new StringReader(text));
	}
	
	public Lexer(Reader reader) {
		this.reader = reader;
	}
	
	public Token nextToken() throws IOException, LexerException {
		int ich = this.nextCharToProcess();
		
		if (ich == -1)
			return null;
		
		char ch = (char) ich;		
		
		if (Character.isDigit(ch))
			return this.nextInteger(ch);
		
		if (ch == StringDelimeter)
			return this.nextString();
		
		if (separators.indexOf(ch)>=0)
			return new Token(String.valueOf(ch), TokenType.SEPARATOR);
		
		StringBuilder builder = new StringBuilder();
		builder.append(ch);
		
		ich = this.reader.read();
		
		while (ich != -1 && Character.isLetterOrDigit((char) ich)) {
			builder.append((char)ich);
			ich = this.reader.read();
		}
		
		this.pushChar(ich);
		
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
		
		this.pushChar(ich);
		
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
	
	private int nextCharToProcess() throws IOException, LexerException {
		int ich;
		
		if (nextich >= 0) {
			ich = nextich;
			nextich = -1;
		}
		else
			ich = this.reader.read();		
		
		for (; ich != -1; ich = this.reader.read()) {
			char ch = (char)ich;
			
			if (Character.isSpaceChar(ch) || Character.isISOControl(ch))
				continue;
			
			if (ch == CommentDelimeter) {
				for (ich = this.reader.read(); ich != -1 && (char)ich != CommentDelimeter; ich = this.reader.read())
					;
				
				if (ich == -1)
					throw new LexerException("Unclosed comment");
			}
			else
				return ich;
		}
		
		return ich;
	}
	
	private void pushChar(int ich) {		
		nextich = ich;
	}
}
