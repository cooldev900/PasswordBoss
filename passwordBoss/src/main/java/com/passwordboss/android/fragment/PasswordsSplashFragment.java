package com.passwordboss.android.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.passwordboss.android.R;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.Site;
import com.passwordboss.android.database.beans.SiteImage;
import com.passwordboss.android.database.beans.SiteImageSize;
import com.passwordboss.android.database.beans.SiteUri;
import com.passwordboss.android.database.bll.SiteBll;
import com.passwordboss.android.database.bll.SiteImageBll;
import com.passwordboss.android.database.bll.SiteImageSizeBll;
import com.passwordboss.android.database.bll.SiteUriBll;
import com.passwordboss.android.event.BaseEvent;
import com.passwordboss.android.event.SecureItemEditEvent;
import com.passwordboss.android.logback.AppSqlError;
import com.passwordboss.android.model.ItemType;
import com.passwordboss.android.widget.AppItemIconView;
import com.tonicartos.superslim.GridSLM;
import com.tonicartos.superslim.LayoutManager;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PasswordsSplashFragment extends BaseFragment {
    @Bind(R.id.fr_pssp_recommended_sites)
    RecyclerView mRecommendedSitesView;

    @NonNull
    private SecureItem[] getRecommendedSites(DatabaseHelperSecure helperSecure) {
        try {
            SiteBll siteBll = new SiteBll(helperSecure);
            SiteUriBll siteUriBll = new SiteUriBll(helperSecure);
            List<Site> recommendedSites = siteBll.getRecommendedSites();
            SecureItem[] array = new SecureItem[recommendedSites.size()];
            for (int i = 0; i < recommendedSites.size(); i++) {
                Site site = recommendedSites.get(i);
                SecureItem secureItem = new SecureItem();
                SiteUri siteUri = siteUriBll.getBySite(site);
                if (null != siteUri) {
                    secureItem.setLoginUrl(siteUri.getUri());
                }
                secureItem.setItemType(ItemType.Website);
                secureItem.setSite(site);
                secureItem.setName(site.getFriendlyName());
                array[i] = secureItem;
            }
            return array;
        } catch (SQLException e) {
            new AppSqlError(e).log(getClass());
        }
        return new SecureItem[0];
    }

    private Map<SecureItem, Uri> getSitesImages(DatabaseHelperSecure helperSecure, SecureItem[] recommendedSites) {
        try {
            final SiteImageSize maxImageSize = new SiteImageSizeBll(helperSecure).getMaxImageSize();
            final SiteImageBll siteImageBll = new SiteImageBll(helperSecure);
            Site site;
            HashMap<SecureItem, Uri> map = new HashMap<>();
            for (SecureItem secureItem : recommendedSites) {
                site = secureItem.getSite();
                if (null == site) continue;
                SiteImage siteImage = siteImageBll.getBySize(site, maxImageSize);
                if (null == siteImage) continue;
                String url = siteImage.getUrl();
                if (null == url) continue;
                map.put(secureItem, Uri.parse(url));
            }
            return map;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    @OnClick(R.id.fr_pssp_button_finished)
    void onClickButtonFinished() {
        EventBus.getDefault().post(new PasswordSplashFinishedEvent());
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_passwords_splash, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        DatabaseHelperSecure.getObservable()
                .flatMap(db -> Observable.just(getRecommendedSites(db))
                        .flatMap(sites -> Observable.just(getSitesImages(db, sites))
                                .map(images -> new SecureItemGridAdapter(getContext(), sites, images))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(secureItemGridAdapter -> {
                    mRecommendedSitesView.setAdapter(secureItemGridAdapter);
                    mRecommendedSitesView.setLayoutManager(new LayoutManager(getContext()));
                }, Throwable::printStackTrace);
    }

    static class PasswordSplashFinishedEvent extends BaseEvent {
    }

    private static class SecureItemGridAdapter extends RecyclerView.Adapter<SecureItemGridAdapter.ViewHolder> {

        private final SecureItem[] mSecureItems;
        private final Map<SecureItem, Uri> mImages;
        private final int mGridColumnWidth;


        public SecureItemGridAdapter(Context context, SecureItem[] secureItems, Map<SecureItem, Uri> images) {
            mGridColumnWidth = context.getResources().getDimensionPixelSize(R.dimen.grid_column_width);
            mSecureItems = secureItems;
            mImages = images;
        }

        @Override
        public int getItemCount() {
            return mSecureItems.length;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            SecureItem secureItem = mSecureItems[position];
            holder.mIconView.setUrl(mImages.get(secureItem), ItemType.from(secureItem));
            holder.mNameView.setText(secureItem.getName());
            holder.itemView.setTag(secureItem);
            GridSLM.LayoutParams layoutParams = GridSLM.LayoutParams.from(holder.itemView.getLayoutParams());
            layoutParams.setSlm(GridSLM.ID);
            layoutParams.setColumnWidth(mGridColumnWidth);
            layoutParams.setFirstPosition(0);
            holder.itemView.setLayoutParams(layoutParams);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_secure_item_tile, parent, false);
            return new ViewHolder(view);
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            private final AppItemIconView mIconView;
            private final TextView mNameView;

            public ViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(v -> {
                    SecureItem secureItem = (SecureItem) v.getTag();
                    if (null == secureItem) return;
                    EventBus.getDefault().post(new SecureItemEditEvent(secureItem));
                });
                mIconView = (AppItemIconView) itemView.findViewById(R.id.it_sitl_icon);
                mNameView = (TextView) itemView.findViewById(R.id.it_sitl_name);
            }
        }
    }
}
