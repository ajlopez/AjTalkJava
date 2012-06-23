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
	
	private void testBlock(Block block, int arity, int nlocals, byte[] bytecodes, Object[] values) {
		assertNotNull(block);
		assertEquals(arity, block.getArity());
		assertEquals(nlocals, block.getNoLocals());
		assertArrayEquals(bytecodes, block.getBytecodes());
		assertArrayEquals(values, block.getValues());
	}
}
