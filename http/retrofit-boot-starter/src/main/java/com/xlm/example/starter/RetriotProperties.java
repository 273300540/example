package com.xlm.example.starter;

import com.xlm.example.spring.config.RetrofitConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Map;

@ConfigurationProperties(prefix = "spring.retriot",ignoreUnknownFields= true)
public class RetriotProperties {
    @NestedConfigurationProperty
    private Map<String,String> suffixRefernceName ;
    @NestedConfigurationProperty
    private Map<String, RetrofitConfig> suffixRetrofitConfig;
    @NestedConfigurationProperty
    private Map<String,String[]> suffixBasePackage;
    private String refernceName;
    @NestedConfigurationProperty
    private RetrofitConfig config;
    private String[] basePackage;

    public Map<String, String> getSuffixRefernceName() {
        return suffixRefernceName;
    }

    public void setSuffixRefernceName(Map<String, String> suffixRefernceName) {
        this.suffixRefernceName = suffixRefernceName;
    }

    public Map<String, RetrofitConfig> getSuffixRetrofitConfig() {
        return suffixRetrofitConfig;
    }

    public void setSuffixRetrofitConfig(Map<String, RetrofitConfig> suffixRetrofitConfig) {
        this.suffixRetrofitConfig = suffixRetrofitConfig;
    }


    public String getRefernceName() {
        return refernceName;
    }

    public void setRefernceName(String refernceName) {
        this.refernceName = refernceName;
    }

    public RetrofitConfig getConfig() {
        return config;
    }

    public void setConfig(RetrofitConfig config) {
        this.config = config;
    }

    public Map<String, String[]> getSuffixBasePackage() {
        return suffixBasePackage;
    }

    public void setSuffixBasePackage(Map<String, String[]> suffixBasePackage) {
        this.suffixBasePackage = suffixBasePackage;
    }

    public String[] getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String[] basePackage) {
        this.basePackage = basePackage;
    }
}
