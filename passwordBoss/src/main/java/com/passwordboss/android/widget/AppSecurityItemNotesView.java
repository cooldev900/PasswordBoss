package com.passwordboss.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.passwordboss.android.R;
import com.passwordboss.android.helper.SoftKeyboard;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppSecurityItemNotesView extends LinearLayout {
    private static final String KEY_SUPER_STATE = "keySuperState";
    private static final String KEY_TEXT = "keyText";
    @Bind(R.id.vw_sin_label)
    TextView mLabelView;
    @Bind(R.id.vw_sin_value)
    EditText mValueView;

    public AppSecurityItemNotesView(Context context) {
        super(context);
        initialize();
    }

    public AppSecurityItemNotesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
        processAttributeSet(attrs);
    }

    public AppSecurityItemNotesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
        processAttributeSet(attrs);
    }

    public CharSequence getText() {
        return mValueView.getText();
    }

    public void setText(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            mValueView.setVisibility(VISIBLE);
            mLabelView.setText(R.string.Notes);
        }
        mValueView.setText(text);
    }

    private void initialize() {
        View view = View.inflate(getContext(), R.layout.view_security_item_notes, this);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.vw_sin_label)
    void onClickLabel() {
        if (mValueView.getVisibility() == VISIBLE) return;
        mLabelView.setText(R.string.Notes);
        mValueView.setVisibility(VISIBLE);
        new SoftKeyboard().showImplicit(mValueView);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            setText(bundle.getCharSequence(KEY_TEXT));
            state = bundle.getParcelable(KEY_SUPER_STATE);
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SUPER_STATE, super.onSaveInstanceState());
        bundle.putCharSequence(KEY_TEXT, mValueView.getText());
        return bundle;
    }

    private void processAttributeSet(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AppSecurityItemNotesView);
        CharSequence text = typedArray.getText(R.styleable.AppSecurityItemNotesView_android_text);
        setText(text);
        typedArray.recycle();
    }
}
