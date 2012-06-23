package com.ajlopez.ajtalk.compiler;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.ajlopez.ajtalk.language.Block;
import com.ajlopez.ajtalk.language.Bytecodes;

public class CompilerTests {
	@Test
	public void compileIntegerNode() throws ParserException, IOException, LexerException {
		Parser parser = new Parser("123");
		Compiler compiler = new Compiler(parser.parseExpressionNode());
		
		Block block = compiler.compileBlock();
		
		assertNotNull(block);
		assertEquals(0, block.getArity());
		assertEquals(0, block.getNoLocals());
		
		byte[] bytecodes = block.getBytecodes();
		
		assertNotNull(bytecodes);
		assertEquals(2, bytecodes.length);
		assertEquals(Bytecodes.GETVALUE, bytecodes[0]);
		assertEquals(0, bytecodes[1]);
		
		Object[] values = block.getValues();
		
		assertNotNull(values);
		assertEquals(1, values.length);
		assertEquals(123, values[0]);
	}
}
