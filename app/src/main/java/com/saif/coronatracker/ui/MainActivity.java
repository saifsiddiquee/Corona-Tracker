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
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.saif.coronatracker.R;
import com.saif.coronatracker.customs.CustomProgressDialog;
import com.saif.coronatracker.databinding.ActivityMainBinding;
import com.saif.coronatracker.helpers.DrawerController;
import com.saif.coronatracker.helpers.Methods;
import com.saif.coronatracker.models.GlobalStats;
import com.saif.coronatracker.restService.ApiClients;
import com.saif.coronatracker.restService.ApiInterfaces;
import com.thefinestartist.finestwebview.FinestWebView;

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
    private DrawerController drawerController;
    private DrawerLayout drawer;
    private NavigationView navView;

    private static String aboutUrl = "https://www.who.int/emergencies/diseases/novel-coronavirus-2019/question-and-answers-hub/";
    private static String linkUrl = "https://corona.gov.bd/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingMainActivity = ActivityMainBinding.inflate(getLayoutInflater());
        View view = bindingMainActivity.getRoot();
        setContentView(view);
        mActivity = MainActivity.this;
        methods = new Methods(this);

        drawer = bindingMainActivity.drawerLayout;
        navView = bindingMainActivity.navView;

        drawerController = new DrawerController(navView, drawer);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
//            methods.changeActionBarFont("Corona Tracker");
//            methods.centerTitle(getWindow(), getTitle());
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

        bindingMainActivity.seeMapDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, AllCountryActivity.class));
            }
        });

        bindingMainActivity.menu.setOnClickListener(view1 -> {
            if (!drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.openDrawer(GravityCompat.END, true);
            }
        });

        bindingMainActivity.layoutAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FinestWebView.Builder(mActivity)
                        .toolbarScrollFlags(0)
                        .updateTitleFromHtml(true)
                        .progressBarColor(mActivity.getResources().getColor(R.color.colorPrimary))
                        .showUrl(false)
                        .swipeRefreshColor(mActivity.getResources().getColor(R.color.colorPrimary))
                        .show(aboutUrl);

                drawer.closeDrawer(GravityCompat.END, true);

            }
        });

        bindingMainActivity.layoutLinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new FinestWebView.Builder(mActivity)
                        .toolbarScrollFlags(0)
                        .updateTitleFromHtml(true)
                        .progressBarColor(mActivity.getResources().getColor(R.color.colorPrimary))
                        .showUrl(false)
                        .swipeRefreshColor(mActivity.getResources().getColor(R.color.colorPrimary))
                        .show(linkUrl);

                drawer.closeDrawer(GravityCompat.END, true);
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
                            bindingMainActivity.txtRecovered.setText(response.body().getRecovered());
                            bindingMainActivity.txtDeath.setText(response.body().getDeaths());
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }

                    bindingMainActivity.mainHolder.setVisibility(View.VISIBLE);

                    bindingMainActivity.seeAllCases.setOnClickListener(view -> {
                        String data = new Gson().toJson(response.body());
                        CaseUpdateFragment caseUpdateFragment = new CaseUpdateFragment();
                        Bundle args = new Bundle();
                        args.putString("CASE_DATA", data);
                        caseUpdateFragment.setArguments(args);
                        caseUpdateFragment.show(getSupportFragmentManager(),
                                "CaseUpdateFragment");
                    });
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

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        } else {
            finish();

        }
        super.onBackPressed();
    }
}
