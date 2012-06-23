package com.ajlopez.ajtalk.compiler;

import java.io.*;

public class ChunkReader {
	private static final char ChunkDelimeter = '!';
	private Reader reader;
	
	public ChunkReader(String text) {
		this(new StringReader(text));
	}
	
	public ChunkReader(Reader reader) {
		this.reader = reader;
	}
	
	public String readChunk() throws IOException {
		StringWriter writer = new StringWriter();
		int ich;
		int lastich = -1;
		
		for (ich = this.reader.read(); ich != -1; ich = this.reader.read()) {
			char ch = (char)ich;
			
			if (ch == ChunkDelimeter && lastich != -1)
				break;
			
			if (lastich == -1 && (ch == '\r' || ch == '\n'))
				continue;
			
			writer.write(ich);
			lastich = ich;
		}
		
		writer.close();
		
		String result = writer.toString();
		
		if (result.equals(""))
			return null;
		
		return result;
	}
}