package com.passwordboss.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.passwordboss.android.R;

public class AppStateInputView extends AutoCompleteTextView {
    public AppStateInputView(Context context) {
        super(context);
        initialize();
    }

    public AppStateInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public AppStateInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public AppStateInputView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    private void initialize() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.us_states));
        setAdapter(adapter);
    }
}
