package com.shepherdmoney.interviewproject.Exception;

/**
 * Define the all available business exception code with enum.
 * */
public enum BusinessExceptionCode {

    CARD_ALREADY_EXIST("Credit card already exists."),

    CARD_NOT_BELONG_TO_USER("Credit card does not belong to user."),

    CARD_NOT_FOUND("Credit card is not found."),

    CARD_ADD_FAILED("Credit card add failed."),

    CARD_DELETE_FAILED("Credit card delete failed."),

    GET_CREDIT_CARDS_FAILED("Get credit cards failed."),

    USER_NOT_FOUND("User is not found."),

    CREATE_USER_FAILED("Create user failed."),

    DELETE_USER_FAILED("Delete user failed."),

    USER_ALREADY_EXIST("User already exists."),

    BALANCE_HISTORY_NOT_EXIST("Balance history is not exist."),

    BALANCE_HISTORY_ADD_FAILED("Balance history add failed.");

    private String desc;

    BusinessExceptionCode(String desc){
        this.desc = desc;
    }

    public String getDesc(){ return this.desc; }

    public void setDesc(String desc) { this.desc = desc; }
}
