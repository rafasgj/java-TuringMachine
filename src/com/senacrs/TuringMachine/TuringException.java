package com.senacrs.TuringMachine;

public class TuringException extends Exception {
	private static final long serialVersionUID = 5970858406411171237L;
	public TuringException(String error, String program, int pc) {
		super('"' + program + "\" @" + (pc+1) + ": '" + program.charAt(pc) + "': " + error);
	}
	public TuringException(String error, String program) {
		super('"' + program + ": " + error);
	}
}
