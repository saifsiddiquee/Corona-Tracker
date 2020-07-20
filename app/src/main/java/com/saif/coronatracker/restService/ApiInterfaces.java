package com.saif.coronatracker.restService;

import com.saif.coronatracker.models.CountriesResponse;
import com.saif.coronatracker.models.GlobalStats;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterfaces {

    @GET("/v3/covid-19/all")
    Call<GlobalStats> getAllCountries();

    @GET("/v3/covid-19/countries/{query}")
    Call<CountriesResponse> getCountryInfo(@Path("query") String country);

    @GET("/v3/covid-19/countries")
    Call<List<CountriesResponse>> getCountries();
}
