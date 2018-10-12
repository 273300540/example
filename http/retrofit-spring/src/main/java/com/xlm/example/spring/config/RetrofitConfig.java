package com.xlm.example.spring.config;

public class RetrofitConfig {
    private String baseUrl;
    private boolean userDefault = true;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public boolean isUserDefault() {
        return userDefault;
    }

    public void setUserDefault(boolean userDefault) {
        this.userDefault = userDefault;
    }
}
