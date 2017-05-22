package com.passwordboss.android.pin;

public interface PinStore {

    boolean isValid(String pin);

    void set(String pin);

    void clear();

    void enable();

    void disable();

    boolean isEnabled();
}
