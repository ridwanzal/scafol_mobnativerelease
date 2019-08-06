package com.example.scafolmobile.restapi;

import com.example.scafolmobile.model.DataResponse;
import com.example.scafolmobile.model.DataResponsePA;
import com.example.scafolmobile.model.DataResponseKegiatan;
import com.example.scafolmobile.model.DataResponsePaket;
import com.example.scafolmobile.model.NominatimReverseMap;

import retrofit2.Call;

import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterfaceCustom {
    @GET("reverse")
    Call<NominatimReverseMap> reverseLatLang(@Query("format") String format, @Query("lat") String lat,
                                             @Query("lon") String lon, @Query("zoom") String zoom, @Query("addressdetails") String addressdetails);
}
