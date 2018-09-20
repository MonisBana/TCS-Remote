package com.mab.merchantapp;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by MonisBana on 6/26/2018.
 */

public interface ApiService {
    @POST("user/login")
    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    Call<User> login(@Body User user);

    @POST("user/signup")
    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    Call<User> signup(@Body User user);

    @Multipart
    @POST("products")
    Call<ResponseBody> postProduct(@Part MultipartBody.Part productImage,
                                 @Part("name") RequestBody name,
                                 @Part("desc") RequestBody desc,
                                 @Part("price") RequestBody price,
                                 @Part("quantity") RequestBody quantity,
                                 @Part("category") RequestBody category,
                                   @Header("Authorization") String authHeader);
    @GET("products")
    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    Call<User> allProducts(@Body Product Products);
}
