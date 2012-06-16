package com.ajlopez.ajtalk.language;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExecutionBlockTests {

	@Test
	public void executeSimpleReturn() {
		byte[] bytecodes = new byte[] { Bytecodes.LOADARG, 0, Bytecodes.RETURN };
		Method method = new Method(1, 0, bytecodes );
		assertEquals(1, method.execute(null, new Object[] { 1 }));
	}
}
