package com.passwordboss.android.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Strings;
import com.passwordboss.android.R;
import com.passwordboss.android.app.ResetApp;
import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.bll.ConfigurationBll;
import com.passwordboss.android.database.bll.DeviceBll;
import com.passwordboss.android.event.PinChangeResultEvent;
import com.passwordboss.android.event.PinCheckResultEvent;
import com.passwordboss.android.pin.PinStore;
import com.passwordboss.android.pin.PinStoreOld;
import com.passwordboss.android.rx.BaseObserver;
import com.passwordboss.android.toolbar.AppToolbar;
import com.passwordboss.android.widget.AppPinKeyboardView;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChangePinFragment extends ToolbarFragment implements View.OnClickListener, BackPressConsumer {

    public static final String ARG_CURRENT_STEP = "arg_current_step";
    public static final String ARG_SHOULD_NOTIFY_ABOUT_RESULT = "arg_should_notify_about_result";

    public static final int STEP_INPUT_OLD_PIN = 1;
    public static final int STEP_INPUT_NEW_PIN = 2;
    public static final int STEP_CONFIRM_NEW_PIN = 3;
    public static final int STEP_CHECK_PIN = 4;
    private static final int DELAY_MILLIS = 750;
    private int mCurrentStep;
    private int mIncorrectAttempts = 0;
    private String mNewPin = "";
    private String mPin = "";
    private PinStore mPinStore;
    private ViewHolder mViewHolder;
    private AlertDialog successDialog;

    public static ChangePinFragment newInstance(int step) {
        ChangePinFragment f = new ChangePinFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CURRENT_STEP, step);
        if (step == STEP_INPUT_NEW_PIN) {
            args.putBoolean(ARG_SHOULD_NOTIFY_ABOUT_RESULT, true);
        }
        f.setArguments(args);
        return f;
    }

    private void addNumber(char number) {
        setPin(mPin + String.valueOf(number));
    }

    private void checkPin() {
        boolean isPinValid;
        switch (mCurrentStep) {
            case STEP_INPUT_OLD_PIN:
            case STEP_CHECK_PIN: {
                isPinValid = mPinStore.isValid(mPin);
                if (!isPinValid) mIncorrectAttempts++;
                if (mIncorrectAttempts >= 3) {
                    Toast.makeText(getActivity(), R.string.PinLoginFailedMessage, Toast.LENGTH_LONG).show();
                    new ResetApp().execute(getContext());
                    return;
                }

                if (isPinValid) {
                    if (mCurrentStep != STEP_CHECK_PIN) {
                        setCurrentStep(STEP_INPUT_NEW_PIN);
                    } else {
                        EventBus.getDefault().postSticky(new PinCheckResultEvent(true));
                    }
                    return;
                } else {
                    Observable.timer(DELAY_MILLIS, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                            .subscribe(time -> setPin(null));
                    mViewHolder.errorView.setVisibility(View.VISIBLE);
                    mViewHolder.enterPinView.setVisibility(View.INVISIBLE);
                }
                break;
            }
            case STEP_INPUT_NEW_PIN: {
                mNewPin = mPin;
                Observable.timer(DELAY_MILLIS, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                        .subscribe(time -> setCurrentStep(STEP_CONFIRM_NEW_PIN));
                break;
            }
            case STEP_CONFIRM_NEW_PIN: {
                boolean isPinsAreEqual = mPin.equals(mNewPin);
                if (isPinsAreEqual) {
                    Observable.timer(DELAY_MILLIS, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                            .subscribe(time -> {
                                mPinStore.set(mNewPin);
                                successDialog = new AlertDialog.Builder(getActivity())
                                        .setTitle(R.string.PasswordBoss)
                                        .setMessage(R.string.NewPinSet)
                                        .setPositiveButton(R.string.OK, (dialogInterface, i) -> {
                                            dialogInterface.dismiss();
                                            EventBus.getDefault().postSticky(new PinChangeResultEvent(true));
                                        })
                                        .setOnCancelListener(dialogInterface -> {
                                            dialogInterface.dismiss();
                                            EventBus.getDefault().postSticky(new PinChangeResultEvent(true));
                                        })
                                        .create();
                                successDialog.show();
                            });

                } else {
                    setPin(null);
                    mViewHolder.errorView.setVisibility(View.VISIBLE);
                    mViewHolder.enterPinView.setVisibility(View.INVISIBLE);
                }

                break;
            }
        }
    }

    private Observable<PinStore> getPinUtilObservable() {
        return Observable.defer(DatabaseHelperSecure::getObservable)
                .filter(helper -> helper != null)
                .map(helper -> {
                    DeviceBll deviceBll = null;
                    try {
                        deviceBll = new DeviceBll(helper);
                    } catch (SQLException e) {
                        throw new IllegalStateException("Could not initialize database");
                    }
                    return deviceBll;
                })
                .flatMap(helper -> Observable.just(DatabaseHelperNonSecure.getHelper(getActivity()))
                        .map(nonSecHelper -> {
                            try {
                                return new ConfigurationBll(nonSecHelper);
                            } catch (SQLException e) {
                                throw new IllegalStateException("Could not initialize database");
                            }
                        })
                        .map(configurationBll -> new PinStoreOld(getActivity(), helper)));
    }

    private void initListeners() {
        mViewHolder.deleteButton.setOnClickListener(this);
        mViewHolder.pinKeyboardView.setOnClickKeypadListener(this::addNumber);
    }

    @Override
    protected void invalidateToolbar(@NonNull AppToolbar toolbar) {
        super.invalidateToolbar(toolbar);
        toolbar.displayUpNavigation();
        toolbar.setTitle(R.string.ChangePIN);
    }

    @Override
    public boolean onBackPressed() {
        if (getArguments().getBoolean(ARG_SHOULD_NOTIFY_ABOUT_RESULT, false)) {
            if (mCurrentStep != STEP_CHECK_PIN) {
                EventBus.getDefault().postSticky(new PinChangeResultEvent(false));
            } else {
                EventBus.getDefault().postSticky(new PinCheckResultEvent(false));
            }
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fr_chpn_button_delete: {
                String newPin = null;
                if (mPin.length() > 0) newPin = mPin.substring(0, mPin.length() - 1);
                setPin(newPin);
                break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_pin, container, false);
    }

    @Override
    public void onPause() {
        if (successDialog != null && successDialog.isShowing()) {
            successDialog.dismiss();
            successDialog = null;
        }
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        setCurrentStep(getArguments().getInt(ARG_CURRENT_STEP, STEP_INPUT_OLD_PIN));
        getPinUtilObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<PinStore>(getActivity()) {
                    @Override
                    public void onNext(PinStore pinStore) {
                        mPinStore = pinStore;
                        initListeners();
                    }
                });

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewHolder = new ViewHolder(view);
    }

    private void setCurrentStep(int step) {
        mCurrentStep = step;
        setPin(null);
        switch (mCurrentStep) {
            case STEP_INPUT_OLD_PIN:
            case STEP_CHECK_PIN: {
                mViewHolder.enterPinView.setText(R.string.EnterExistingPin);
                break;
            }
            case STEP_INPUT_NEW_PIN: {
                mViewHolder.enterPinView.setText(R.string.EnterNewPin);
                break;
            }
            case STEP_CONFIRM_NEW_PIN: {
                mViewHolder.enterPinView.setText(R.string.ConfirmPin);
                break;
            }
        }
    }

    private void setPin(@Nullable String pin) {
        mPin = Strings.nullToEmpty(pin);
        if (mPin.length() > 4) mPin = mPin.substring(0, 4);
        mViewHolder.deleteButton.setVisibility(mPin.length() > 0 ? View.VISIBLE : View.INVISIBLE);
        mViewHolder.enterPinView.setVisibility(View.VISIBLE);
        mViewHolder.errorView.setVisibility(View.INVISIBLE);
        mViewHolder.visibleSymbol01.setText(mPin.length() >= 1 ? Character.valueOf(mPin.charAt(0)).toString() : "");
        mViewHolder.visibleSymbol02.setText(mPin.length() >= 2 ? Character.valueOf(mPin.charAt(1)).toString() : "");
        mViewHolder.visibleSymbol03.setText(mPin.length() >= 3 ? Character.valueOf(mPin.charAt(2)).toString() : "");
        mViewHolder.visibleSymbol04.setText(mPin.length() >= 4 ? Character.valueOf(mPin.charAt(3)).toString() : "");
        if (mPin.length() >= 4) checkPin();
    }

    public static class ViewHolder {

        public Button deleteButton;
        public TextView enterPinView;
        public TextView errorView;
        public AppPinKeyboardView pinKeyboardView;
        public View visibleInput;
        public TextView visibleSymbol01;
        public TextView visibleSymbol02;
        public TextView visibleSymbol03;
        public TextView visibleSymbol04;

        public ViewHolder(View rootView) {
            visibleSymbol01 = (TextView) rootView.findViewById(R.id.fr_chpn_visible_symbol_01);
            visibleSymbol02 = (TextView) rootView.findViewById(R.id.fr_chpn_visible_symbol_02);
            visibleSymbol03 = (TextView) rootView.findViewById(R.id.fr_chpn_visible_symbol_03);
            visibleSymbol04 = (TextView) rootView.findViewById(R.id.fr_chpn_visible_symbol_04);

            deleteButton = (Button) rootView.findViewById(R.id.fr_chpn_button_delete);
            enterPinView = (TextView) rootView.findViewById(R.id.fr_chpn_label_enter_pin);
            errorView = (TextView) rootView.findViewById(R.id.fr_chpn_label_error);
            pinKeyboardView = (AppPinKeyboardView) rootView.findViewById(R.id.fr_chpn_keyboard);

            visibleInput = rootView.findViewById(R.id.fr_chpn_visible_input);
        }
    }
}
