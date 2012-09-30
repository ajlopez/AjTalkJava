package com.ajlopez.ajtalk;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.ajlopez.ajtalk.compiler.ChunkReader;
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

	@Test
	public void initializeProtoObjectSubclassWithInstanceVariables() throws ExecutionException {
		Machine machine = new Machine();
		machine.initialize();
		
		IObject protoObject = (IObject)machine.getValue("ProtoObject");
		Object result = protoObject.send("subclass:instanceVariableNames:classVariableNames:", new Object[] { "Point", "x y", "" }, machine);
		
		assertNotNull(result);
		assertTrue(result instanceof IClass);
		assertEquals(machine.getValue("Point"), result);		
		
		IClass klass = (IClass)result;
				
		assertEquals(protoObject, klass.getSuperBehavior());
		assertEquals(2, klass.getObjectSize());
		assertEquals(0, klass.getVariableOffset("x"));
		assertEquals(1, klass.getVariableOffset("y"));
	}

	@Test
	public void initializeProtoObjectCommentStamp() throws ExecutionException {
		Machine machine = new Machine();
		machine.initialize();
		
		IObject protoObject = (IObject)machine.getValue("ProtoObject");
		Object result = protoObject.send("commentStamp:", new Object[] { "comment" }, machine);
		
		assertNotNull(result);
		assertTrue(result instanceof IObject);
		assertTrue(result instanceof CommentProcessor);
	}

	@Test
	public void commentProcessorProcessComment() throws ExecutionException, IOException {
		ChunkReader reader = new ChunkReader("comment!");
		CommentProcessor processor = new CommentProcessor(null);
		processor.send("process:", new Object[] { reader }, null);
		assertNull(reader.readChunk());
	}

	@Test
	public void methodProcessorProcessMethods() throws ExecutionException, IOException {
		ChunkReader reader = new ChunkReader("one ^1! zero ^0! !");
		Machine machine = new Machine();
		IClass klass = (IClass)machine.getValue("Object");
		MethodProcessor processor = new MethodProcessor(null, klass);
		processor.send("process:", new Object[] { reader }, null);
		assertNotNull(klass.getMethod("one"));
		assertNotNull(klass.getMethod("zero"));
		assertNull(reader.readChunk());
	}
	
	@Test
	public void initializeProtoObjectCommentStampPrior() throws ExecutionException {
		Machine machine = new Machine();
		machine.initialize();
		
		IObject protoObject = (IObject)machine.getValue("ProtoObject");
		Object result = protoObject.send("commentStamp:prior:", new Object[] { "comment", 0 }, machine);
		
		assertNotNull(result);
		assertTrue(result instanceof IObject);
		assertTrue(result instanceof CommentProcessor);
	}

	@Test
	public void initializeProtoObjectMethodsFor() throws ExecutionException {
		Machine machine = new Machine();
		machine.initialize();
		
		IObject protoObject = (IObject)machine.getValue("ProtoObject");
		Object result = protoObject.send("methodsFor:", new Object[] { "category" }, machine);
		
		assertNotNull(result);
		assertTrue(result instanceof IObject);
		assertTrue(result instanceof MethodProcessor);
	}

	@Test
	public void initializeProtoObjectMethodsForStamp() throws ExecutionException {
		Machine machine = new Machine();
		machine.initialize();
		
		IObject protoObject = (IObject)machine.getValue("ProtoObject");
		Object result = protoObject.send("methodsFor:stamp:", new Object[] { "category" }, machine);
		
		assertNotNull(result);
		assertTrue(result instanceof IObject);
		assertTrue(result instanceof MethodProcessor);
	}
}
