package com.gizwits.lease.exceptions;

/**
 * @author lilh
 * @date 2017/8/11 15:47
 */
public class ExcelException extends Exception{

    private static final long serialVersionUID = 7582457389069996252L;

    public ExcelException() {
    }

    public ExcelException(String message) {
        super(message);
    }

    public ExcelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelException(Throwable cause) {
        super(cause);
    }

    public ExcelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
