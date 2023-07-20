package com.zakiis.rpc.protocol;

public class IncompatibleVersionException extends Exception {

	private static final long serialVersionUID = 6540948145190528360L;

	/**
     * Instantiates a new Incompatible version exception.
     *
     * @param message the message
     */
    public IncompatibleVersionException(String message) {
        super(message);
    }
}
