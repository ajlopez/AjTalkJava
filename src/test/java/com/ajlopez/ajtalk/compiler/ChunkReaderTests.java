package com.ajlopez.ajtalk.compiler;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.Test;

public class ChunkReaderTests {
	@Test
	public void readChunk() throws IOException, ParserException, LexerException {
		ChunkReader reader = new ChunkReader(this.resourceAsReader("FileOut01.st"));
		String chunk = reader.readChunk();
		assertNotNull(chunk);
		assertEquals('\'', chunk.charAt(0));
	}

	@Test
	public void readChunkWithInitialBang() throws IOException, ParserException, LexerException {
		ChunkReader reader = new ChunkReader("!\"Comment\"");
		String chunk = reader.readChunk();
		assertNotNull(chunk);
		assertEquals('!', chunk.charAt(0));
	}

	@Test
	public void readChunkWithInnerBang() throws IOException, ParserException, LexerException {
		ChunkReader reader = new ChunkReader("This is a !! bang!");
		String chunk = reader.readChunk();
		assertNotNull(chunk);
		assertEquals("This is a ! bang", chunk);
	}

	@Test
	public void readSecondChunkWithInitialBang() throws IOException, ParserException, LexerException {
		ChunkReader reader = new ChunkReader("\"A comment\"!\r\n!\"Comment\"");
		String chunk = reader.readChunk();
		assertNotNull(chunk);
		chunk = reader.readChunk();
		assertNotNull(chunk);
		assertEquals('!', chunk.charAt(0));
	}

	@Test
	public void readChunks() throws IOException, ParserException, LexerException {
		ChunkReader reader = new ChunkReader(this.resourceAsReader("FileOut01.st"));
		
		String chunk = reader.readChunk();
		assertNotNull(chunk);

		chunk = reader.readChunk();
		assertNotNull(chunk);

		chunk = reader.readChunk();
		assertNotNull(chunk);
		assertEquals('!', chunk.charAt(0));

		chunk = reader.readChunk();
		assertNotNull(chunk);

		chunk = reader.readChunk();
		assertNotNull(chunk);
		assertEquals('!', chunk.charAt(0));

		chunk = reader.readChunk();
		assertNotNull(chunk);

		chunk = reader.readChunk();
		assertNotNull(chunk);
		assertEquals(' ', chunk.charAt(0));
	}
	
	private Reader resourceAsReader(String name) throws IOException, ParserException, LexerException {
		InputStream stream = this.getClass().getResourceAsStream(name);
		return new InputStreamReader(stream);
	}
}
