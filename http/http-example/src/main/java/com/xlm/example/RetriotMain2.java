package com.xlm.example;

import com.xlm.example.http.Contributor;
import com.xlm.example.http.HttpTest;
import com.xlm.example.tools.ExecuteCallAdapter;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

public class RetriotMain2 {
   // private static final String API_URL = "https://api.github.com";
    private static final String API_URL = "http://127.0.0.1:8080";
    public static void main(String[] args) throws IOException {
        Retrofit.Builder builder = new Retrofit.Builder();
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                                    @Override
                                    public okhttp3.Response intercept(Chain chain) throws IOException {
                                        System.out.println(chain.request());
                                        okhttp3.Response response = chain.proceed(chain.request());
                                        System.out.println(response);
                                        return response;
                                    }
                                }
                ).build();
        builder.callFactory(httpClient);
        builder.baseUrl(API_URL).addConverterFactory(GsonConverterFactory.create());
        builder.addCallAdapterFactory(new ExecuteCallAdapter.ExecuteCallAdapterFacatory());
        Retrofit retrofit = builder.build();
        HttpTest test = retrofit.create(HttpTest.class);

        List<Contributor> contributors = test.contributors("square", "retrofit");
        System.out.println(contributors);

        Call<List<Contributor>> callContributors = test.contributorsCall("square", "retrofit");
        Response<List<Contributor>> response = callContributors.execute();
        System.out.println(response.body());


    }

}

