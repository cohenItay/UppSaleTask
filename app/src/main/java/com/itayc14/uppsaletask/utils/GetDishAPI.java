package com.itayc14.uppsaletask.utils;

import android.support.annotation.Nullable;

import com.itayc14.uppsaletask.Dish;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by itaycohen on 16.5.2017.
 */

public interface GetDishAPI {
    @GET("posapi/api/Test")
    Call<List<Dish>> getSpecificDish(@Query("query") String Dish);

    @GET("posapi/api/Test")
    Call<List<Dish>> getAllDishes();
}
