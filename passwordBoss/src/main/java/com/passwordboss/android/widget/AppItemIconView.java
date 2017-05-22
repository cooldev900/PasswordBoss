package com.passwordboss.android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.passwordboss.android.R;
import com.passwordboss.android.model.ItemType;

public class AppItemIconView extends FrameLayout {
    private static final int DEFAULT_BORDER_COLOR = 0xffcdcdcd;
    private static final double VIEW_ASPECT_RATIO = 2;
    private final ViewAspectRatioMeasurer mAspectRatio = new ViewAspectRatioMeasurer(VIEW_ASPECT_RATIO);
    @ColorInt
    private int mBackgroundColor = -1;
    @ColorInt
    private int mBorderColor = DEFAULT_BORDER_COLOR;
    @DrawableRes
    private int mIconResId;
    private ImageView mIconView;
    private ImageView mImageView;
    private Uri mUri;


    public AppItemIconView(Context context) {
        super(context);
        initialize();
    }

    public AppItemIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public AppItemIconView(Context context, AttributeSet attrs, int defStyleAttr) {
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

    @ColorInt
    @Nullable
    public Integer getIconColor() {
        //noinspection ResourceAsColor
        return mBackgroundColor == -1 ? null : mBackgroundColor;
    }

    public void setIconColor(@ColorInt @Nullable Integer color) {
        if (null == color || color == Color.TRANSPARENT) {
            mBackgroundColor = -1;
            return;
        }
        mBackgroundColor = color;
        Resources resources = getContext().getResources();
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(resources.getDimension(R.dimen.padding_quarter));
        drawable.setColor(color);
        drawable.setStroke(resources.getDimensionPixelSize(R.dimen.vw_ii_stroke_width), mBorderColor);
        setBackground(drawable);
    }

    private void initialize() {
        View.inflate(getContext(), R.layout.view_item_icon, this);
        mIconView = (ImageView) findViewById(R.id.vw_ii_icon);
        mImageView = (ImageView) findViewById(R.id.vw_ii_image);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mAspectRatio.measure(widthMeasureSpec, heightMeasureSpec);
        int newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mAspectRatio.getMeasuredWidth(), MeasureSpec.EXACTLY);
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(mAspectRatio.getMeasuredHeight(), MeasureSpec.EXACTLY);
        super.onMeasure(newWidthMeasureSpec, newHeightMeasureSpec);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mIconResId = ss.iconId;
        mBackgroundColor = ss.backgroundColor;
        mBorderColor = ss.borderColor;
        mUri = ss.uri;
        for (int i = 0; i < getChildCount(); i++) {
            //noinspection unchecked
            getChildAt(i).restoreHierarchyState(ss.childrenStates);
        }
        // FIXME: 6/20/2016 quick fix of current problem, do some investigation to find reason of it
        setIcon(mIconResId, mBackgroundColor, mBorderColor);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.childrenStates = new SparseArray<Parcelable>();
        ss.uri = mUri;
        ss.backgroundColor = mBackgroundColor;
        ss.borderColor = mBorderColor;
        ss.iconId = mIconResId;
        for (int i = 0; i < getChildCount(); i++) {
            //noinspection unchecked
            getChildAt(i).saveHierarchyState(ss.childrenStates);
        }
        return ss;
    }

    public void setIcon(@DrawableRes int iconResId, @ColorInt int backgroundColor, @ColorInt int borderColor) {
        setIcon(iconResId);
        mBorderColor = borderColor;
        setIconColor(backgroundColor);
    }

    private void setIcon(@DrawableRes int resId) {
        mIconResId = resId;
        mIconView.setImageResource(resId);
    }

    public void setItemType(@Nullable ItemType itemType, @ColorInt @Nullable Integer color) {
        if (null == itemType) {
            mIconView.setImageDrawable(null);
            setIconColor(Color.WHITE);
            return;
        }
        if (null == color) color = itemType.getColor();
        mBorderColor = DEFAULT_BORDER_COLOR;
        setIcon(itemType.getIconId());
        setIconColor(color);
    }

    public void setItemType(@Nullable ItemType itemType) {
        setItemType(itemType, null);
    }

    private void setUri(@Nullable Uri uri) {
        mUri = uri;
        if (null == uri) return;
        Glide.with(getContext())
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(mImageView);

    }

    public void setUrl(@Nullable Uri uri, ItemType itemType) {
        setItemType(itemType);
        setUri(uri);
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
        int backgroundColor;
        int borderColor;
        SparseArray childrenStates;
        int iconId;
        Uri uri;


        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in, ClassLoader loader) {
            super(in);
            childrenStates = in.readSparseArray(loader);
            iconId = in.readInt();
            borderColor = in.readInt();
            backgroundColor = in.readInt();
            uri = in.readParcelable(loader);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeSparseArray(childrenStates);
            out.writeInt(iconId);
            out.writeInt(borderColor);
            out.writeInt(backgroundColor);
            out.writeParcelable(uri, flags);
        }
    }
}