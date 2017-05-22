package com.passwordboss.android.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.passwordboss.android.R;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.bll.ConfigurationBll;
import com.passwordboss.android.event.TwoStepVerificationValueUpdatedEvent;
import com.passwordboss.android.event.TwoStepVerificationWizardNextStepEvent;
import com.passwordboss.android.utils.Pref;

import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TwoStepVerificationDoneFragment extends BaseFragment {
    private static final Logger LOGGER = LoggerFactory.getLogger(TwoStepVerificationDoneFragment.class);

    @OnClick(R.id.fr_tsvd_button_done)
    void onClickButtonDone() {
        EventBus.getDefault().post(new TwoStepVerificationWizardNextStepEvent(TwoStepVerificationWizardNextStepEvent.Step.Close));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_two_step_verification_done, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            ConfigurationBll configurationBll = new ConfigurationBll(DatabaseHelperNonSecure.getHelper(getContext()));
            configurationBll.updateOrInsertItem(Pref.EMAIL, DatabaseConstants.ADVANCED_TWO_STEP_VERIFICATION, "1");
            EventBus.getDefault().postSticky(new TwoStepVerificationValueUpdatedEvent());
        } catch (SQLException e) {
            LOGGER.error("update 2-step verification at configuration bll", e);
        }
    }
}
