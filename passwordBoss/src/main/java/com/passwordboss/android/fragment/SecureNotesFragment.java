package com.passwordboss.android.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.passwordboss.android.R;
import com.passwordboss.android.database.bll.SecureItemBll;
import com.passwordboss.android.event.SecureItemsRefreshEvent;
import com.passwordboss.android.model.ItemType;
import com.passwordboss.android.toolbar.AppToolbar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;

import rx.Observable;

public class SecureNotesFragment extends ToolbarFragment {

    private Observable<Long> getCountObservable() {
        return SecureItemBll
                .getObservable()
                .flatMap(secureItemBll -> {
                    try {
                        return Observable.just(secureItemBll.getCountOfSecureItemsByType(ItemType.SecureNotes));
                    } catch (SQLException e) {
                        return Observable.error(e);
                    }
                });
    }

    @Override
    protected void invalidateToolbar(@NonNull AppToolbar toolbar) {
        super.invalidateToolbar(toolbar);
        toolbar.setTitle(R.string.SecureNotes);
        if (isPhone()) {
            toolbar.displayUpNavigation();
        }

    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment, null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SecureItemsRefreshEvent event) {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.fr_fr_fragment);
        if (!(fragment instanceof SecureNotesSplashFragment)) {
            return;
        }
        getCountObservable()
                .subscribe(count -> {
                    if (0 == count) return;
                    getChildFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fr_fr_fragment, SecureItemsTabsFragment.newInstance(ItemType.SecureNotes))
                            .commit();
                });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (null == savedInstanceState) {
            getCountObservable()
                    .subscribe(count -> {
                        Fragment fragment;
                        if (count > 0) {
                            fragment = SecureItemsTabsFragment.newInstance(ItemType.SecureNotes);
                        } else {
                            fragment = new SecureNotesSplashFragment();
                        }
                        getChildFragmentManager()
                                .beginTransaction()
                                .add(R.id.fr_fr_fragment, fragment)
                                .commit();
                    });
        }

    }

}
