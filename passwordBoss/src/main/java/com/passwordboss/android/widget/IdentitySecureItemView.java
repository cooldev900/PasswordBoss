package com.passwordboss.android.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.helper.SecureItemSubTitle;
import com.passwordboss.android.model.ItemType;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IdentitySecureItemView extends FrameLayout {
    @Bind(R.id.vw_idsi_chevron)
    View mChevronView;
    @Bind(R.id.vw_idsi_clear)
    View mClearView;
    @Bind(R.id.vw_idsi_icon)
    AppItemIconView mIconView;
    @Bind(R.id.vw_idsi_sub_title)
    TextView mSubTitleView;
    @Bind(R.id.vw_idsi_title)
    TextView mTitleView;
    private int mEmptyIconId;
    private ItemType mItemType;
    private SecureItem mSecureItem;

    public IdentitySecureItemView(Context context) {
        super(context);
        initialize();
    }

    public IdentitySecureItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public IdentitySecureItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        dispatchFreezeSelfOnly(container);
    }

    public ItemType getItemType() {
        return mItemType;
    }

    public SecureItem getSecureItem() {
        return mSecureItem;
    }

    private void initialize() {
        View.inflate(getContext(), R.layout.view_identity_secuire_item, this);
        setClickable(true);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.vw_idsi_clear)
    void onClickClear() {
        mSecureItem = null;
        updateViews();
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        for (int i = 0; i < getChildCount(); i++) {
            //noinspection unchecked
            getChildAt(i).restoreHierarchyState(ss.childrenStates);
        }
        setEmptyIconId(ss.emptyIconId);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.childrenStates = new SparseArray<Parcelable>();
        ss.emptyIconId = mEmptyIconId;
        for (int i = 0; i < getChildCount(); i++) {
            //noinspection unchecked
            getChildAt(i).saveHierarchyState(ss.childrenStates);
        }
        return ss;
    }

    public void setData(ItemType itemType, SecureItem secureItem) {
        mItemType = itemType;
        mSecureItem = secureItem;
        mIconView.setItemType(itemType);
        updateViews();
    }

    public void setEmptyIconId(@DrawableRes int emptyIconId) {
        mEmptyIconId = emptyIconId;
    }

    private void updateViews() {
        if (null == mSecureItem) {
            mIconView.setIcon(mEmptyIconId, Color.WHITE, 0xff18ad8c);
            final String title = null == mItemType ? null : getContext().getString(mItemType.getNameId());
            mTitleView.setText(title);
            mSubTitleView.setText(null);
            mChevronView.setVisibility(VISIBLE);
            mClearView.setVisibility(INVISIBLE);
        } else {
            mIconView.setItemType(mItemType, mSecureItem.getColor());
            mTitleView.setText(mSecureItem.getName());
            mSubTitleView.setText(new SecureItemSubTitle(mSecureItem).get());
            mChevronView.setVisibility(INVISIBLE);
            mClearView.setVisibility(VISIBLE);
        }
        mSubTitleView.setVisibility(mSubTitleView.getText().length() == 0 ? GONE : VISIBLE);
    }

    @SuppressWarnings("unchecked")
    static class SavedState extends BaseSavedState {

        public static final Creator<SavedState> CREATOR = ParcelableCompat.
                newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {
                    @Override
                    public SavedState createFromParcel(Parcel source, ClassLoader loader) {
                        return new SavedState(source, loader);
                    }

                    @Override
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                });
        SparseArray childrenStates;
        int emptyIconId;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in, ClassLoader loader) {
            super(in);
            childrenStates = in.readSparseArray(loader);
            emptyIconId = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeSparseArray(childrenStates);
            out.writeInt(emptyIconId);
        }
    }

}

