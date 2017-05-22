package com.passwordboss.android.http.beans;

import java.io.Serializable;

public class ErrorHttpBean implements Serializable {
    private int code;
    private int id;
    private String message;

    public int getCode() {
        return code;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

}
