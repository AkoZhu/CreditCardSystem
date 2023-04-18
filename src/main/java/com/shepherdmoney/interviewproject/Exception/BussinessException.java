package com.shepherdmoney.interviewproject.Exception;

public class BussinessException extends RuntimeException{

    private BusinessExceptionCode code;

    public BussinessException (BusinessExceptionCode code){
        // Set the desc as RuntimeException message.
        super(code.getDesc());
        this.code = code;
    }

    public BusinessExceptionCode getCode() { return code;}

    public void setCode(BusinessExceptionCode code) { this.code = code; }

}
