package com.centit.dde.exception;

/**
 * Sql解析异常
 *
 * @author sx
 * @create 2014-6-27
 */
public class SqlResolveException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 3262161604965777589L;

    private int errorcode;

    public SqlResolveException(int errorcode) {
        super();
        this.errorcode = errorcode;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public SqlResolveException(int errorcode, Throwable cause) {
        super(cause);
        this.errorcode = errorcode;
    }

    public SqlResolveException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlResolveException(String message) {
        super(message);

    }

}
