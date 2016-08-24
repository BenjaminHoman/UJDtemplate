package com.ujd.main;

import java.lang.instrument.Instrumentation;

import com.ujd.sharedutils.config.Config;
import com.ujd.util.ByteCodeTransformer;
import com.ujd.util.FunctionArgumentLogger;

public class AgentMain {
	/**
	 * \brief Agent's entry point. Will get called by the JVM before the program to debug's main method.
	 * @param agentArgs
	 * @param inst
	 */
	public static void premain(String agentArgs, Instrumentation inst){
		String configFilename = agentArgs;
		Config config = new Config(configFilename);
		
		FunctionArgumentLogger.setConfig(config);
		
		inst.addTransformer(new ByteCodeTransformer(config.getDebugClassPattern()));
		System.out.println("starting UJD agent with config: " + agentArgs);
	}
}
