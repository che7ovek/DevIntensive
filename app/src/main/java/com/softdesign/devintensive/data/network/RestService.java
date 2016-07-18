package com.softdesign.devintensive.data.network;


import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RestService {
    @POST("login")
    Call<UserModelRes> loginUser (@Body UserLoginReq req);

    @GET("user/list?orderBy=rating")
    Call<UserListRes> getUserList();
}
