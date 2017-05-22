package com.passwordboss.android.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.passwordboss.android.R;

/**
 * Created by Boris on 26/05/16.
 */
public class PasswordPropertiesView extends RelativeLayout {

    @Bind(R.id.cv_psprop_text_decription)
    TextView textDescription;
    @Bind(R.id.cv_psprop_letters)
    CheckBox isLettersUsed;
    @Bind(R.id.cv_psprop_numbers)
    CheckBox isNumbersUsed;
    @Bind(R.id.cv_psprop_capitals)
    CheckBox isCapitalsUsed;
    @Bind(R.id.cv_psprop_symbols)
    CheckBox isSymbolsUsed;
    @Bind(R.id.cv_psprop_min_length)
    CheckBox hasMinLength;

    public PasswordPropertiesView(Context context) {
        super(context);
        init(context);
    }

    public PasswordPropertiesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PasswordPropertiesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PasswordPropertiesView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context c){
        LayoutInflater inflater = LayoutInflater.from(c);
        View rootView = inflater.inflate(R.layout.password_properties_view, this, false);
        addView(rootView);
        ButterKnife.bind(this);
    }

    public void setTextDescription(@NonNull String text){
        textDescription.setText(text);
    }

    public void setIsLettersUsed(boolean isLettersUsed){
        this.isLettersUsed.setChecked(isLettersUsed);
    }

    public void setIsNumbersUsed(boolean isNumbersUsed){
        this.isNumbersUsed.setChecked(isNumbersUsed);
    }

    public void setIsCapitalsUsed(boolean isCapitalsUsed){
        this.isCapitalsUsed.setChecked(isCapitalsUsed);
    }

    public void setIsSymbolsUsed(boolean isSymbolsUsed){
        this.isSymbolsUsed.setChecked(isSymbolsUsed);
    }

    public void setHasMinLength(boolean hasMinLength){
        this.hasMinLength.setChecked(hasMinLength);
    }

    public boolean isValid(){
        return isLettersUsed.isChecked() &&
                isNumbersUsed.isChecked() &&
                isCapitalsUsed.isChecked() &&
                isSymbolsUsed.isChecked() &&
                hasMinLength.isChecked();
    }
}
