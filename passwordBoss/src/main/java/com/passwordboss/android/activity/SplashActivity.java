package com.passwordboss.android.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.passwordboss.android.R;
import com.passwordboss.android.analytics.AnalyticsHelperSegment;
import com.passwordboss.android.app.Installation;
import com.passwordboss.android.constants.Constants;
import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.beans.Configuration;
import com.passwordboss.android.database.bll.ConfigurationBll;
import com.passwordboss.android.event.BaseEvent;
import com.passwordboss.android.helper.NetworkState;
import com.passwordboss.android.http.ServerAPI;
import com.passwordboss.android.http.beans.DeletedProfilesHttpBean;
import com.passwordboss.android.utils.Pref;
import com.passwordboss.android.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.SQLException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    private static final Logger LOGGER = LoggerFactory.getLogger(SplashActivity.class);
    @SuppressWarnings("FieldCanBeLocal")
    private static long MIN_TIMEOUT = 1500;

    @Bind(R.id.ac_sp_logo)
    ImageView imgLogo;
    @Bind(R.id.progress_bar_container)
    FrameLayout progressBarContainer;
    @Bind(R.id.ac_sp_title)
    TextView titleText;
    private Resources mResources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        mResources = getResources();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(BackgroundTaskFinishedEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        boolean languageChosen = Pref.getValue(this, Constants.LANGUAGE_CHOSEN, false);
        if (languageChosen) {
            SignInActivity.start(this);
        } else {
            startActivity(new Intent(this, SelectLanguageActivity.class));
        }
        finish();
        this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    @Override
    protected void onResume() {
        super.onResume();

        imgLogo.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imgLogo.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int currentY = (int) imgLogo.getY();
                int targetY = (int) (mResources.getDimension(R.dimen.ac_spl_logo_target_margin_top));
                AnimatorSet animatorSet = provideInitialAnimation(targetY - currentY);
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        new BackgroundTask(SplashActivity.this).start();
                    }
                });
                animatorSet.start();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private AnimatorSet provideInitialAnimation(int logoTranslationY) {
        final long LOGO_ANIMATION_DURATION = 700;
        final long ALPHA_DURATION = 450;

        AnimatorSet result = new AnimatorSet();

        ObjectAnimator progressBarAlphaAnimation = ObjectAnimator
                .ofFloat(progressBarContainer, "alpha", 0, 1f);
        progressBarAlphaAnimation.setDuration(ALPHA_DURATION);
        progressBarAlphaAnimation.setRepeatCount(0);
        progressBarAlphaAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                progressBarContainer.setVisibility(View.VISIBLE);
            }
        });

        ObjectAnimator logoTranslationAnimation = ObjectAnimator
                .ofFloat(imgLogo, "translationY", logoTranslationY);
        logoTranslationAnimation.setDuration(LOGO_ANIMATION_DURATION);
        logoTranslationAnimation.setRepeatCount(0);

        ObjectAnimator titleTranslation = ObjectAnimator
                .ofFloat(titleText, "translationY", logoTranslationY);
        titleTranslation.setDuration(LOGO_ANIMATION_DURATION);
        titleTranslation.setRepeatCount(0);

        ObjectAnimator titleAlphaAnimation = ObjectAnimator.ofFloat(titleText, "alpha", 0, 1f);
        titleAlphaAnimation.setDuration(ALPHA_DURATION);
        titleAlphaAnimation.setRepeatCount(0);
        titleAlphaAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                titleText.setVisibility(View.VISIBLE);
            }
        });

        result.play(progressBarAlphaAnimation).before(logoTranslationAnimation);
        result.playTogether(logoTranslationAnimation, titleTranslation);
        result.play(titleAlphaAnimation).after(titleTranslation);
        return result;
    }

    private static class BackgroundTask extends Thread {
        private final Context mContext;
        private ConfigurationBll mConfigurationBll;

        private BackgroundTask(Context context) {
            mContext = context;
        }


        private void createDatabaseDirIfNeeded() {
            File file = new File(Constants.APP_DATABASE);
            if (file.exists()) return;
            //noinspection ResultOfMethodCallIgnored
            file.mkdir();
        }

        private void deleteRecursive(File file) {
            try {
                if (file.isDirectory()) {
                    for (File child : file.listFiles())
                        deleteRecursive(child);
                }
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            } catch (Exception ignored) {
            }
        }

        private void initializeConfigurationBll() {
            try {
                DatabaseHelperNonSecure databaseHelperNonSecure = DatabaseHelperNonSecure.getHelper(mContext);
                mConfigurationBll = new ConfigurationBll(databaseHelperNonSecure);
            } catch (SQLException e) {
                LOGGER.error("SQL: could not create configuration bll ", e);
            }
        }

        private void initializeDatabase() {
            createDatabaseDirIfNeeded();
            Utils.extractNonSecureDatabaseIfNeeded(mContext);
        }


        private void removeDeletedProfiles() {
            if (new NetworkState(mContext).isDisconnected() || TextUtils.isEmpty(Pref.INSTALLATION_UUID))
                return;
            ServerAPI api = new ServerAPI();
            Configuration seedSync = mConfigurationBll.getConfigurationByEmailAndKey(Constants.NO_EMAIL, Constants.SEED_SINCE);
            DeletedProfilesHttpBean response;
            if (seedSync != null) {
                response = api.getPublicSync(seedSync.getValue(), true);
            } else {
                response = api.getPublicSync(null, false);
            }
            if (response != null && response.getDeletedProfiles() != null) {
                for (String account : response.getDeletedProfiles()) {
                    String path = Constants.APP_DATABASE + File.separator + account;
                    deleteRecursive(new File(path));
                }
            }
        }

        @Override
        public void run() {
            try {
                long start = System.currentTimeMillis();
                runImpl();
                long toSleep = MIN_TIMEOUT - (System.currentTimeMillis() - start);
                if (toSleep > 0) Thread.sleep(toSleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                EventBus.getDefault().postSticky(new BackgroundTaskFinishedEvent());
            }
        }

        private void runImpl() {
            initializeDatabase();
            initializeConfigurationBll();
            if (new NetworkState(mContext).isConnected() && Strings.isNullOrEmpty(Pref.INSTALLATION_UUID)) {
                new Installation().register(mContext);
            }
            AnalyticsHelperSegment.register(mContext);
            removeDeletedProfiles();
        }
    }

    private static class BackgroundTaskFinishedEvent extends BaseEvent {

        public BackgroundTaskFinishedEvent() {
            super(null);
        }
    }
}
