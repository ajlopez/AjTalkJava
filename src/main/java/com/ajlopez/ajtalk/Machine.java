package com.ajlopez.ajtalk;

import java.io.IOException;
import java.util.*;

import com.ajlopez.ajtalk.compiler.Compiler;
import com.ajlopez.ajtalk.compiler.ChunkReader;
import com.ajlopez.ajtalk.compiler.Parser;
import com.ajlopez.ajtalk.compiler.ast.MethodNode;
import com.ajlopez.ajtalk.language.*;

public class Machine {
	private Map<String,Object> values = new HashMap<String,Object>();
	
	public void initialize()
	{
		IClass metaclass = new BaseClass(null, null);

		IMethod subclass = new ClassSubclassMethod();
		metaclass.defineMethod("subclass:", subclass);
		metaclass.defineMethod("subclass:instanceVariableNames:", subclass);
		metaclass.defineMethod("subclass:instanceVariableNames:classVariableNames:", subclass);
		metaclass.defineMethod("subclass:instanceVariableNames:classVariableNames:poolDictionaries:category:", subclass);
		
		IMethod comment = new ClassCommentMethod();
		metaclass.defineMethod("commentStamp:", comment);
		metaclass.defineMethod("commentStamp:prior:", comment);
		
		IMethod methodsFor = new ClassMethodsForMethod();
		metaclass.defineMethod("methodsFor:", methodsFor);
		metaclass.defineMethod("methodsFor:stamp:", methodsFor);
		
		IClass classProtoObject = new BaseClass(metaclass, null);
		this.setValue("ProtoObject", classProtoObject);
		
		IClass classObject = new BaseClass(metaclass, classProtoObject);
		this.setValue("Object", classObject);
	}
	
	public void setValue(String name, Object value) {
		values.put(name, value);
	}
	
	public Object getValue(String name) {
		return values.get(name);
	}
}

class ClassSubclassMethod implements IMethod {

	@Override
	public Object execute(Object self, Object[] arguments, Machine machine) throws ExecutionException {
		IBehavior behavior = (IBehavior) self;
		String[] instanceVariableNames = null;
		if (arguments.length > 1)
			instanceVariableNames = ((String)arguments[1]).split(" ");
		
		// TODO Review metaclass
		Object result = new BaseClass(((IObject)behavior).getBehavior(), behavior, instanceVariableNames);
		machine.setValue((String)arguments[0], result);
		return result;
	}	
}

class ClassCommentMethod implements IMethod {

	@Override
	public Object execute(Object self, Object[] arguments, Machine machine) throws ExecutionException {
		return new CommentProcessor(null);
	}	
}

class ClassMethodsForMethod implements IMethod {
	@Override
	public Object execute(Object self, Object[] arguments, Machine machine) throws ExecutionException {
		return new MethodProcessor(null, (IBehavior)self);
	}	
}

class CommentProcessor extends BaseObject {
	public CommentProcessor(IBehavior behavior) {
		super(behavior);
	}	
	
	@Override
	public Object send(String selector, Object[]arguments, Machine machine) throws ExecutionException {
		if (!selector.equals("process:"))
			throw new ExecutionException("Invalid Method");
		
		ChunkReader reader = (ChunkReader)arguments[0];
		try {
			reader.readChunk();
		} catch (IOException e) {
			throw new ExecutionException(e);
		}
		return null;
	}
}

class MethodProcessor extends BaseObject {
	private IBehavior klass;
	
	public MethodProcessor(IBehavior behavior, IBehavior klass) {
		super(behavior);
		this.klass = klass;
	}	
	
	@Override
	public Object send(String selector, Object[]arguments, Machine machine) throws ExecutionException {
		if (!selector.equals("process:"))
			throw new ExecutionException("Invalid Method");
		
		ChunkReader reader = (ChunkReader)arguments[0];
		try {
			for (String chunk = reader.readChunk(); chunk != null && !chunk.equals(" "); chunk = reader.readChunk()) {
				Parser parser = new Parser(chunk);
				MethodNode node; 
				node = parser.parseMethodNode();
				Compiler compiler = new Compiler(node);
				IMethod method = compiler.compileMethod();
				this.klass.defineMethod(node.getSelector(), method);
			}
		} catch (Exception e) {
			throw new ExecutionException(e);
		}
		return null;
	}
}