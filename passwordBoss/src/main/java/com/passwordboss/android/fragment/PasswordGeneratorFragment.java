package com.passwordboss.android.fragment;


import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.passwordboss.android.R;
import com.passwordboss.android.toolbar.AppToolbar;
import com.passwordboss.android.utils.PasswordGeneratorHelper;
import com.passwordboss.android.utils.Utils;
import com.passwordboss.android.widget.SeekBarWithThumb;

public class PasswordGeneratorFragment extends ToolbarFragment {

    private int mHeight;
    private int mPasswordLength;
    private ViewHolder mViewHolder;

    @SuppressLint("SetTextI18n")
    private void alignBubbleToSeekBarThumb() {
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.ALIGN_BOTTOM, mViewHolder.passwordLengthSeekBar.getId());
        Rect thumbRect = mViewHolder.passwordLengthSeekBar.getSeekBarThumb().getBounds();
        int mSumOfMargin = thumbRect.centerX();

        p.setMargins(mSumOfMargin, 0, 0, (int) (mHeight * 0.466));

        mViewHolder.txtPasswordLength.setLayoutParams(p);
        mViewHolder.txtPasswordLength.setVisibility(View.VISIBLE);
        mViewHolder.txtPasswordLength.setText(Integer.toString(mPasswordLength));
    }

    private void generateAndShowPassword() {
        String password = PasswordGeneratorHelper.generateDyn(
                mPasswordLength,
                mViewHolder.enableLetters.isChecked(),
                mViewHolder.enableCapitals.isChecked(),
                mViewHolder.enableNumbers.isChecked(),
                mViewHolder.enableSymbols.isChecked());
        mViewHolder.generatedPassword.setText(password);
    }

    private void initListeners() {
        mViewHolder.passwordLengthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (progress < 4) {
                    progress = 4;
                    seekBar.setProgress(progress);
                }

                mPasswordLength = progress;

                alignBubbleToSeekBarThumb();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mViewHolder.btnUpdateNow.setOnClickListener(v -> generateAndShowPassword());

        mViewHolder.btnCopy.setOnClickListener(v -> {
            if (mViewHolder.generatedPassword.getText().toString().length() == 0) {
                return;
            }

            Utils.copyTextToClipBoard(getActivity(), mViewHolder.generatedPassword.getText().toString().trim());
            Toast.makeText(getActivity(), R.string.PasswordHasBeenCopied, Toast.LENGTH_SHORT)
                    .show();
        });
    }

    @Override
    protected void invalidateToolbar(@NonNull AppToolbar toolbar) {
        super.invalidateToolbar(toolbar);
        if (isPhone()) {
            toolbar.displayUpNavigation();
        } else {
            toolbar.displayCloseNavigation();
        }
        toolbar.setTitle(R.string.PasswordGenerator);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_password_generator, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewHolder = new ViewHolder(view);

        mHeight = getResources().getDimensionPixelOffset(R.dimen.fr_pg_seekbar_thumb_height);

        mPasswordLength = mViewHolder.passwordLengthSeekBar.getProgress();

        //disable editing
        mViewHolder.generatedPassword.setKeyListener(null);
        generateAndShowPassword();

        initListeners();

        final ViewTreeObserver observer = mViewHolder.passwordLengthSeekBar.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                alignBubbleToSeekBarThumb();
                mViewHolder.passwordLengthSeekBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    public static final class ViewHolder {

        public Button btnCopy;
        public Button btnUpdateNow;
        public SwitchCompat enableCapitals;
        public SwitchCompat enableLetters;
        public SwitchCompat enableNumbers;
        public SwitchCompat enableSymbols;
        public EditText generatedPassword;
        public SeekBarWithThumb passwordLengthSeekBar;
        public TextView txtPasswordLength;

        public ViewHolder(View rootView) {
            passwordLengthSeekBar = (SeekBarWithThumb) rootView.findViewById(R.id.fr_ps_select_length);
            generatedPassword = (EditText) rootView.findViewById(R.id.fr_ps_generated_password);
            txtPasswordLength = (TextView) rootView.findViewById(R.id.fr_pg_password_length);
            enableSymbols = (SwitchCompat) rootView.findViewById(R.id.fr_pg_enable_symbols_switch);
            enableCapitals = (SwitchCompat) rootView.findViewById(R.id.fr_pg_enable_capitals_switch);
            enableNumbers = (SwitchCompat) rootView.findViewById(R.id.fr_pg_enable_numbers_switch);
            enableLetters = (SwitchCompat) rootView.findViewById(R.id.fr_pg_enable_letter_switch);
            btnUpdateNow = (Button) rootView.findViewById(R.id.fr_pg_update_now);
            btnCopy = (Button) rootView.findViewById(R.id.fr_pg_copy);
        }
    }
}
