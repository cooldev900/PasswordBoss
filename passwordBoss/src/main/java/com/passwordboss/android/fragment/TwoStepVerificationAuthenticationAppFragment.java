package com.passwordboss.android.fragment;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.passwordboss.android.R;
import com.passwordboss.android.dialog.ProgressDialog;
import com.passwordboss.android.dialog.alert.ErrorDialog;
import com.passwordboss.android.event.BaseEvent;
import com.passwordboss.android.event.TwoStepVerificationWizardNextStepEvent;
import com.passwordboss.android.event.TwoStepVerificationWizardPreviousStepEvent;
import com.passwordboss.android.http.ServerAPI;
import com.passwordboss.android.http.UnexpectedServerError;
import com.passwordboss.android.http.beans.ServerResponseHttpBean;
import com.passwordboss.android.http.beans.TwoStepVerificationHttpBean;
import com.passwordboss.android.task.NetworkTask;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TwoStepVerificationAuthenticationAppFragment extends EventBusFragment {

    public static final String URL_AUTHENTICATION_APPS = "https://support.passwordboss.com/customer/portal/articles/1958846-what-2-step-verification-apps-does-password-boss-support-?b_id=6279&utm_source=PC&utm_medium=Wizard&utm_campaign=2StepSetup";
    private static final Logger LOGGER = LoggerFactory.getLogger(TwoStepVerificationAuthenticationAppFragment.class);
    @Bind(R.id.fr_tsvaa_label_authenticator_apps)
    TextView mAuthenticatorAppsView;
    @Bind(R.id.fr_tsvaa_qr_code)
    ImageView mQrView;
    @Bind(R.id.fr_tsvaa_secret_key)
    TextView mSecretKeyView;
    private TwoStepVerificationHttpBean mData;

    private void bindDataToViews() {
        if (null == mData) return;
        mSecretKeyView.setText(mData.getMulti_factor_code());
        try {
            SVG svg = SVG.getFromString(mData.getQr());
            if (svg.getDocumentWidth() != -1) {
                Bitmap newBM = Bitmap.createBitmap((int) Math.ceil(svg.getDocumentWidth()),
                        (int) Math.ceil(svg.getDocumentHeight()),
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(newBM);
                canvas.drawRGB(255, 255, 255);
                svg.renderToCanvas(canvas);
                mQrView.setImageBitmap(newBM);
            }
        } catch (SVGParseException e) {
            LOGGER.error("SVG error", e);
        }
    }

    @OnClick(R.id.fr_tsvaa_button_copy)
    void onClickButtonCopy() {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("code", mSecretKeyView.getText().toString()));
        Toast.makeText(getContext(), R.string.SecretKeyCopied, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.fr_tsvaa_button_next)
    void onClickButtonNext() {
        TwoStepVerificationWizardNextStepEvent.Data data = new TwoStepVerificationWizardNextStepEvent.Data();
        data.setOneTimeCode(mData.getMulti_factor_one_time_code());
        EventBus.getDefault().post(new TwoStepVerificationWizardNextStepEvent(TwoStepVerificationWizardNextStepEvent.Step.SecurityCode, data)); // deliver code into Backup Security Code fragment
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_two_step_verification_authentication_app, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onGetDataResultEvent(GetDataResultEvent event) {
        try {
            EventBus.getDefault().removeStickyEvent(event);
            if (event.hasError()) {
                new ErrorDialog(getContext()).show(event.getError(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EventBus.getDefault().post(new TwoStepVerificationWizardPreviousStepEvent());
                    }
                });
                return;
            }
            mData = event.mTwoStepVerificationHttpBean;
            bindDataToViews();
        } finally {
            new ProgressDialog(this).dismiss();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String firstPart = getString(R.string.TwoStepSecretKeyScreenFirstMessage);
        String secondPart = getString(R.string.TwoStepSecretKeyScreenFirstMessageAppList);
        mAuthenticatorAppsView.setMovementMethod(LinkMovementMethod.getInstance());
        mAuthenticatorAppsView.setText(Html.fromHtml(firstPart + "<a href=" + URL_AUTHENTICATION_APPS + "> " + secondPart + "</a>"));
        new ProgressDialog(this).showLoading();
        new GetDataTask(getContext()).start();
    }

    private static class GetDataTask extends NetworkTask<GetDataResultEvent> {

        private TwoStepVerificationHttpBean mTwoStepVerification;

        private GetDataTask(Context context) {
            super(context);
        }

        @Override
        protected GetDataResultEvent createEventError(Exception e) {
            return new GetDataResultEvent(e);
        }

        @Override
        protected GetDataResultEvent createEventSuccess() {
            return new GetDataResultEvent(mTwoStepVerification);
        }


        @Override
        protected void runImpl() throws Exception {
            ServerAPI serverAPI = new ServerAPI();
            ServerResponseHttpBean responseHttpBean = serverAPI.getTwoStepVerification();
            mTwoStepVerification = responseHttpBean.getTwo_step_verification();
            if (null != responseHttpBean.getError()) {
                throw new UnexpectedServerError(getContext(), responseHttpBean.getError());
            }
        }
    }

    private static class GetDataResultEvent extends BaseEvent {
        private final TwoStepVerificationHttpBean mTwoStepVerificationHttpBean;

        public GetDataResultEvent(TwoStepVerificationHttpBean twoStepVerificationHttpBean) {
            mTwoStepVerificationHttpBean = twoStepVerificationHttpBean;
        }

        public GetDataResultEvent(Throwable throwable) {
            super(throwable);
            mTwoStepVerificationHttpBean = null;
        }
    }

}
