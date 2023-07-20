package com.zakiis.rpc.exception;

public class NotSupportYetException extends RuntimeException {

	private static final long serialVersionUID = 2927374734010268359L;

	/**
     * Instantiates a new Not support yet exception.
     */
    public NotSupportYetException() {
        this("currently not supported, may be supported in future");
    }

    /**
     * Instantiates a new Not support yet exception.
     *
     * @param message the message
     */
    public NotSupportYetException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Not support yet exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public NotSupportYetException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Not support yet exception.
     *
     * @param cause the cause
     */
    public NotSupportYetException(Throwable cause) {
        super(cause);
    }
}
