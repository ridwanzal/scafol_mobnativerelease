package com.release.restapi;

import com.release.model.DataResponse;
import com.release.model.DataResponseBidang;
import com.release.model.DataResponseCatatan;
import com.release.model.DataResponseDinas;
import com.release.model.DataResponsePA;
import com.release.model.DataResponseKegiatan;
import com.release.model.DataResponsePaket;
import com.release.model.DataResponseProgress;
import com.release.model.DataResponseUsers;

import retrofit2.Call;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    // dinas by user
    @GET("dinas/user")
    Call<DataResponseDinas> getDinas(@Query("user_id") String user_id);

    // bidang
    @GET("bidang/bidang")
    Call<DataResponseBidang> getBidang(@Query("ke_id") String ke_id);

    // main retrieve

    @GET("users/")
    Call<DataResponse> getUserById(@Query("user_id") String user_id);

    @GET("users/kontraktorall")
    Call<DataResponseUsers> getKontrak();

    @GET("kegiatan")
    Call<DataResponseKegiatan> getKegiatan(@Query("ke_id") String ke_id);

    @GET("kegiatan/")
    Call<DataResponseKegiatan> getKegiatanAdmin();

    @GET("kegiatan/dinas")
    Call<DataResponseKegiatan> getKegiatanAdminDinas(@Query("dinas_id") String dinas_id);

    @GET("paket/pptk")
    Call<DataResponsePaket> getPaketPptk(@Query("pptk_id") String pptk_id);

    @GET("paket/dinas")
    Call<DataResponsePaket> getPaketDinas(@Query("dinas_id") String dinas_id);

    @GET("paket/catatan")
    Call<DataResponseCatatan> getCatatan(@Query("pa_id") String pa_id);

    @FormUrlEncoded
    @POST("paket/submit_catatan")
    Call<DataResponseCatatan> addCatatan(@Field("pa_id") String pa_id,  @Field("ca_catatan") String ca_catatan, @Field("date_created") String date_created, @Field("date_updated") String date_updated);

    @GET("paket/")
    Call<DataResponsePaket> getPaketId(@Query("pa_id") String pa_id);

    @GET("progress/fisik")
    Call<DataResponseProgress> getProgressByPaket(@Query("pa_id") String pa_id);

    @GET("kegiatan/bidang/")
    Call<DataResponseKegiatan> getKegiatanBidang(@Query("bi_id") String bi_id);

    @GET("login")
    Call<DataResponse> checkLogin(@Query("username") String username, @Query("password") String password);

    // role pptk

    @GET("dashboardpptk")
    Call<DataResponsePA> infoPaketPPTK(@Query("pptk_id") String pptk_id);

    @GET("dashboardpptk/paketall/")
    Call<DataResponsePA> countPaketPPTK(@Query("pptk_id") String pptk_id);

    @GET("dashboardpptk/paketprogress/")
    Call<DataResponsePA> countPaketProgressPPTK(@Query("pptk_id") String pptk_id);

    @GET("dashboardpptk/paketbelum/")
    Call<DataResponsePA> countPaketBelumPPTK(@Query("pptk_id") String pptk_id);

    @GET("dashboardpptk/paketselesai/")
    Call<DataResponsePA> countPaketSelesai(@Query("pptk_id") String pptk_id);

    @GET("dashboardpptk/pagupptk/")
    Call<DataResponsePA> countPaguPPTK(@Query("pptk_id") String pptk_id);

    @GET("dashboardpptk/realpptk/")
    Call<DataResponsePA> countRealPPTK(@Query("pptk_id") String pptk_id);

    @GET("dashboardpptk/sisapptk/")
    Call<DataResponsePA> countSisaPPTK(@Query("pptk_id") String pptk_id);

    // paket dan progress

    @FormUrlEncoded
    @POST("paket/updatemap/")
    Call<DataResponsePaket> updateMap(@Field("pa_id") String pa_id,  @Field("pa_lokasi") String pa_lokasi, @Field("pa_loc_latitude") String pa_loc_latitude, @Field("pa_loc_longitude") String pa_loc_longitude);

    @FormUrlEncoded
    @POST("paket/updatekontrak/")
    Call<DataResponsePaket> updateKontrak(@Field("pa_id") String pa_id,  @Field("pa_nomor_kontrak") String pa_nomor_kontrak, @Field("pa_nilai_kontrak") String pa_nilai_kontrak, @Field("pa_awal_kontrak") String pa_awal_kontak, @Field("pa_akhir_kontrak") String pa_akhir_kontrak);

    @FormUrlEncoded
    @POST("progress/addprogressf/")
    Call<DataResponseProgress> addNewProgress(@Field("pa_id") String pa_id,  @Field("pr_target") String pr_target, @Field("pr_real") String pr_real, @Field("pr_deviasi") String pr_deviasi, @Field("pr_tanggal") String pr_tanggal,  @Field("ke_id") String ke_id);

    // role admin
    @GET("dashboardadmin/paguadmin/")
    Call<DataResponsePA> countPaguAdmin(@Query("dinas_id") String dinas_id);

    @GET("dashboardadmin/realadmin/")
    Call<DataResponsePA> countRealuAdmin(@Query("dinas_id") String dinas_id);

    @GET("dashboardadmin/sisaadmin/")
    Call<DataResponsePA> countSisaAdmin(@Query("dinas_id") String dinas_id);

    @GET("dashboardadmin/paketall/")
    Call<DataResponsePA> countTotalPaketAdmin(@Query("dinas_id") String dinas_id);

    // http://apiext.scafol.id/api/progress/getlastprogresspptk?pptk_id=317
    @GET("progress/getlastprogress")
    Call<DataResponseProgress> getlastProgressPPTK(@Query("pa_id") String pa_id);

    @GET("progress/getlastprogressall")
    Call<DataResponseProgress> getlastProgressallPPTK(@Query("pa_id") String pa_id);
}
