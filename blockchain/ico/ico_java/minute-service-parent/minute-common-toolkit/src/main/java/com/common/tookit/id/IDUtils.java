package com.common.tookit.id;

public class IDUtils {

	private static IdWorker worker = new IdWorker(1, 1);
	
	private IDUtils() {
		
	}
	
	public static long getId() {
		return worker.nextId();
	}

}
