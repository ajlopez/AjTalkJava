package com.ajlopez.ajtalk.language;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseClassTests {

	@Test
	public void defineMethod() {
		BaseClass klass = new BaseClass(null);
		IMethod method = new MyMethod();
		klass.defineMethod("mymethod", method);
		assertEquals(method, klass.getMethod("mymethod"));
	}

	private class MyMethod implements IMethod {

		@Override
		public Object execute(Object self, Object[] arguments) {
			// TODO Auto-generated method stub
			return null;
		}
	
	}
}
