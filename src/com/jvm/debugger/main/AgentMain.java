package com.jvm.debugger.main;

import java.lang.instrument.Instrumentation;

import com.jvm.debugger.util.ByteCodeTransformer;
import com.jvm.debugger.util.Config;
import com.jvm.debugger.util.FunctionArgumentLogger;

public class AgentMain {
	/**
	 * \brief Agent's entry point. Will get called by the JVM before the program to debug's main method.
	 * @param agentArgs
	 * @param inst
	 */
	public static void premain(String agentArgs, Instrumentation instr){
		String configFilename = agentArgs;
		Config config = new Config(configFilename);
		
		FunctionArgumentLogger.setConfig(config);
		
		instr.addTransformer(new ByteCodeTransformer(config));
		System.out.println("starting UJD agent with config: " + agentArgs);
	}
}
