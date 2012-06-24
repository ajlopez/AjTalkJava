package com.ajlopez.ajtalk;
import static org.junit.Assert.*;

import org.junit.Test;

public class MachineTests {
	@Test
	public void getUndefinedValue() {
		Machine machine = new Machine();
		
		assertNull(machine.getValue("foo"));
	}

	@Test
	public void setAndGetValue() {
		Machine machine = new Machine();
		
		machine.setValue("foo", "bar");
		assertEquals("bar", machine.getValue("foo"));
	}
}
