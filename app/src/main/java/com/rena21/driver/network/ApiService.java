package com.rena21.driver.network;


import com.rena21.driver.pojo.UserToken;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/requestTokenWithPhoneNumber")
    Call<UserToken> getToken(@Query("phoneNumber") String phoneNumber);

    @POST("/api/vendor/add")
    Call<Void> setVendorInfo(@Body HashMap<String, Object> bodyMap);
}
