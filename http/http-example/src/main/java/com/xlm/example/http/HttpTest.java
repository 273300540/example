package com.xlm.example.http;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface HttpTest {
    @GET("/repos/{owner}/{repo}/contributors")
    List<Contributor> contributors(
            @Path("owner") String owner,
            @Path("repo") String repo);

    @GET("/repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> contributorsCall(
            @Path("owner") String owner,
            @Path("repo") String repo);
}
