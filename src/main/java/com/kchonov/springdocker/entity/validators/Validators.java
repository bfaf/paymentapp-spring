package com.kchonov.springdocker.entity.validators;

public class Validators {
    public static final String EMAIL = "[a-zA-Z0-9._]+@[a-zA-Z0-9.]+\\.[a-z]+";
    public static final String PHONE_NUMBER = "^[0-9]+$";
    public static final String MERCHANT_STATUS = "^active$|^inactive$";
    public static final String TRANSACTION_STATUS = "^approved|^reversed|^refunded|^error|";
}
