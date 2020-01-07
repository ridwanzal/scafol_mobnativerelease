package com.release.restapi;

import com.release.model.DataResponse;
import com.release.model.DataResponseAnggaran;
import com.release.model.DataResponseBidang;
import com.release.model.DataResponseCatatan;
import com.release.model.DataResponseDinas;
import com.release.model.DataResponseInfo;
import com.release.model.DataResponseKegiatanAnggaran;
import com.release.model.DataResponsePA;
import com.release.model.DataResponseKegiatan;
import com.release.model.DataResponsePAS;
import com.release.model.DataResponsePJ;
import com.release.model.DataResponsePaket;
import com.release.model.DataResponseProgress;
import com.release.model.DataResponseRencana;
import com.release.model.DataResponseSerapan;
import com.release.model.DataResponseToken;
import com.release.model.DataResponseUsers;

import retrofit2.Call;

import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    // dinas by user
    @GET("dinas/user")
    Call<DataResponseDinas> getDinas(@Query("user_id") String user_id);

    // get dinas detail by dinas
    @GET("dinas/dinas")
    Call<DataResponseDinas> getDinasDetail(@Query("dinas_id") String dinas_id);

    // bidang
    @GET("bidang/bidang")
    Call<DataResponseBidang> getBidang(@Query("ke_id") String ke_id);

    // main retrieve

    @GET("users/")
    Call<DataResponse> getUserById(@Query("user_id") String user_id);

    @GET("users/uname")
    Call<DataResponseUsers> getUserByName(@Query("username") String username);

    @GET("users/kontraktorall")
    Call<DataResponseUsers> getKontrak();

    @FormUrlEncoded
    @POST("users/updateprofile")
    Call<DataResponseUsers> updateProfile(@Field("user_id") String user_id,  @Field("nama") String nama, @Field("email") String email, @Field("telephone") String telephone);

    @GET("kegiatan")
    Call<DataResponseKegiatan> getKegiatan(@Query("ke_id") String ke_id);

    @GET("kegiatan/")
    Call<DataResponseKegiatan> getKegiatanAdmin();

    @GET("kegiatan/paket")
    Call<DataResponseKegiatan> getKegiatanTree(@Query("dinas_id") String dinas_id);

    @GET("kegiatan/anggaran")
    Call<DataResponseKegiatanAnggaran> getKegiatanTreeAnggaran(@Query("dinas_id") String dinas_id);

    @GET("kegiatan/paket")
    Call<DataResponseKegiatan> getKegiatanTreePPTK(@Query("dinas_id") String dinas_id, @Query("pptk_id") String pptk_id);

    @GET("kegiatan/anggaran")
    Call<DataResponseKegiatanAnggaran> getKegiatanTreeAnggaranPPTK(@Query("dinas_id") String dinas_id, @Query("pptk_id") String pptk_id);

    @GET("kegiatan/paket")
    Call<DataResponseKegiatan> getKegiatanTreeBidang(@Query("dinas_id") String dinas_id, @Query("bi_id") String bi_id);

    @GET("kegiatan/anggaran")
    Call<DataResponseKegiatanAnggaran> getKegiatanTreeAnggaranBidang(@Query("dinas_id") String dinas_id, @Query("bi_id") String bi_id);

    @GET("kegiatan/dinas")
    Call<DataResponseKegiatan> getKegiatanAdminDinas(@Query("dinas_id") String dinas_id);

    @GET("paket/pptk")
    Call<DataResponsePaket> getPaketPptk(@Query("pptk_id") String pptk_id);

    @GET("paket/mapprogmaxpptk")
    Call<DataResponsePaket> getMapPPTK(@Query("pptk_id") String pptk_id);

    @GET("paket/mapprogmax")
    Call<DataResponsePaket> getMapDinas(@Query("dinas_id") String dinas_id);

    @GET("paket/mapprogmaxbidang")
    Call<DataResponsePaket> getMapBidang(@Query("bi_id") String bi_id);

    @GET("paket/dinas")
    Call<DataResponsePaket> getPaketDinas(@Query("dinas_id") String dinas_id);

    @GET("dashboardbidang")
    Call<DataResponsePA>infoPaketBidang(@Query("bi_id") String bi_id);

    @GET("paket/bidang")
    Call<DataResponsePaket>getPaketBidang(@Query("bi_id") String bi_id, @Query("dinas_id") String dinas_id);

    @GET("paket/bidangpaket")
    Call<DataResponsePaket>getPaketBidangId(@Query("bi_id") String bi_id);

    @GET("paket/catatan")
    Call<DataResponseCatatan> getCatatan(@Query("pa_id") String pa_id);

    @FormUrlEncoded
    @POST("paket/catatan_delete")
    Call<DataResponseCatatan> removeCatatan(@Field("ca_id") String ca_id);

    @GET("anggaran/pptk")
    Call<DataResponseAnggaran> getAnggaranPPTK(@Query("pptk_id") String pptk_id);

    @GET("anggaran/bidang")
    Call<DataResponseAnggaran> getAnggaranBidang(@Query("bi_id") String bi_id, @Query("dinas_id") String dinas_id);

    @GET("anggaran/dinas")
    Call<DataResponseAnggaran> getAnggaranAdmin(@Query("dinas_id") String dinas_id);

    @GET("anggaran/anggaran")
    Call<DataResponseAnggaran> getAnggaran(@Query("an_id") String an_id);

    @GET("anggaran/serapan")
    Call<DataResponseSerapan> getSerapan(@Query("an_id") String an_id);

    @GET("anggaran/serapan_sum")
    Call<DataResponseSerapan> getSerapanSum(@Query("an_id") String an_id);

    @FormUrlEncoded
    @POST("anggaran/serapan/")
    Call<DataResponseSerapan> addSerapan(@Field("an_id") String an_id,  @Field("se_daya_serap") String se_daya_serap, @Field("se_sisa") String se_sisa, @Field("se_tanggal") String se_tanggal,  @Field("se_keterangan") String se_keterangan,  @Field("ke_id") String ke_id);

    @FormUrlEncoded
    @POST("anggaran/anggaran_kontrak")
    Call<DataResponseAnggaran> editAnggaran(@Field("an_id") String an_id,  @Field("an_nomor_kontrak") String an_nomor_kontrak, @Field("an_nilai_kontrak") String an_nilai_kontrak, @Field("an_awal_kontrak") String an_awal_kontrak, @Field("an_akhir_kontrak") String an_akhir_kontrak);

    @FormUrlEncoded
    @POST("paket/submit_catatan")
    Call<DataResponseCatatan> addCatatan(@Field("pa_id") String pa_id,  @Field("ca_catatan") String ca_catatan, @Field("date_created") String date_created, @Field("date_updated") String date_updated);

    @GET("paket/")
    Call<DataResponsePaket> getPaketId(@Query("pa_id") String pa_id);

    @GET("progress/fisik")
    Call<DataResponseProgress> getProgressByPaket(@Query("pa_id") String pa_id);

    @GET("progress/progresslast")
    Call<DataResponseProgress> getProgressFisikLast(@Query("pa_id") String pa_id);

    @GET("progress/keuangan")
    Call<DataResponseProgress> getProgressByPaketKeuangan(@Query("pa_id") String pa_id);

    @GET("kegiatan/bidang/")
    Call<DataResponseKegiatan> getKegiatanBidang(@Query("bi_id") String bi_id);

    @GET("login")
    Call<DataResponse> checkLogin(@Query("username") String username, @Query("password") String password);

    @GET("dashboardsuper/total_pagu_daerah")
    Call<DataResponsePAS> infoPaketSuper(@Query("daerah_id") String daerah_id);

    @GET("dashboardsuper/total_paket_daerah")
    Call<DataResponsePAS> infoPaketDetailSuper(@Query("daerah_id") String daerah_id);

    @GET("dashboardsuper/instansi")
    Call<DataResponseDinas> getDinasSuper(@Query("daerah_id") String daerah_id);

    // role pptk
    @GET("dashboardadmin")
    Call<DataResponsePA> infoPaketAdmin(@Query("dinas_id") String dinas_id);

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
    Call<DataResponsePaket> updateMap(@Path("pa_id") String pa_id,  @Field("pa_lokasi") String pa_lokasi, @Field("pa_loc_latitude") String pa_loc_latitude, @Field("pa_loc_longitude") String pa_loc_longitude);

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

    // role bidang
    @GET("dashboardbidang/pagubidang/")
    Call<DataResponsePA> countPaguBidang(@Query("bi_id") String bi_id);

    @GET("dashboardbidang/realbidang/")
    Call<DataResponsePA> countRealBidang(@Query("bi_id") String bi_id);

    @GET("dashboardbidang/sisabidang/")
    Call<DataResponsePA> countSisaBidang(@Query("bi_id") String bi_id);

    @GET("progress/getlastprogress")
    Call<DataResponseProgress> getlastProgressPPTK(@Query("pa_id") String pa_id);

    @GET("progress/getlastprogressall")
    Call<DataResponseProgress> getlastProgressallPPTK(@Query("pa_id") String pa_id);

    @GET("progress/prog_daya_serap")
    Call<DataResponseProgress> getProgressByPaketKeuanganSerap(@Query("pa_id") String pa_id);

    @FormUrlEncoded
    @POST("progress/progressk/")
    Call<DataResponseProgress> addNewProgressKeuangan(@Field("pa_id") String pa_id,  @Field("pr_daya_serap_kontrak") String pr_daya_serap_kontrak, @Field("pr_keterangan") String pr_keterangan, @Field("pr_tanggal") String pr_tanggal,  @Field("ke_id") String ke_id);

    // kurva s rencana
    @GET("kurva_s")
    Call<DataResponseRencana> getKurvaS(@Query("pa_id") String pa_id);

    @FormUrlEncoded
    @POST("kurva_s/submit_rencana/")
    Call<DataResponseRencana> addNewRencana(@Field("pa_id") String pa_id,  @Field("re_tanggal") String re_tanggal,  @Field("re_progress") String re_progress);

    @FormUrlEncoded
    @POST("kurva_s/delete_rencana")
    Call<DataResponseRencana> removeKurvaS(@Field("re_id") String re_id);

    @GET("token")
    Call<DataResponseToken> getRemoteToken();

    @GET("info")
    Call<DataResponseInfo> getNotificationInfo();

    @GET("penyediajasa/dinas")
    Call<DataResponsePJ> getPenyediaJasa(@Query("dinas_id") String dinas_id);


    @GET("penyediajasa/penyedia_bypaket")
    Call<DataResponsePJ> getPenyediaJasaByPaket(@Query("pa_id") String pa_id);

    @FormUrlEncoded
    @POST("penyediajasa/submit_penyedia_backup")
    Call<DataResponsePJ> submitPenyedia(@Field("pa_id") String pa_id,  @Field("ko_nama") String ko_nama);
}
