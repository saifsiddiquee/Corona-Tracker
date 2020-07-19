package com.saif.coronatracker.customs;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.saif.coronatracker.constants.FontNames;

public class TextViewRegular extends androidx.appcompat.widget.AppCompatTextView {

    public TextViewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), new FontNames().APP_REGULAR);
        setTypeface(typeface);
    }
}