package com.itayc14.uppsaletask.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by itaycohen on 16.5.2017.
 */

public class RetrofitBuilderHelper {
    private static final String BASE_URL = "http://testuppsale.azurewebsites.net/";
    private static Retrofit retrofit = null;

    /**
     * creates an Retrofit instance with Gson converter
     * @return the Retrofit instance
     */
    public static Retrofit getInstance(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
