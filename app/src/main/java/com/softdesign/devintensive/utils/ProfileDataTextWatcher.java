package com.softdesign.devintensive.utils;

import android.app.AlertDialog;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;

import com.softdesign.devintensive.R;

import java.util.regex.Pattern;

/**
 * Created by roman on 09.07.16.
 */
public class ProfileDataTextWatcher implements TextWatcher {

    private Pattern mPattern;
    private TextInputLayout mTil;

    public ProfileDataTextWatcher(Pattern p, TextInputLayout textInputLayout) {
        mPattern = p;
        mTil = textInputLayout;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.toString().trim().isEmpty()) {
            mTil.setError(DevIntensiveApplication.getAppContext().getString(R.string.error_no_data));
        } else if (!mPattern.matcher(charSequence.toString().trim()).matches()) {
            mTil.setError(DevIntensiveApplication.getAppContext().getString(R.string.error_uncorrect_data));
        } else {
            mTil.setError(null);
            mTil.setErrorEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
