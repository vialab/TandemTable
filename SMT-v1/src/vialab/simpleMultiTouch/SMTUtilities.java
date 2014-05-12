package vialab.simpleMultiTouch;

import java.lang.reflect.Method;

import processing.core.PApplet;

public final class SMTUtilities {
	
	/**
	 * Don't let anyone instantiate this class.
	 */
	private SMTUtilities() {}

	static Method getPMethod(PApplet parent, String methodName, Class<?>... parameterTypes) {
		try {
			return parent.getClass().getMethod(methodName, parameterTypes);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	
}
