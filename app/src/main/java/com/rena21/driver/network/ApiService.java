package com.rena21.driver.network;


import com.rena21.driver.pojo.UserToken;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/requestTokenWithPhoneNumber")
    Call<UserToken> getToken(@Query("phoneNumber") String phoneNumber);
}
