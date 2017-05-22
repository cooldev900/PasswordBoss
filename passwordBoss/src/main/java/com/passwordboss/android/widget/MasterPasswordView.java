package com.passwordboss.android.widget;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.passwordboss.android.R;
import com.passwordboss.android.helper.PasswordScanner;
import com.passwordboss.android.utils.Pref;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MasterPasswordView extends LinearLayout {

    public static final int CHECK_TYPE_CURRENT_PASSWORD = 1;
    public static final int CHECK_TYPE_NEW_PASSWORD = 2;
    public static final int CHECK_TYPE_CONFIRM_PASSWORD = 3;

    public static final int VIEW_TYPE_CHANGE_MASTER_PASSWORD = 1;
    public static final int VIEW_TYPE_CHOOSE_MASTER_PASSWORD = 2;
    public static final int VIEW_TYPE_CONFIRM_NEW_PASSWORD = 3;

    @Bind(R.id.cv_ps_title_text)
    TextView mTextTitle;
    @Bind(R.id.cv_ps_title_description)
    TextView mTextTitleDescription;
    @Bind(R.id.cv_ps_label_error)
    TextView mErrorLabel;
    @Bind(R.id.cv_ps_password)
    AppPasswordView mPasswordView;
    @Bind(R.id.cv_ps_properties_view)
    PasswordPropertiesView mPropertiesView;
    @Bind(R.id.cv_ps_confirm_status_image)
    ImageView mConfirmImage;

    private String mNewPassword;
    private String mOldPassword;
    private int mCurrentViewType;

    public MasterPasswordView(Context context) {
        super(context);
        init(context);
    }

    public MasterPasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MasterPasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MasterPasswordView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context c){
        LayoutInflater inflater = LayoutInflater.from(c);
        View rootView = inflater.inflate(R.layout.view_master_password, this, false);
        addView(rootView);

        ButterKnife.bind(this);
        initPasswordViewListener();
    }

    private void initPasswordViewListener(){
        mPasswordView.addTextChangedListener(new TextWatcher() {
            private final PasswordScanner mPasswordScanner = new PasswordScanner();

            @Override
            public void afterTextChanged(Editable s) {
                if(mCurrentViewType == VIEW_TYPE_CHOOSE_MASTER_PASSWORD) {
                    mPasswordScanner.scanPassword(s.toString());
                    mPropertiesView.setIsLettersUsed(mPasswordScanner.hasLetters());
                    mPropertiesView.setIsCapitalsUsed(mPasswordScanner.hasCapitals());
                    mPropertiesView.setHasMinLength(mPasswordScanner.hasMinLength());
                    mPropertiesView.setIsNumbersUsed(mPasswordScanner.hasDigits());
                    mPropertiesView.setIsSymbolsUsed(mPasswordScanner.hasSymbols());
                } else if(mCurrentViewType == VIEW_TYPE_CONFIRM_NEW_PASSWORD){
                    mConfirmImage.setVisibility(mNewPassword.equals(s.toString().trim()) ? View.VISIBLE : View.GONE);
                    mErrorLabel.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
    }

    public boolean isValid(int checkType){
        switch (checkType){
            case CHECK_TYPE_CURRENT_PASSWORD:{
                String password = mPasswordView.getText().toString().trim();
                String email = Pref.EMAIL;
                String DATABASE_KEY = Pref.generatePassword(email, password);
                boolean isValid = DATABASE_KEY != null && Pref.DATABASE_KEY != null && Pref.DATABASE_KEY.equals(DATABASE_KEY);
                if(!isValid){
                    mErrorLabel.setText(R.string.IncorrectMasterPassword);
                    mErrorLabel.setVisibility(View.VISIBLE);
                    mTextTitleDescription.setVisibility(View.INVISIBLE);
                } else {
                    mOldPassword = password;
                    mErrorLabel.setText("");
                    mTextTitleDescription.setVisibility(View.VISIBLE);
                    mErrorLabel.setVisibility(View.INVISIBLE);
                }
                return isValid;
            }
            case CHECK_TYPE_NEW_PASSWORD:{
                boolean isValid = mPropertiesView.isValid();
                if(isValid){
                    mNewPassword = mPasswordView.getText().toString().trim();
                }
                return isValid;
            }
            case CHECK_TYPE_CONFIRM_PASSWORD:{
                String confirmPassword = mPasswordView.getText().toString().trim();
                boolean isValid = mNewPassword != null && confirmPassword.equals(mNewPassword);
                if(!isValid){
                    mErrorLabel.setText(R.string.PasswordNotMatched);
                    mErrorLabel.setVisibility(View.VISIBLE);
                } else {
                    mErrorLabel.setVisibility(View.INVISIBLE);
                }
                return isValid;
            }
            default:{
                throw new IllegalArgumentException("Unknown checkType: " + checkType);
            }
        }
    }

    public void showViewForViewType(int viewType){
        mCurrentViewType = viewType;
        switch (viewType){
            case VIEW_TYPE_CHANGE_MASTER_PASSWORD:{
                mTextTitle.setText(R.string.ChangeMasterPass);
                mTextTitleDescription.setText(R.string.EnterCurrentMasterPassword);
                mPropertiesView.setVisibility(View.GONE);
                mConfirmImage.setVisibility(View.GONE);
                mErrorLabel.setVisibility(View.INVISIBLE);
                mPasswordView.setText("");
                break;
            }
            case VIEW_TYPE_CHOOSE_MASTER_PASSWORD:{
                mTextTitle.setText(R.string.MobileAccountCreationHeadline2);
                mTextTitleDescription.setText(R.string.MobileAccountCreationBody2);
                mPropertiesView.setVisibility(View.VISIBLE);
                mConfirmImage.setVisibility(View.GONE);
                mErrorLabel.setVisibility(View.INVISIBLE);
                mPasswordView.setText("");
                break;
            }
            case VIEW_TYPE_CONFIRM_NEW_PASSWORD:{
                mTextTitle.setText(R.string.MobileAccountCreationHeadline3);
                mTextTitleDescription.setText("");
                mPropertiesView.setVisibility(View.INVISIBLE);
                mConfirmImage.setVisibility(View.GONE);
                mErrorLabel.setVisibility(View.INVISIBLE);
                mPasswordView.setText("");
                break;
            }
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.newPassword = this.mNewPassword;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState)state;
        super.onRestoreInstanceState(ss.getSuperState());

        this.mNewPassword = ss.newPassword;
    }

    public String getNewPassword(){
        return mNewPassword;
    }

    public String getOldPassword(){
        return mOldPassword;
    }

    private static class SavedState extends BaseSavedState {

        public String newPassword;

        public SavedState(Parcel in) {
            super(in);
            newPassword = in.readString();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(newPassword);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
