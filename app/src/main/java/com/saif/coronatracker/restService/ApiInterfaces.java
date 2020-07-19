package com.saif.coronatracker.restService;

import com.saif.coronatracker.models.GlobalStats;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterfaces {

    @GET("/v3/covid-19/all")
    Call<GlobalStats> getAllCountries();
}
