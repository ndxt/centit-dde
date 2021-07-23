package com.centit.dde.exception;

public class CodeRuntimeException extends RuntimeException {

	public CodeRuntimeException(String string) {
		super(string);
	}

	public CodeRuntimeException(Exception e){
		super(e);
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

}
