package com.ajlopez.ajtalk.compiler;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.ajlopez.ajtalk.language.Block;
import com.ajlopez.ajtalk.language.Bytecodes;

public class CompilerTests {
	@Test
	public void compileInteger() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("123");
		Compiler compiler = new Compiler(parser.parseExpressionNode());
		
		Block block = compiler.compileBlock();
		this.testBlock(block, 0, 0, new byte[] {Bytecodes.GETVALUE, 0}, new Object[] { 123 });
	}

	@Test
	public void compileString() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("'foo'");
		Compiler compiler = new Compiler(parser.parseExpressionNode());
		
		Block block = compiler.compileBlock();
		this.testBlock(block, 0, 0, new byte[] {Bytecodes.GETVALUE, 0}, new Object[] { "foo" });
	}

	@Test
	public void compileSymbol() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("#foo");
		Compiler compiler = new Compiler(parser.parseExpressionNode());
		
		Block block = compiler.compileBlock();
		this.testBlock(block, 0, 0, new byte[] {Bytecodes.GETVALUE, 0}, new Object[] { "foo" });
	}

	@Test
	public void compileChar() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("$x");
		Compiler compiler = new Compiler(parser.parseExpressionNode());
		
		Block block = compiler.compileBlock();
		this.testBlock(block, 0, 0, new byte[] {Bytecodes.GETVALUE, 0}, new Object[] { 'x' });
	}

	@Test
	public void compileGlobalId() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("Smalltalk");
		Compiler compiler = new Compiler(parser.parseExpressionNode());
		
		Block block = compiler.compileBlock();
		this.testBlock(block, 0, 0, new byte[] {Bytecodes.GETGLOBAL, 0}, new Object[] { "Smalltalk" });
	}
	
	@Test
	public void compileReturnString() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("^'foo'");
		Compiler compiler = new Compiler(parser.parseExpressionNode());
		
		Block block = compiler.compileBlock();
		this.testBlock(block, 0, 0, new byte[] {Bytecodes.GETVALUE, 0, Bytecodes.RETURN}, new Object[] { "foo" });
	}
	
	@Test
	public void compileUnaryMessage() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("1 inc");
		Compiler compiler = new Compiler(parser.parseExpressionNode());
		
		Block block = compiler.compileBlock();
		this.testBlock(block, 0, 0, new byte[] {Bytecodes.GETVALUE, 0, Bytecodes.SEND, 1, 0}, new Object[] { 1, "inc" });
	}
	
	@Test
	public void compileBinaryMessage() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("1+2");
		Compiler compiler = new Compiler(parser.parseExpressionNode());
		
		Block block = compiler.compileBlock();
		this.testBlock(block, 0, 0, new byte[] {Bytecodes.GETVALUE, 0, Bytecodes.GETVALUE, 1, Bytecodes.SEND, 2, 1}, new Object[] { 1, 2, "+" });
	}
	
	@Test
	public void compileKeywordMessage() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("foo do: bar with: 1");
		Compiler compiler = new Compiler(parser.parseExpressionNode());
		
		Block block = compiler.compileBlock();
		this.testBlock(block, 0, 0, new byte[] {Bytecodes.GETGLOBAL, 0, Bytecodes.GETGLOBAL, 1, Bytecodes.GETVALUE, 2, Bytecodes.SEND, 3, 2}, new Object[] { "foo", "bar", 1, "do:with:" });
	}
	
	private void testBlock(Block block, int arity, int nlocals, byte[] bytecodes, Object[] values) {
		assertNotNull(block);
		assertEquals(arity, block.getArity());
		assertEquals(nlocals, block.getNoLocals());
		assertArrayEquals(bytecodes, block.getBytecodes());
		assertArrayEquals(values, block.getValues());
	}
}
