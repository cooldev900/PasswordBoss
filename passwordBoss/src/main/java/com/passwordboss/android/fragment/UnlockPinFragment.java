package com.passwordboss.android.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Strings;
import com.passwordboss.android.R;
import com.passwordboss.android.activity.UnlockActivity;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.Device;
import com.passwordboss.android.database.bll.DeviceBll;
import com.passwordboss.android.utils.Pref;
import com.passwordboss.android.widget.AppPinKeyboardView;

import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UnlockPinFragment extends BaseFragment {
    public static final String KEY_ATTEMPT = "keyAttempt";
    private static final String KEY_PIN = "keyPin";
    private static final Logger LOGGER = LoggerFactory.getLogger(UnlockPinFragment.class);
    @Bind(R.id.fr_unpn_button_delete)
    Button mDeleteButton;
    @Bind(R.id.fr_unpn_label_enter_pin)
    TextView mEnterPinView;
    @Bind(R.id.fr_unpn_label_error)
    TextView mErrorView;
    @Bind(R.id.fr_unpn_keyboard)
    AppPinKeyboardView mPinKeyboardView;
    @Bind(R.id.fr_unpn_symbol_01)
    CheckedTextView mSymbol01View;
    @Bind(R.id.fr_unpn_symbol_02)
    CheckedTextView mSymbol02View;
    @Bind(R.id.fr_unpn_symbol_03)
    CheckedTextView mSymbol03View;
    @Bind(R.id.fr_unpn_symbol_04)
    CheckedTextView mSymbol04View;
    private int mAttempt;
    private String mPin = "";

    private void addNumber(char number) {
        setPin(mPin + String.valueOf(number));
    }

    private void checkPin() {
        mAttempt++;
        try {
            DeviceBll deviceBll = new DeviceBll(DatabaseHelperSecure.getHelper(getContext(), Pref.DATABASE_KEY));
            Device device = deviceBll.getDeviceByInstallationUuid(Pref.INSTALLATION_UUID);
            int pin = null == device ? -1 : device.getPinNumber();
            if (pin == Integer.parseInt(mPin)) {
                EventBus.getDefault().post(new UnlockActivity.UnlockedEvent());
                return;
            }
            setPin(null);
            mErrorView.setVisibility(View.VISIBLE);
            mEnterPinView.setVisibility(View.INVISIBLE);

        } catch (SQLException e) {
            LOGGER.error("SQL: create DeviceBll", e);
        }
        if (mAttempt >= 3) {
            Toast.makeText(getContext(), R.string.PinLoginFailedMessage, Toast.LENGTH_LONG).show();
            EventBus.getDefault().post(new UnlockActivity.LogoutEvent());
        }
    }


    @OnClick(R.id.fr_unpn_button_cancel)
    void onClickButtonCancel() {
        EventBus.getDefault().post(new UnlockActivity.LogoutEvent());
    }

    @OnClick(R.id.fr_unpn_button_delete)
    void onClickButtonDelete() {
        String newPin = null;
        if (mPin.length() > 0) newPin = mPin.substring(0, mPin.length() - 1);
        setPin(newPin);
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_unlock_pin, null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_PIN, mPin);
        outState.putInt(KEY_ATTEMPT, mAttempt);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mPinKeyboardView.setOnClickKeypadListener(this::addNumber);
        if (null != savedInstanceState) {
            setPin(savedInstanceState.getString(KEY_PIN));
            mAttempt = savedInstanceState.getInt(KEY_ATTEMPT);
        } else {
            setPin(null);
        }
    }

    private void setPin(@Nullable String pin) {
        mPin = Strings.nullToEmpty(pin);
        if (mPin.length() > 4) mPin = mPin.substring(0, 4);
        mDeleteButton.setVisibility(mPin.length() > 0 ? View.VISIBLE : View.INVISIBLE);
        mEnterPinView.setVisibility(View.VISIBLE);
        mErrorView.setVisibility(View.INVISIBLE);
        mSymbol01View.setChecked(mPin.length() >= 1);
        mSymbol02View.setChecked(mPin.length() >= 2);
        mSymbol03View.setChecked(mPin.length() >= 3);
        mSymbol04View.setChecked(mPin.length() >= 4);
        if (mPin.length() >= 4) checkPin();
    }
}
