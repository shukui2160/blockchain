package com.minute.application.app.config;

public class RequestContext {

	private static ThreadLocal<TlabsToken> requestContext = new ThreadLocal<>();

	public static void set(TlabsToken token) {
		requestContext.set(token);
	}

	public static TlabsToken get() {
		return requestContext.get();
	}

	public static void remove() {
		requestContext.remove();
	}

}
