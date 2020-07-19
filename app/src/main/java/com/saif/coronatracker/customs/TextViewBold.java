package com.saif.coronatracker.customs;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.saif.coronatracker.constants.FontNames;

public class TextViewBold extends AppCompatTextView {
    public TextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), new FontNames().APP_BOLD);
        setTypeface(typeface);
    }
}
