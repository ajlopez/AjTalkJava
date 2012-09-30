package com.ajlopez.ajtalk.compiler;

import java.io.*;

import com.ajlopez.ajtalk.ExecutionException;
import com.ajlopez.ajtalk.Machine;
import com.ajlopez.ajtalk.compiler.ast.Node;
import com.ajlopez.ajtalk.language.IBlock;
import com.ajlopez.ajtalk.language.IObject;

public class ChunkReader {
	private static final char ChunkDelimeter = '!';
	private Reader reader;
	private int lastchar = -1;
	
	public ChunkReader(String text) {
		this(new StringReader(text));
	}
	
	public ChunkReader(Reader reader) {
		this.reader = reader;
	}

	public void processChunks(Machine machine) throws ExecutionException, ParserException, IOException, LexerException, CompilerException {
		for (String chunk = this.readChunk(); chunk != null; chunk = this.readChunk()) {
			boolean toprocess = false;
			
			if (chunk.length() > 0 && chunk.charAt(0) == '!') {
				toprocess = true;
				chunk = "^" + chunk.substring(1);
			}
			
			Parser parser = new Parser(chunk);
			Node node = parser.parseExpressionNode();
			Compiler compiler = new Compiler(node);
			IBlock block = compiler.compileBlock();
			
			Object result = block.execute(null, machine);
			
			if (toprocess) {
				IObject obj = (IObject)result;
				obj.send("process:", new Object[] { this }, machine);
			}
		}
	}	
	
	public String readChunk() throws IOException {
		StringWriter writer = new StringWriter();
		int ich;
		int lastich = -1;
		
		for (ich = this.nextChar(); ich != -1; ich = this.nextChar()) {
			char ch = (char)ich;
			
			if (ch == ChunkDelimeter && lastich != -1) {
				ich = this.nextChar();
				
				if (ich == -1 || (char) ich != ChunkDelimeter) {
					this.pushChar(ich);
					break;
				}
			}
			
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
	
	private int nextChar() throws IOException {
		if (this.lastchar != -1) {
			int next = this.lastchar;
			this.lastchar = -1;
			return next;
		}
		
		return this.reader.read();
	}
	
	private void pushChar(int ich) {
		this.lastchar = ich;
	}
}
