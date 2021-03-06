package com.ajlopez.ajtalk.compiler;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.Test;

import com.ajlopez.ajtalk.ExecutionException;
import com.ajlopez.ajtalk.Machine;
import com.ajlopez.ajtalk.compiler.ast.Node;
import com.ajlopez.ajtalk.language.*;

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

	@Test
	public void readAllChunksInPointSt() throws IOException, ParserException, LexerException {
		ChunkReader reader = new ChunkReader(this.resourceAsReader("Point.st"));
		
		int nchuncks = 0;
		
		for (String chunk = reader.readChunk(); chunk != null; chunk = reader.readChunk())
			nchuncks++;
		
		assertTrue(nchuncks>50);
	}

	@Test
	public void readAllChunksInRectangleSt() throws IOException, ParserException, LexerException {
		ChunkReader reader = new ChunkReader(this.resourceAsReader("Rectangle.st"));
		
		int nchuncks = 0;
		
		for (String chunk = reader.readChunk(); chunk != null; chunk = reader.readChunk())
			nchuncks++;
		
		assertTrue(nchuncks>50);
	}

	@Test
	public void readAllChunksInFileOut01() throws IOException, ParserException, LexerException {
		ChunkReader reader = new ChunkReader(this.resourceAsReader("FileOut01.st"));
		
		int nchuncks = 0;
		
		for (String chunk = reader.readChunk(); chunk != null; chunk = reader.readChunk())
			nchuncks++;
		
		assertTrue(nchuncks>=10);
	}

	@Test
	public void executeFirstChunk() throws IOException, ParserException, LexerException, ExecutionException, CompilerException {
		ChunkReader reader = new ChunkReader(this.resourceAsReader("Point.st"));
		
		Machine machine = new Machine();
		machine.initialize();
		String chunk = reader.readChunk();
		Parser parser = new Parser(chunk);
		Node node = parser.parseExpressionNode();
		Compiler compiler = new Compiler(node);
		IBlock block = compiler.compileBlock();
		assertNull(block.execute(null, machine));
	}

	@Test
	public void executeFirstAndSecondChunk() throws IOException, ParserException, LexerException, ExecutionException, CompilerException {
		ChunkReader reader = new ChunkReader(this.resourceAsReader("Point.st"));
		
		Machine machine = new Machine();
		machine.initialize();
		
		String chunk = reader.readChunk();
		Parser parser = new Parser(chunk);
		Node node = parser.parseExpressionNode();
		Compiler compiler = new Compiler(node);
		IBlock block = compiler.compileBlock();
		
		block.execute(null, machine);

		chunk = reader.readChunk();
		parser = new Parser(chunk);
		node = parser.parseExpressionNode();
		compiler = new Compiler(node);
		block = compiler.compileBlock();
		
		block.execute(null, machine);
		
		assertNotNull(machine.getValue("Point"));
		assertTrue(machine.getValue("Point") instanceof IClass);
		
		IClass klass = (IClass)machine.getValue("Point");
		assertEquals(machine.getValue("Object"), klass.getSuperBehavior());
		assertEquals(2, klass.getObjectSize());
		assertEquals(0, klass.getVariableOffset("x"));
		assertEquals(1, klass.getVariableOffset("y"));
	}

	@Test
	public void processChunks() throws IOException, ParserException, LexerException, ExecutionException, CompilerException {
		ChunkReader reader = new ChunkReader(this.resourceAsReader("Point.st"));
		
		Machine machine = new Machine();
		machine.initialize();
		
		reader.processChunks(machine);
		
		assertNotNull(machine.getValue("Point"));
		assertTrue(machine.getValue("Point") instanceof IClass);
		
		IClass klass = (IClass)machine.getValue("Point");
		assertEquals(machine.getValue("Object"), klass.getSuperBehavior());
		assertEquals(2, klass.getObjectSize());
		assertEquals(0, klass.getVariableOffset("x"));
		assertEquals(1, klass.getVariableOffset("y"));
	}

	@Test
	public void executeFirstAndSecondChunkRectangle() throws IOException, ParserException, LexerException, ExecutionException, CompilerException {
		ChunkReader reader = new ChunkReader(this.resourceAsReader("Rectangle.st"));
		
		Machine machine = new Machine();
		machine.initialize();
		
		String chunk = reader.readChunk();
		Parser parser = new Parser(chunk);
		Node node = parser.parseExpressionNode();
		Compiler compiler = new Compiler(node);
		IBlock block = compiler.compileBlock();
		
		block.execute(null, machine);

		chunk = reader.readChunk();
		parser = new Parser(chunk);
		node = parser.parseExpressionNode();
		compiler = new Compiler(node);
		block = compiler.compileBlock();
		
		block.execute(null, machine);
		
		assertNotNull(machine.getValue("Rectangle"));
		assertTrue(machine.getValue("Rectangle") instanceof IClass);
		
		IClass klass = (IClass)machine.getValue("Rectangle");
		assertEquals(machine.getValue("Object"), klass.getSuperBehavior());
		assertEquals(2, klass.getObjectSize());
		assertEquals(0, klass.getVariableOffset("origin"));
		assertEquals(1, klass.getVariableOffset("corner"));
	}
	
	private Reader resourceAsReader(String name) throws IOException, ParserException, LexerException {
		InputStream stream = this.getClass().getResourceAsStream(name);
		return new InputStreamReader(stream);
	}
}
