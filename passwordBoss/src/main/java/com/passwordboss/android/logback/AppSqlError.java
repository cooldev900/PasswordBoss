package com.passwordboss.android.logback;


public class AppSqlError extends AppError {
    public AppSqlError(Exception exception) {
        super("SQL error", exception);
    }
}
