package com.passwordboss.android.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.passwordboss.android.R;
import com.passwordboss.android.event.TwoStepVerificationWizardFinishEvent;
import com.passwordboss.android.event.TwoStepVerificationWizardNextStepEvent;
import com.passwordboss.android.event.TwoStepVerificationWizardPreviousStepEvent;
import com.passwordboss.android.toolbar.AppToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class TwoStepVerificationFragment extends ToolbarFragment implements BackPressConsumer {

    private static final String KEY_DONE = "keyDone";
    private boolean mDone = false;

    @Override
    protected void invalidateToolbar(@NonNull AppToolbar toolbar) {
        super.invalidateToolbar(toolbar);
        toolbar.setTitle(R.string.TwoStepVerificationTitle);
        toolbar.displayCloseNavigation();
    }

    @Override
    public boolean onBackPressed() {
        if (mDone) {
            EventBus.getDefault().post(new TwoStepVerificationWizardFinishEvent());
            return true;
        }
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            getChildFragmentManager().popBackStack();
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment, container, false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(TwoStepVerificationWizardNextStepEvent event) {
        Fragment fragment = null;
        switch (event.getStep()) {
            case AuthenticationApp:
                fragment = new TwoStepVerificationAuthenticationAppFragment();
                break;
            case BackupPhone:
                fragment = new TwoStepVerificationBackupPhoneFragment();
                break;
            case BackupSecurityCode:
                fragment = TwoStepVerificationBackupSecurityCodeFragment.newInstance(event.getData());
                break;
            case Close:
                EventBus.getDefault().post(new TwoStepVerificationWizardFinishEvent());
                break;
            case Done:
                mDone = true; // to prevent to go back through fragments history
                fragment = new TwoStepVerificationDoneFragment();
                break;
            case MasterPassword:
                fragment = new TwoStepVerificationMasterPasswordFragment();
                break;
            case SecurityCode:
                fragment = TwoStepVerificationSecurityCodeFragment.newInstance(event.getData());
                break;
            default:
                break;
        }
        if (null == fragment) return;
        getChildFragmentManager().beginTransaction().replace(R.id.fr_fr_fragment, fragment).addToBackStack(null).commit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(TwoStepVerificationWizardPreviousStepEvent event) {
        if (getChildFragmentManager().getBackStackEntryCount() == 0) return;
        onBackPressed();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_DONE, mDone);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (null == savedInstanceState) {
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.fr_fr_fragment, new TwoStepVerificationStartFragment())
                    .commit();
        } else {
            mDone = savedInstanceState.getBoolean(KEY_DONE, false);
        }
    }
}
