package com.ujd.util;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.LoaderClassPath;

public class ByteCodeTransformer implements ClassFileTransformer {
	
	private Pattern classToDebugPattern;
	
	public ByteCodeTransformer(String classPattern){
		this.classToDebugPattern = Pattern.compile(classPattern);
	}

	public byte[] transform(ClassLoader classLoader, 
							String className, 
							Class<?> classType, 
							ProtectionDomain protectionDomain, 
							byte[] classBytes) throws IllegalClassFormatException {
		
		String normalClassName = className.replace("/", ".");
		if (shouldTransform(normalClassName)){
			return transformClass(normalClassName, classBytes);
			
		} else {
			return classBytes;
		}
	}
	
	private boolean shouldTransform(String className){
		if (className != null && !className.contains("com.ujd")){
			Matcher matcher = classToDebugPattern.matcher(className);
			return matcher.find();
		}
		return false;
	}
	
	private byte[] transformClass(String className, byte[] bytes){
		try {
			ClassPool cp = ClassPool.getDefault();
			cp.insertClassPath(new ClassClassPath(this.getClass()));
			cp.insertClassPath(new LoaderClassPath(this.getClass().getClassLoader()));
			CtClass cc = cp.get(className);
			
			int count = 0;
			for (CtConstructor constructor: cc.getDeclaredConstructors()){
				modifyMethod(className, constructor);
				count++;
			}
			for (CtMethod method: cc.getDeclaredMethods()){
				modifyMethod(className, method);
				count++;
			}
			byte[] newBytes = cc.toBytecode();
			cc.detach();
			
			System.out.println("Debugging Class: " + className + " with: " + count + " behaviors");
			return newBytes;
		
		} catch (Throwable e){}
		return bytes;
	}
	
	private void modifyMethod(String className, CtBehavior method){
		try {
			String programName = "String programName = new java.io.File("+className+".class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();";
			
			String code = "com.ujd.util.FunctionArgumentLogger.log(programName,"
				+ "Thread.currentThread().getId(),"
				+ "\""+ className +"\","
				+ "\""+ method.getName() +"\","
				+ "$args, ($w) $_);";
			
			method.insertAfter("try {" + programName + "\n" + code + "} catch (java.lang.Throwable e){}");
			
		} catch (CannotCompileException e){}
	}
}
