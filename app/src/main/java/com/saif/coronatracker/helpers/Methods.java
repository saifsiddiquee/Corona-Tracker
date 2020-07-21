package com.saif.coronatracker.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.snackbar.Snackbar;
import com.saif.coronatracker.R;

import java.util.ArrayList;
import java.util.Objects;

public class Methods {

    private Context context;

    public Methods(Context context) {
        this.context = context;
    }

    public void changeActionBarFont(String title) {
        Objects.requireNonNull(((AppCompatActivity) context).getSupportActionBar()).setDisplayShowCustomEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) context).getSupportActionBar()).setDisplayShowTitleEnabled(false);

        LayoutInflater inflator = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View v = inflator.inflate(R.layout.actionbar_title, null);

        ((TextView) v.findViewById(R.id.title)).setText(title);

        Objects.requireNonNull(((AppCompatActivity) context).getSupportActionBar()).setCustomView(v);
    }

    public void centerTitle(Window window, CharSequence title) {
        ArrayList<View> textViews = new ArrayList<>();

        window.getDecorView().findViewsWithText(textViews, title, View.FIND_VIEWS_WITH_TEXT);

        if (textViews.size() > 0) {
            AppCompatTextView appCompatTextView = null;
            if (textViews.size() == 1) {
                appCompatTextView = (AppCompatTextView) textViews.get(0);
            } else {
                for (View v : textViews) {
                    if (v.getParent() instanceof Toolbar) {
                        appCompatTextView = (AppCompatTextView) v;
                        break;
                    }
                }
            }

            if (appCompatTextView != null) {
                ViewGroup.LayoutParams params = appCompatTextView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                appCompatTextView.setLayoutParams(params);
                appCompatTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
        }
    }

    public Snackbar showInternetSnackBar() {
        Snackbar snackbar = Snackbar
                .make(((FragmentActivity) context).findViewById(android.R.id.content), "No internet connection!", Snackbar.LENGTH_INDEFINITE);

        // Changing message text color
        snackbar.setActionTextColor(context.getResources().getColor(R.color.colorSecondaryLight));
        snackbar.show();
        return snackbar;
    }
}
