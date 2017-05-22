package com.passwordboss.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.passwordboss.android.R;
import com.passwordboss.android.database.bll.DeviceBll;
import com.passwordboss.android.dialog.alert.InfoDialog;
import com.passwordboss.android.pin.PinStore;
import com.passwordboss.android.pin.PinStoreOld;
import com.passwordboss.android.rx.ApplySchedulers;
import com.passwordboss.android.widget.AppPinKeyboardView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpPinActivity extends BaseActivity {
    private static final String KEY_PIN = "keyPin";
    private static final Logger LOGGER = LoggerFactory.getLogger(SignUpPinActivity.class);
    @Bind(R.id.ac_supn_button_delete)
    Button mDeleteButton;
    @Bind(R.id.ac_supn_pin_01)
    TextView mPin01View;
    @Bind(R.id.ac_supn_pin_02)
    TextView mPin02View;
    @Bind(R.id.ac_supn_pin_03)
    TextView mPin03View;
    @Bind(R.id.ac_supn_pin_04)
    TextView mPin04View;
    @Bind(R.id.ac_supn_keyboard)
    AppPinKeyboardView mPinKeyboardView;
    @Bind(R.id.ac_supn_button_set_pin)
    Button mSetPinButton;
    private String mPin = "";

    public static void start(Context context) {
        Intent intent = new Intent(context, SignUpPinActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private void addNumber(char number) {
        setPin(mPin + String.valueOf(number));
    }

    @Override
    public void onBackPressed() {
        onClickButtonSkip();
    }

    @OnClick(R.id.ac_supn_button_delete)
    void onClickButtonDelete() {
        String newPin = null;
        if (mPin.length() > 0) newPin = mPin.substring(0, mPin.length() - 1);
        setPin(newPin);
    }

    @OnClick(R.id.ac_supn_button_set_pin)
    void onClickButtonSetPin() {
        DeviceBll.getObservable()
                .map(deviceBll -> {
                    PinStore pinStore = new PinStoreOld(this, deviceBll);
                    pinStore.set(mPin);
                    pinStore.enable();
                    return null;
                })
                .compose(new ApplySchedulers<>())
                .subscribe(o -> {
                    new InfoDialog(this)
                            .show(getText(R.string.EnablePINorTouchIDNotification), (dialog, which) -> MainActivity.start(SignUpPinActivity.this));
                });
    }

    @OnClick(R.id.ac_supn_button_skip)
    void onClickButtonSkip() {
        MainActivity.start(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_pin);
        ButterKnife.bind(this);
        mPinKeyboardView.setOnClickKeypadListener(this::addNumber);
        if (null != savedInstanceState) {
            setPin(savedInstanceState.getString(KEY_PIN));
        } else {
            setPin(null);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(KEY_PIN, mPin);
    }

    private void setPin(@Nullable String pin) {
        mPin = Strings.nullToEmpty(pin);
        if (mPin.length() > 4) mPin = mPin.substring(0, 4);
        mSetPinButton.setEnabled(mPin.length() == 4);
        mDeleteButton.setVisibility(mPin.length() > 0 ? View.VISIBLE : View.INVISIBLE);
        mPin01View.setText(mPin.length() >= 1 ? mPin.substring(0, 1) : null);
        mPin02View.setText(mPin.length() >= 2 ? mPin.substring(1, 2) : null);
        mPin03View.setText(mPin.length() >= 3 ? mPin.substring(2, 3) : null);
        mPin04View.setText(mPin.length() >= 4 ? mPin.substring(3, 4) : null);
    }
}
