package com.jvm.debugger.util;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;
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
	
	private Config config;
	
	public ByteCodeTransformer(Config config){
		this.config = config;
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
		if (className != null && !className.contains("com.jvm.debugger.")){
			for (Pattern pattern: this.config.getDebugClassPatterns()){
				if (pattern.matcher(className).find()){
					return true;
				}
			}
		}
		return false;
	}
	
	private synchronized byte[] transformClass(String className, byte[] bytes){
		try {
			ClassPool cp = ClassPool.getDefault();
			
			cp.insertClassPath(new ClassClassPath(this.getClass()));
			cp.insertClassPath(new LoaderClassPath(this.getClass().getClassLoader()));
			for (String path: this.config.getExtraClassPaths()){
				cp.insertClassPath(path);
			}
			
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
			
			if (this.config.shouldPrintDebugStatus()){
				System.out.println("Debugging Class: " + className + " with: " + count + " behaviors");
			}
			
			return newBytes;
		
		} catch (Throwable e){}
		return bytes;
	}
	
	private void modifyMethod(String className, CtBehavior method){
		try {
			String code = "com.jvm.debugger.util.FunctionArgumentLogger.log(programName,"
				+ "Thread.currentThread().getId(),"
				+ "\""+ className +"\","
				+ "\""+ method.getName() +"\","
				+ "$args, ($w) $_);";
			
			method.insertAfter("try {" + code + "} catch (java.lang.Throwable e){}");
			
		} catch (CannotCompileException e){}
	}
}
