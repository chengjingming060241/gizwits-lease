package com.gizwits.lease.exceptions;

import com.gizwits.boot.exceptions.SystemException;

/**
 * Created by rongmc on 2017/7/15.
 */
public class LeaseException  {
    public static  void throwSystemException(LeaseExceEnums leaseExceEnums){
        throw  new SystemException(leaseExceEnums.getCode(),leaseExceEnums.getMessage());
    }

    public static  void throwSystemException(LeaseExceEnums leaseExceEnums, String appendMessage){
        throw  new SystemException(leaseExceEnums.getCode(),leaseExceEnums.getMessage() + appendMessage);
    }

    public static  void throwSystemException(String frontMessage,LeaseExceEnums leaseExceEnums){
        throw  new SystemException(leaseExceEnums.getCode(),frontMessage + leaseExceEnums.getMessage() );
    }

    public static  void throwSystemException(String frontMessage,LeaseExceEnums leaseExceEnums, String appendMessage){
        throw  new SystemException(leaseExceEnums.getCode(),frontMessage + leaseExceEnums.getMessage() + appendMessage);
    }
}
