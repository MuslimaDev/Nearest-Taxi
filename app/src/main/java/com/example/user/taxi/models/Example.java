package com.example.user.taxi.models;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Example {

    @SerializedName("success")
    private Boolean success;

    @SerializedName("companies")
    private List<Company> companies = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }
}