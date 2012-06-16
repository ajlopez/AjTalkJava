package com.ajlopez.ajtalk.language;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseObjectTests {

	@Test
	public void hasBehavior() {
		BaseClass behavior = new BaseClass(null);
		BaseObject object = new BaseObject(behavior);
		assertEquals(behavior, object.getBehavior());
	}

	@Test
	public void getSetVariable() {
		BaseClass behavior = new BaseClass(null, new String[] { "x", "y"} );
		BaseObject object = new BaseObject(behavior);
		
		assertNull(object.getVariable(0));
		assertNull(object.getVariable(1));
		
		object.setVariable(0, 10);
		object.setVariable(1, 20);
		
		assertEquals(10, object.getVariable(0));
		assertEquals(20, object.getVariable(1));
	}
}
