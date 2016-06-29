package com.centit.dde.ws;

public class WsDataException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 7867444570988554051L;

    private int errorcode;

    public WsDataException() {
        super();
    }

    public WsDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public WsDataException(int errorcode, String message, Throwable cause) {
        super(message, cause);
        this.errorcode = errorcode;
    }

    public WsDataException(int errorcode, Throwable cause) {
        this(errorcode, null, cause);
    }

    public WsDataException(String message) {
        super(message);
    }

    public WsDataException(Throwable cause) {
        super(cause);
    }

    public int getErrorcode() {
        return errorcode;
    }

}
