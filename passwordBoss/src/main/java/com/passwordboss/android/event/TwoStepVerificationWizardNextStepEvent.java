package com.passwordboss.android.event;

import android.support.annotation.Nullable;

import java.io.Serializable;

public class TwoStepVerificationWizardNextStepEvent extends BaseEvent {
    private final Step mStep;
    private Data mData;

    public TwoStepVerificationWizardNextStepEvent(Step step, Data data) {
        mStep = step;
        mData = data;
    }

    public TwoStepVerificationWizardNextStepEvent(Step step) {
        this(step, null);
    }

    @Nullable
    public Data getData() {
        return mData;
    }

    public Step getStep() {
        return mStep;
    }

    public enum Step {
        AuthenticationApp,
        BackupPhone,
        BackupSecurityCode,
        Close,
        Done,
        MasterPassword,
        SecurityCode,
        Start
    }

    public static class Data implements Serializable {
        private String mOneTimeCode;

        @Nullable
        public String getOneTimeCode() {
            return mOneTimeCode;
        }

        public void setOneTimeCode(String oneTimeCode) {
            mOneTimeCode = oneTimeCode;
        }
    }

}
