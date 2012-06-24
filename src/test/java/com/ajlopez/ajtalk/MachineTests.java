package com.ajlopez.ajtalk;
import static org.junit.Assert.*;

import org.junit.Test;

import com.ajlopez.ajtalk.language.IBehavior;
import com.ajlopez.ajtalk.language.IClass;
import com.ajlopez.ajtalk.language.IObject;

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

	@Test
	public void initializeHasProtoObjectClass() {
		Machine machine = new Machine();
		machine.initialize();
		
		assertNotNull(machine.getValue("ProtoObject"));
		assertTrue(machine.getValue("ProtoObject") instanceof IBehavior);
	}

	@Test
	public void initializeHasObjectClass() {
		Machine machine = new Machine();
		machine.initialize();
		
		assertNotNull(machine.getValue("Object"));
		assertTrue(machine.getValue("Object") instanceof IBehavior);
		
		IBehavior klass = (IBehavior)machine.getValue("Object");
		assertEquals(machine.getValue("ProtoObject"), klass.getSuperBehavior());
	}

	@Test
	public void initializeProtoObjectSubclass() throws ExecutionException {
		Machine machine = new Machine();
		machine.initialize();
		
		IObject protoObject = (IObject)machine.getValue("ProtoObject");
		Object result = protoObject.send("subclass:", new Object[] { "Point" }, machine);
		
		assertNotNull(result);
		assertTrue(result instanceof IClass);
		assertEquals(machine.getValue("Point"), result);		
		
		IClass klass = (IClass)result;
		
		assertEquals(protoObject, klass.getSuperBehavior());
	}
}
