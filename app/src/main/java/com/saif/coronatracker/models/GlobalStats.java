package com.saif.coronatracker.models;

import com.google.gson.annotations.SerializedName;

public class GlobalStats {

    @SerializedName("cases")
    private String cases;

    @SerializedName("critical")
    private String critical;

    @SerializedName("active")
    private String active;

    @SerializedName("todayCases")
    private String todayCases;

    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }

    public String getCritical() {
        return critical;
    }

    public void setCritical(String critical) {
        this.critical = critical;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getTodayCases() {
        return todayCases;
    }

    public void setTodayCases(String todayCases) {
        this.todayCases = todayCases;
    }
}
