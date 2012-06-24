package com.ajlopez.ajtalk.language;

import static org.junit.Assert.*;

import org.junit.Test;

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

	private class MyMethod implements IMethod {

		@Override
		public Object execute(Object self, Object[] arguments, Machine machine) {
			// TODO Auto-generated method stub
			return null;
		}
	
	}
}
