package com.xlm.example;

import com.xlm.example.spring.config.RetrofitConfig;
import com.xlm.example.spring.config.RetrofitConfigurator;
import com.xlm.example.starter.RetriotProperties;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RetrofitConfigurators {
    private List<RetrofitConfigurator> retrofitConfiguratorList;
    private RetrofitConfigurator defaultRetrofitConfigurator;

    public void addRetrofitConfigurator(RetrofitConfigurator configurator) {
        if (this.retrofitConfiguratorList == null) {
            retrofitConfiguratorList = new ArrayList<>();
        }
        retrofitConfiguratorList.add(configurator);
    }

    public void setDefaultRetrofitConfigurator(RetrofitConfigurator configurator) {
        this.defaultRetrofitConfigurator = configurator;
    }

    public void process(BeanDefinitionRegistry registry) {
        if (retrofitConfiguratorList != null) {
            retrofitConfiguratorList.stream().forEach(one -> {
                one.postProcessBeanDefinitionRegistry(registry);
            });
        }
        if (defaultRetrofitConfigurator != null) {
            defaultRetrofitConfigurator.postProcessBeanDefinitionRegistry(registry);
        }
    }

    public void init(BeanDefinitionRegistry registry) {
        if (retrofitConfiguratorList != null) {
            retrofitConfiguratorList.stream().forEach(one -> {
                one.initialize();
            });
        }
        if (defaultRetrofitConfigurator != null) {
            defaultRetrofitConfigurator.initialize();
        }
    }

    public RetrofitConfigurator createRetrofitConfigurator(String suffix, String[] basePackage, String refrenceName, RetrofitConfig config) {
        RetrofitConfigurator configurator = new RetrofitConfigurator();
        configurator.setAppendName(suffix);
        configurator.setBasePackage(basePackage);
        if (refrenceName != null && refrenceName.length() > 0) {
            configurator.setRetrofitName(refrenceName);
        } else {
            configurator.setConfig(config);
        }
        return configurator;
    }

    public void addRetrofitConfigurator(String[] basePackage, String refrenceName) {
        RetrofitConfigurator configurator = createRetrofitConfigurator(null, basePackage, refrenceName, null);
        setDefaultRetrofitConfigurator(configurator);
    }

    public void addRetrofitConfigurator(String[] basePackage, RetrofitConfig config) {
        RetrofitConfigurator configurator = createRetrofitConfigurator(null, basePackage, null, config);
        setDefaultRetrofitConfigurator(configurator);
    }

    public void addRetrofitConfigurator(String[] basePackage, String refrenceName, RetrofitConfig config) {
        RetrofitConfigurator configurator = createRetrofitConfigurator(null, basePackage, refrenceName, config);
        setDefaultRetrofitConfigurator(configurator);
    }

    public void addRetrofitConfigurator(String suffix, String[] basePackage, RetrofitConfig config) {
        RetrofitConfigurator configurator = createRetrofitConfigurator(suffix, basePackage, null, config);
        addRetrofitConfigurator(configurator);
    }

    public void addRetrofitConfigurator(String suffix, String[] basePackage, String refrenceName) {
        RetrofitConfigurator configurator = createRetrofitConfigurator(suffix, basePackage, refrenceName, null);
        addRetrofitConfigurator(configurator);
    }

    public void from(RetriotProperties retriotProperties) {
        if (retriotProperties.getSuffixRefernceName() != null) {
            for (Map.Entry<String, String> one : retriotProperties.getSuffixRefernceName().entrySet()) {

                addRetrofitConfigurator(one.getKey(), retriotProperties.getSuffixBasePackage().get(one.getKey()), one.getValue());
            }
        }
        if (retriotProperties.getSuffixRetrofitConfig() != null) {
            for (Map.Entry<String, RetrofitConfig> one : retriotProperties.getSuffixRetrofitConfig().entrySet()) {
                addRetrofitConfigurator(one.getKey(), retriotProperties.getSuffixBasePackage().get(one.getKey()), one.getValue());
            }
        }
        addRetrofitConfigurator(retriotProperties.getBasePackage(), retriotProperties.getRefernceName(), retriotProperties.getConfig());
    }

}

