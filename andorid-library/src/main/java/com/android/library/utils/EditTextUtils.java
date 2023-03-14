package com.android.library.utils;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class EditTextUtils {

    public static void setTextLimit(final EditText editText, final TextView textView, final int maxLen) {
        if (editText == null || textView == null) {
            return;
        }
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLen)});
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("DefaultLocale")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = editText.getText().toString().trim().length();
                textView.setText(String.format("%d/%d",len,maxLen));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
