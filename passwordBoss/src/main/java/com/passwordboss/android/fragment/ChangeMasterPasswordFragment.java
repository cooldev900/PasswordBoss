package com.passwordboss.android.fragment;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.passwordboss.android.R;
import com.passwordboss.android.dialog.ProgressDialog;
import com.passwordboss.android.dialog.alert.ErrorDialog;
import com.passwordboss.android.event.MasterPasswordChangedEvent;
import com.passwordboss.android.event.MasterPasswordTaskResultEvent;
import com.passwordboss.android.task.ChangeMasterPasswordTask;
import com.passwordboss.android.widget.MasterPasswordView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangeMasterPasswordFragment extends EventBusFragment {

    public static final String TAG = ChangeMasterPasswordFragment.class.getName() + "Tag";

    public static final String PARAM_STEP = "param_step";

    public static final int STEP_ENTER_CURRENT_PASSWORD = 1;
    public static final int STEP_ENTER_NEW_PASSWORD = 2;
    public static final int STEP_CONFIRM_NEW_PASSWORD = 3;

    public HashMap<Integer, Integer> buttonTextMap;
    @Bind(R.id.fr_ch_ps_main_view)
    MasterPasswordView mMasterPasswordView;
    @Bind(R.id.fr_ch_ps_button_next)
    Button mNextButton;
    private int mCurrentStep;
    private AlertDialog mDialog;

    private boolean dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
            return true;
        }
        return false;
    }

    public boolean onBackPressed() {
        boolean wasDialogClosed = dismissDialog();
        if (!wasDialogClosed && mCurrentStep > 2) {
            --mCurrentStep;
            showViewForStep(mCurrentStep);
            return true;
        } else {
            return false;
        }
    }

    @OnClick(R.id.fr_ch_ps_button_next)
    public void onClickButtonNext() {
        boolean isValid;
        switch (mCurrentStep) {
            case STEP_ENTER_CURRENT_PASSWORD: {
                isValid = mMasterPasswordView.isValid(MasterPasswordView.CHECK_TYPE_CURRENT_PASSWORD);
                break;
            }
            case STEP_ENTER_NEW_PASSWORD: {
                isValid = mMasterPasswordView.isValid(MasterPasswordView.CHECK_TYPE_NEW_PASSWORD);
                break;
            }
            case STEP_CONFIRM_NEW_PASSWORD: {
                isValid = mMasterPasswordView.isValid(MasterPasswordView.CHECK_TYPE_CONFIRM_PASSWORD);
                break;
            }
            default: {
                isValid = false;
            }
        }
        if (!isValid) return;
        ++mCurrentStep;
        if (buttonTextMap.containsKey(mCurrentStep)) {
            mNextButton.setText(buttonTextMap.get(mCurrentStep));
            showViewForStep(mCurrentStep);
            return;
        }
        new ProgressDialog(this).show(getText(R.string.UpdatingData));
        new ChangeMasterPasswordTask(
                getContext(),
                mMasterPasswordView.getNewPassword(),
                mMasterPasswordView.getOldPassword())
                .start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_master_password, container, false);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(MasterPasswordTaskResultEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        new ProgressDialog(this).dismiss();
        if (event.hasError()) {
            new ErrorDialog(getActivity()).show(event.getError(), (dialogInterface, i) -> {
                dialogInterface.dismiss();
                EventBus.getDefault().post(new MasterPasswordChangedEvent());
            });
            return;
        }
        mDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.MasterPassChangedTitle)
                .setMessage(R.string.RememberToUseNewPassword)
                .setPositiveButton(R.string.OK, (dialogInterface, i) -> {
                    dismissDialog();
                    EventBus.getDefault().post(new MasterPasswordChangedEvent());
                })
                .setCancelable(false)
                .create();
        mDialog.show();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonTextMap = new HashMap<>();
        buttonTextMap.put(STEP_ENTER_CURRENT_PASSWORD, R.string.Next);
        buttonTextMap.put(STEP_ENTER_NEW_PASSWORD, R.string.Next);
        buttonTextMap.put(STEP_CONFIRM_NEW_PASSWORD, R.string.Update);

        ButterKnife.bind(this, view);

        mCurrentStep = (savedInstanceState == null) ? STEP_ENTER_CURRENT_PASSWORD :
                savedInstanceState.getInt(PARAM_STEP, STEP_ENTER_CURRENT_PASSWORD);
        showViewForStep(mCurrentStep);
    }

    private void showViewForStep(int currentStep) {
        switch (currentStep) {
            case STEP_ENTER_CURRENT_PASSWORD: {
                mMasterPasswordView.showViewForViewType(MasterPasswordView.VIEW_TYPE_CHANGE_MASTER_PASSWORD);
                break;
            }
            case STEP_ENTER_NEW_PASSWORD: {
                mMasterPasswordView.showViewForViewType(MasterPasswordView.VIEW_TYPE_CHOOSE_MASTER_PASSWORD);
                break;
            }
            case STEP_CONFIRM_NEW_PASSWORD: {
                mMasterPasswordView.showViewForViewType(MasterPasswordView.VIEW_TYPE_CONFIRM_NEW_PASSWORD);
                break;
            }
        }
    }

}
