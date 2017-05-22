package com.passwordboss.android.fragment;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.passwordboss.android.R;
import com.passwordboss.android.event.TwoStepVerificationWizardNextStepEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TwoStepVerificationBackupSecurityCodeFragment extends BaseFragment {

    private static final String KEY_DATA = "keyData";
    @Bind(R.id.fr_tsvbsc_code)
    TextView mCodeView;

    public static TwoStepVerificationBackupSecurityCodeFragment newInstance(TwoStepVerificationWizardNextStepEvent.Data data) {
        TwoStepVerificationBackupSecurityCodeFragment fragment = new TwoStepVerificationBackupSecurityCodeFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_DATA, data);
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick(R.id.fr_tsvbsc_button_copy)
    void onClickButtonCopy() {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("code", mCodeView.getText().toString()));
        Toast.makeText(getContext(), R.string.SecurityCodeCopied, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.fr_tsvbsc_button_next)
    void onClickButtonNext() {
        EventBus.getDefault().post(new TwoStepVerificationWizardNextStepEvent(TwoStepVerificationWizardNextStepEvent.Step.BackupPhone));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_two_step_verification_backup_security_code, null);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        if (null == arguments) return;
        TwoStepVerificationWizardNextStepEvent.Data data = (TwoStepVerificationWizardNextStepEvent.Data) arguments.getSerializable(KEY_DATA);
        if (null == data) return;
        mCodeView.setText(data.getOneTimeCode());
    }

}
