package com.xlm.example.spring.config;

import com.xlm.example.tools.ExecuteCallAdapter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.function.Function;

public class RetrofitConfigurator implements BeanDefinitionRegistryPostProcessor, InitializingBean {
    private String[] basePackage;
    private String appendName;
    private Retrofit retrofit;
    private String retrofitName;
    private RetrofitConfig config;
    private Function<BeanDefinitionHolder, Boolean> handleFunction;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        HandleAbleClassPathBeanDefinitionScanner beanDefinitionScanner = new HandleAbleClassPathBeanDefinitionScanner(registry, false);
        beanDefinitionScanner.addExcludeFilter(new TypeFilter() {
            @Override
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                return !metadataReader.getClassMetadata().isInterface();
            }
        });
        beanDefinitionScanner.addIncludeFilter(new TypeFilter() {
            @Override
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                return true;
            }
        });
        if (appendName != null && appendName.length() > 0) {

            beanDefinitionScanner.setBeanNameGenerator(new BeanNameGenerator() {
                AnnotationBeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
                @Override
                public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
                    return beanNameGenerator.generateBeanName(definition,registry)+appendName;
                }
            });
        }
        beanDefinitionScanner.setBeanDefinitionHandleFunction(handleFunction);
        beanDefinitionScanner.scan(basePackage);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initialize();
    }

    public void initialize() {
        if (retrofitName != null && retrofitName.length() > 0) {
            handleFunction = new RetrofitHandler(retrofitName);
            return;
        } else if (retrofit == null) {
            retrofit = buildFromConfig();
        }
        if (retrofit == null) {
            throw new IllegalStateException("retrofit is null");
        }
        handleFunction = new RetrofitHandler(retrofit);
    }

    protected Retrofit buildFromConfig() {
        if (config == null) {
            return null;
        }
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(config.getBaseUrl());
        if (config.isUserDefault()) {
            builder.addCallAdapterFactory(new ExecuteCallAdapter.ExecuteCallAdapterFacatory());
            builder.addConverterFactory(GsonConverterFactory.create());
        }
        return builder.build();
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    public String[] getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String[] basePackage) {
        this.basePackage = basePackage;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public String getRetrofitName() {
        return retrofitName;
    }

    public void setRetrofitName(String retrofitName) {
        this.retrofitName = retrofitName;
    }

    public RetrofitConfig getConfig() {
        return config;
    }

    public void setConfig(RetrofitConfig config) {
        this.config = config;
    }

    public String getAppendName() {
        return appendName;
    }

    public void setAppendName(String appendName) {
        this.appendName = appendName;
    }
}
