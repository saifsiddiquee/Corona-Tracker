package com.saif.coronatracker.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.saif.coronatracker.MainActivity;
import com.saif.coronatracker.R;
import com.saif.coronatracker.adapters.CountryAdapter;
import com.saif.coronatracker.customs.CustomProgressDialog;
import com.saif.coronatracker.databinding.ActivityAllCountryBinding;
import com.saif.coronatracker.databinding.ActivityMainBinding;
import com.saif.coronatracker.helpers.Methods;
import com.saif.coronatracker.models.CountriesResponse;
import com.saif.coronatracker.restService.ApiClients;
import com.saif.coronatracker.restService.ApiInterfaces;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllCountryActivity extends AppCompatActivity {

    private ActivityAllCountryBinding allCountryBinding;
    private Activity mActivity;
    private Methods methods;
    private SearchView searchView;
    private CustomProgressDialog customProgress;

    private CountryAdapter countryAdapter;
    private List<CountriesResponse> countriesResponseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allCountryBinding = ActivityAllCountryBinding.inflate(getLayoutInflater());
        View view = allCountryBinding.getRoot();
        setContentView(view);
        mActivity = AllCountryActivity.this;
        methods = new Methods(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            methods.changeActionBarFont("Affected Countries");
        }

        customProgress = CustomProgressDialog.getInstance();
        customProgress.showProgress(mActivity, "Fetching Data...", false);
        initAllCountryData();
    }

    private void initAllCountryData() {
        allCountryBinding.rvCountry.setLayoutManager(new LinearLayoutManager(this));
        countryAdapter = new CountryAdapter();
        allCountryBinding.rvCountry.setAdapter(countryAdapter);
        countriesResponseList = new ArrayList<>();

        ApiInterfaces coronaService =
                ApiClients.getClient().create(ApiInterfaces.class);

        Call<List<CountriesResponse>> call = coronaService.getCountries();
        call.enqueue(new Callback<List<CountriesResponse>>() {
            @Override
            public void onResponse(@NotNull Call<List<CountriesResponse>> call, @NotNull Response<List<CountriesResponse>> response) {
                countriesResponseList = response.body();
                if (countriesResponseList != null) {
                    for (CountriesResponse countriesResponse : countriesResponseList) {

                        System.out.println("Country Name : " + countriesResponse.getCountry() + " - Death Count : " + countriesResponse.getDeaths() + "\n");

                        countryAdapter.setCountryList(getApplicationContext(), countriesResponseList);
                    }
                }
                customProgress.hideProgress();
            }

            @Override
            public void onFailure(@NotNull Call<List<CountriesResponse>> call, @NotNull Throwable t) {
                customProgress.hideProgress();
                Log.d("Error", t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setQueryHint("Search");
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(android.R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorAccent));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                countryAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                countryAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        } else if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}
