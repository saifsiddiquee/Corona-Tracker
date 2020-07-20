package com.saif.coronatracker.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.saif.coronatracker.R;
import com.saif.coronatracker.databinding.ActivityCountryDetailsBinding;
import com.saif.coronatracker.helpers.Methods;
import com.squareup.picasso.Picasso;

public class CountryDetailsActivity extends AppCompatActivity {

    private ActivityCountryDetailsBinding bindingCountryDetails;
    private Activity mActivity;
    private Methods methods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingCountryDetails = ActivityCountryDetailsBinding.inflate(getLayoutInflater());
        View view = bindingCountryDetails.getRoot();
        setContentView(view);

        mActivity = CountryDetailsActivity.this;
        methods = new Methods(mActivity);
        setData();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        String countryName = getIntent().getStringExtra("country");
        String todayCase = getIntent().getStringExtra("todayCase");
        String todayDeath = getIntent().getStringExtra("todayDeath");
        String totalCases = getIntent().getStringExtra("cases");
        String totalDeaths = getIntent().getStringExtra("deaths");
        String totalTests = getIntent().getStringExtra("tests");
        String totalRecovered = getIntent().getStringExtra("recovered");
        String todayRecovered = getIntent().getStringExtra("todayRecovered");

        methods.changeActionBarFont(countryName);

        bindingCountryDetails.tvToday.setText(todayCase);
        bindingCountryDetails.tvTodayDeath.setText(todayDeath);
        bindingCountryDetails.tvTodayRecovered.setText(todayRecovered);

        bindingCountryDetails.tvTotalCase.setText("Total Affected: " + totalCases);
        bindingCountryDetails.tvTotalDied.setText("Total Died: " + totalDeaths);
        bindingCountryDetails.tvTotalRecovered.setText("Total Recovered: " + totalRecovered);
        bindingCountryDetails.tvTotalTest.setText("Total Tests: " + totalTests);

        Picasso.get().load(getIntent().getStringExtra("flag"))
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(bindingCountryDetails.ivCountryPoster, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        bindingCountryDetails.ivCountryPoster.setAlpha(0f);
                        bindingCountryDetails.ivCountryPoster.animate().setDuration(800).alpha(1f).start();
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
