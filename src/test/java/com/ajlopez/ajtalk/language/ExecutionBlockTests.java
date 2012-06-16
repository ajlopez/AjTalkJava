package com.ajlopez.ajtalk.language;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExecutionBlockTests {
	@Test
	public void executeMethodWithSimpleReturn() {
		byte[] bytecodes = new byte[] { Bytecodes.GETARG, 0, Bytecodes.RETURN };
		Method method = new Method(1, 0, bytecodes );
		assertEquals(1, method.execute(null, new Object[] { 1 }));
	}

	@Test
	public void executeBlockWithSimpleReturn() {
		byte[] bytecodes = new byte[] { Bytecodes.GETARG, 0, Bytecodes.RETURN };
		Block block = new Block(1, 0, bytecodes );
		assertEquals(1, block.execute(new Object[] { 1 }));
	}

	@Test
	public void executeSetAndGetLocalWithSimpleReturn() {
		byte[] bytecodes = new byte[] { Bytecodes.GETARG, 0, Bytecodes.SETLOCAL, 0, Bytecodes.GETLOCAL, 0, Bytecodes.RETURN };
		Block block = new Block(1, 1, bytecodes );
		assertEquals(1, block.execute(new Object[] { 1 }));
	}
}
