package com.saif.coronatracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.saif.coronatracker.customs.CustomProgressDialog;
import com.saif.coronatracker.databinding.ActivityMainBinding;
import com.saif.coronatracker.models.GlobalStats;
import com.saif.coronatracker.restService.ApiClients;
import com.saif.coronatracker.restService.ApiInterfaces;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding bindingMainActivity;
    private static String TAG = "MainActivity.class";

    private CustomProgressDialog customProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingMainActivity = ActivityMainBinding.inflate(getLayoutInflater());
        View view = bindingMainActivity.getRoot();
        setContentView(view);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        customProgress = CustomProgressDialog.getInstance();
        customProgress.showProgress(MainActivity.this, "Please Wait", false);
        getGlobalStats();
    }

    private void getGlobalStats() {
        ApiInterfaces coronaService =
                ApiClients.getClient().create(ApiInterfaces.class);

        Call<GlobalStats> call = coronaService.getAllCountries();
        call.enqueue(new Callback<GlobalStats>() {
            @Override
            public void onResponse(Call<GlobalStats> call, Response<GlobalStats> response) {
                customProgress.hideProgress();
                if (response.isSuccessful()) {
                    try {
                        if (response.body() != null) {
                            bindingMainActivity.txtTotalCases.setText(response.body().getCases());
                            bindingMainActivity.txtActiveCases.setText(response.body().getActive());
                            bindingMainActivity.txtTodayCases.setText(response.body().getTodayCases());
                            bindingMainActivity.txtCriticalCases.setText(response.body().getCritical());
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }

                    bindingMainActivity.mainHolder.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<GlobalStats> call, Throwable t) {
                customProgress.hideProgress();
            }
        });
    }
}
