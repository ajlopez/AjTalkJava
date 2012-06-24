package com.ajlopez.ajtalk.language;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ajlopez.ajtalk.ExecutionException;
import com.ajlopez.ajtalk.Machine;

public class ExecutionBlockTests {
	@Test
	public void executeMethodWithSimpleReturn() throws ExecutionException {
		byte[] bytecodes = new byte[] { Bytecodes.GETARGUMENT, 0, Bytecodes.RETURN };
		Method method = new Method(1, 0, bytecodes );
		assertEquals(1, method.execute(null, new Object[] { 1 }, null));
	}

	@Test
	public void executeBlockWithSimpleReturn() throws ExecutionException {
		byte[] bytecodes = new byte[] { Bytecodes.GETARGUMENT, 0, Bytecodes.RETURN };
		Block block = new Block(1, 0, bytecodes );
		assertEquals(1, block.execute(new Object[] { 1 }, null));
	}

	@Test
	public void executeSetAndGetLocalWithSimpleReturn() throws ExecutionException {
		byte[] bytecodes = new byte[] { Bytecodes.GETARGUMENT, 0, Bytecodes.SETLOCAL, 0, Bytecodes.GETLOCAL, 0, Bytecodes.RETURN };
		Block block = new Block(1, 1, bytecodes );
		assertEquals(1, block.execute(new Object[] { 1 }, null));
	}

	@Test
	public void executeAddIntegers() throws ExecutionException {
		byte[] bytecodes = new byte[] { Bytecodes.GETARGUMENT, 0, Bytecodes.GETARGUMENT, 1, Bytecodes.ADD, Bytecodes.RETURN };
		Block block = new Block(1, 1, bytecodes );
		assertEquals(3, block.execute(new Object[] { 1, 2 }, null));
	}

	@Test
	public void executeGetValue() throws ExecutionException {
		byte[] bytecodes = new byte[] { Bytecodes.GETVALUE, 0, Bytecodes.RETURN };
		Object[] values = new Object[] { 1 };
		Block block = new Block(1, 1, bytecodes, values );
		assertEquals(1, block.execute(null, null));
	}

	@Test
	public void executeSetVariable() throws ExecutionException {
		BaseClass behavior = new BaseClass(null, null, new String[] { "x", "y"} );
		BaseObject object = new BaseObject(behavior);
		
		byte[] bytecodes = new byte[] { Bytecodes.GETARGUMENT, 0, Bytecodes.SETVARIABLE, 0 };
		Method method = new Method(1, 1, bytecodes );
		assertEquals(object, method.execute(object, new Object[] { 10 }, null));
		assertEquals(10, object.getVariable(0));
		assertNull(object.getVariable(1));
	}

	@Test
	public void executeSetVariables() throws ExecutionException {
		BaseClass behavior = new BaseClass(null, null, new String[] { "x", "y"} );
		BaseObject object = new BaseObject(behavior);
		
		byte[] bytecodes = new byte[] { Bytecodes.GETARGUMENT, 0, Bytecodes.SETVARIABLE, 0, Bytecodes.GETARGUMENT, 1, Bytecodes.SETVARIABLE, 1 };
		Method method = new Method(1, 1, bytecodes );
		assertEquals(object, method.execute(object, new Object[] { 10, 20 }, null));
		assertEquals(10, object.getVariable(0));
		assertEquals(20, object.getVariable(1));
	}

	@Test
	public void executeGetGlobalVariable() throws ExecutionException {
		Machine machine = new Machine();
		machine.setValue("one", 1);
		
		byte[] bytecodes = new byte[] { Bytecodes.GETGLOBAL, 0, Bytecodes.RETURN };
		Block block = new Block(0, 0, bytecodes, new Object[] { "one" } );
		assertEquals(1, block.execute(null, machine));
	}

	@Test
	public void executeSetGlobalVariable() throws ExecutionException {
		Machine machine = new Machine();
		
		byte[] bytecodes = new byte[] { Bytecodes.GETVALUE, 0, Bytecodes.SETGLOBAL, 1 };
		Block block = new Block(0, 0, bytecodes, new Object[] { 1,  "one" } );
		block.execute(null, machine);
		
		assertEquals(1, machine.getValue("one"));
	}
}
