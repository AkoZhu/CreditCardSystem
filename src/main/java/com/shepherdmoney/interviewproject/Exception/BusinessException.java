package com.shepherdmoney.interviewproject.Exception;

public class BusinessException extends RuntimeException{

    private BusinessExceptionCode code;

    public BusinessException(BusinessExceptionCode code){
        // Set the desc as RuntimeException message.
        super(code.getDesc());
        this.code = code;
    }

    public BusinessExceptionCode getCode() { return code;}

    public void setCode(BusinessExceptionCode code) { this.code = code; }

}
