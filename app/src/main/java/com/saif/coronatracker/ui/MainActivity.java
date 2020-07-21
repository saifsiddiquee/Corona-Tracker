package com.saif.coronatracker.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.saif.coronatracker.R;
import com.saif.coronatracker.customs.CustomProgressDialog;
import com.saif.coronatracker.databinding.ActivityMainBinding;
import com.saif.coronatracker.helpers.Methods;
import com.saif.coronatracker.models.GlobalStats;
import com.saif.coronatracker.restService.ApiClients;
import com.saif.coronatracker.restService.ApiInterfaces;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding bindingMainActivity;
    private static String TAG = "MainActivity.class";

    private CustomProgressDialog customProgress;
    private Methods methods;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingMainActivity = ActivityMainBinding.inflate(getLayoutInflater());
        View view = bindingMainActivity.getRoot();
        setContentView(view);
        mActivity = MainActivity.this;
        methods = new Methods(this);

        if (getSupportActionBar() != null) {
            methods.changeActionBarFont("Corona Tracker");
            methods.centerTitle(getWindow(), getTitle());
        }

        customProgress = CustomProgressDialog.getInstance();
        customProgress.showProgress(MainActivity.this, "Please Wait...", false);
        getGlobalStats();

        bindingMainActivity.swipeRefresh.setOnRefreshListener(() -> mActivity.runOnUiThread(() -> {
            bindingMainActivity.mainHolder.setVisibility(View.GONE);
            customProgress = CustomProgressDialog.getInstance();
            customProgress.showProgress(MainActivity.this, "Fetching Data...", false);
            new Handler().postDelayed(() -> {
                getGlobalStats();
                bindingMainActivity.swipeRefresh.setRefreshing(false);
            }, 1500);
        }));

        bindingMainActivity.cardAffectedCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, AllCountryActivity.class));
            }
        });
    }

    private void getGlobalStats() {
        ApiInterfaces coronaService =
                ApiClients.getClient().create(ApiInterfaces.class);

        Call<GlobalStats> call = coronaService.getAllCountries();
        call.enqueue(new Callback<GlobalStats>() {
            @Override
            public void onResponse(@NonNull Call<GlobalStats> call, @NonNull Response<GlobalStats> response) {
                customProgress.hideProgress();
                bindingMainActivity.imgNoInternet.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    try {
                        if (response.body() != null) {
                            bindingMainActivity.txtTotalCases.setText(response.body().getCases());
                            bindingMainActivity.txtActiveCases.setText(response.body().getActive());
                            bindingMainActivity.txtTodayCases.setText(response.body().getTodayCases());
                            bindingMainActivity.txtCriticalCases.setText(response.body().getCritical());
                            bindingMainActivity.txtRecovered.setText(response.body().getRecovered());
                            bindingMainActivity.txtRecoveredToday.setText(response.body().getTodayRecovered());
                            bindingMainActivity.txtDeath.setText(response.body().getDeaths());
                            bindingMainActivity.txtDeathToday.setText(response.body().getTodayDeaths());
                            bindingMainActivity.txtAffectedCountries.setText(response.body().getAffectedCountries());
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }

                    bindingMainActivity.mainHolder.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<GlobalStats> call, @NonNull Throwable t) {
                customProgress.hideProgress();

                if (t instanceof IOException) {
                    //internet problem
                    bindingMainActivity.imgNoInternet.setVisibility(View.VISIBLE);
                    Snackbar snackbar = methods.showInternetSnackBar();
                    snackbar.setAction("Retry", view -> {
                        customProgress = CustomProgressDialog.getInstance();
                        customProgress.showProgress(MainActivity.this, "Please Wait...", false);
                        getGlobalStats();
                    });
                } else {
                    //server problem
                    Toast.makeText(mActivity, mActivity.getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
