package com.shepherdmoney.interviewproject.Exception;

/**
 * Define the all available business exception code with enum.
 * */
public enum BusinessExceptionCode {

    CARD_ALREADY_EXIST("Credit card already exists."),

    CARD_NOT_FOUND("Credit card is not found."),

    CARD_ADD_FAILED("Credit card add failed."),

    CARD_DELETE_FAILED("Credit card delete failed."),

    USER_NOT_FOUND("User is not found."),

    CREATE_USER_FAILED("Create user failed."),

    DELETE_USER_FAILED("Delete user failed."),

    USER_ALREADY_EXIST("User already exists.");

    private String desc;

    BusinessExceptionCode(String desc){
        this.desc = desc;
    }

    public String getDesc(){ return this.desc; }

    public void setDesc(String desc) { this.desc = desc; }
}
