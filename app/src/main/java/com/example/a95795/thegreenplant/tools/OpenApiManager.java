package com.example.a95795.thegreenplant.tools;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class OpenApiManager {
    private static OpenApiService openApiService;

    private static Retrofit createRetrofit(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OpenApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return  retrofit;
    }

    public synchronized static OpenApiService createOpenApiService(){
        if(openApiService==null){
            Retrofit retrofit=createRetrofit();
            openApiService=retrofit.create(OpenApiService.class);
        }
        return openApiService;
    }
}
