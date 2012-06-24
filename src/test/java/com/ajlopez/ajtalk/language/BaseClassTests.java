package com.ajlopez.ajtalk.language;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ajlopez.ajtalk.ExecutionException;
import com.ajlopez.ajtalk.Machine;

public class BaseClassTests {

	@Test
	public void createClass() {
		BaseClass klass = new BaseClass(null);
		assertEquals(0, klass.getObjectSize());
		assertEquals(-1, klass.getVariableOffset("a"));
	}

	@Test
	public void createClassWithInstanceVariables() {
		BaseClass klass = new BaseClass(null, new String[] { "x", "y" });
		assertEquals(2, klass.getObjectSize());
		assertEquals(-1, klass.getVariableOffset("z"));
		assertEquals(0, klass.getVariableOffset("x"));
		assertEquals(1, klass.getVariableOffset("y"));
	}

	@Test
	public void createSubclassWithInstanceVariables() {
		BaseClass superklass = new BaseClass(null, new String[] { "x", "y" });
		BaseClass klass = new BaseClass(superklass, new String[] { "z" });
		assertEquals(3, klass.getObjectSize());
		assertEquals(0, klass.getVariableOffset("x"));
		assertEquals(1, klass.getVariableOffset("y"));
		assertEquals(2, klass.getVariableOffset("z"));
	}

	@Test
	public void defineMethod() {
		BaseClass klass = new BaseClass(null);
		IMethod method = new MyMethod();
		klass.defineMethod("mymethod", method);
		assertEquals(method, klass.getMethod("mymethod"));
	}

	@Test
	public void createInstance() {
		BaseClass klass = new BaseClass(null, new String[] { "x", "y" });
		IObject object = klass.createInstance();
		
		assertNotNull(object);
		assertNull(object.getVariable(0));
		assertNull(object.getVariable(1));
		assertEquals(klass, object.getBehavior());
	}

	@Test
	public void invokeInstanceMethod() throws ExecutionException {
		BaseClass klass = new BaseClass(null, new String[] { "x", "y" });
		IMethod method = new MyMethod();
		klass.defineMethod("mymethod", method);
		IObject object = klass.createInstance();
		
		assertEquals(1, object.send("mymethod", new Object[] { 1 }, null));
	}

	private class MyMethod implements IMethod {
		@Override
		public Object execute(Object self, Object[] arguments, Machine machine) {
			return arguments[0];
		}	
	}
}
