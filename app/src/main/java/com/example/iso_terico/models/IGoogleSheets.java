package com.example.iso_terico.models;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface IGoogleSheets {
    @GET
    Call<String> getPersonas(@Url String url);
    @POST("exec")
    Call<String> getStringRequestBody(@Body String body);
}
