package com.saif.coronatracker.customs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.saif.coronatracker.R;

public class CustomProgressDialog {
    public static CustomProgressDialog customProgress = null;
    private Dialog mDialog;

    public static CustomProgressDialog getInstance() {
        if (customProgress == null) {
            customProgress = new CustomProgressDialog();
        }
        return customProgress;
    }

    public void showProgress(Context context, String message, boolean cancelable) {
        mDialog = new Dialog(context, R.style.CustomDialogTheme);
        // no tile for the dialog
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_progress);
        CardView cp_cardview = mDialog.findViewById(R.id.cp_cardview);
        cp_cardview.setCardBackgroundColor(Color.parseColor("#80000000"));
        ProgressBar mProgressBar = mDialog.findViewById(R.id.cp_pbar);
        TextView progressText = mDialog.findViewById(R.id.cp_title);
        progressText.setText(message);
        progressText.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        // you can change or add this line according to your need
        mProgressBar.setIndeterminate(true);
        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);
        mDialog.show();
    }

    public void hideProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
