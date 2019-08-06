package com.example.scafolmobile.restapi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientCustom {
    public static final String BASE_URL_CUSTOM = "http://nominatim.openstreetmap.org/";
    private static Retrofit retrofit = null;
    public static Retrofit getClientCustom(){
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_CUSTOM)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
