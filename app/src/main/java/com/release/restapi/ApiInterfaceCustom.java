package com.release.restapi;

import com.release.model.NominatimReverseMap;

import retrofit2.Call;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterfaceCustom {
    @GET("reverse")
    Call<NominatimReverseMap> reverseLatLang(@Query("format") String format, @Query("lat") String lat,
                                             @Query("lon") String lon, @Query("zoom") String zoom, @Query("addressdetails") String addressdetails);
}
