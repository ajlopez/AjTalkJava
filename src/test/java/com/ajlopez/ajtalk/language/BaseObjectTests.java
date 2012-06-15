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

}
