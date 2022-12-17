package com.mth.example.banhangapp.retrofit;

import com.mth.example.banhangapp.model.DonHang;
import com.mth.example.banhangapp.model.GioHang;
import com.mth.example.banhangapp.model.LoaiSanPham;
import com.mth.example.banhangapp.model.QuangCao;
import com.mth.example.banhangapp.model.SanPham;
import com.mth.example.banhangapp.model.ThongBao;
import com.mth.example.banhangapp.model.User;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface DataClient {
    @GET("getdataquangcao.php")
    Observable<List<QuangCao>> getQuangCao();

    @GET("getdataloaisp.php")
    Observable<List<LoaiSanPham>> getLoaiSanPham();

    @GET("getdatasp.php")
    Observable<List<SanPham>> getSanPham();

    @FormUrlEncoded
    @POST("getdanhmucsp.php")
    Observable<List<SanPham>> getSanPhamDanhMuc(@Field("idloai") int loai);

    @GET("getdatauser.php")
    Observable<List<User>> getUser();

    @FormUrlEncoded
    @POST("insertuser.php")
    Observable<String> registerUser(@Field("username") String email,
                                    @Field("password") String password,
                                    @Field("tenuser") String tenuser,
                                    @Field("diachi") String address,
                                    @Field("sdt") int sdt);

    @FormUrlEncoded
    @POST("getdatauserbyid.php")
    Observable<List<User>> getUserById(@Field("id") int id);

    @FormUrlEncoded
    @POST("getdatauserbyusername.php")
    Observable<List<User>> getUserByUsername(@Field("username") String username);

    @Multipart
    @POST("uploadimage.php")
    Observable<String> uploadPhoto(@Part MultipartBody.Part photo);

    @FormUrlEncoded
    @POST("updateuser.php")
    Observable<String> updateUser(@Field("id") int id,
                                  @Field("tenuser") String tenUser,
                                  @Field("diachi") String diaChi,
                                  @Field("sdt") int sdt,
                                  @Field("anh") String anh);

    @FormUrlEncoded
    @POST("updatelanmuauser.php")
    Observable<String> updateLanMuaUser(@Field("username") String username,
                                        @Field("damua") int damua);

    @FormUrlEncoded
    @POST("updateuserwithoutimg.php")
    Observable<String> updateUserWithoutImg(@Field("id") int id,
                                            @Field("tenuser") String tenUser,
                                            @Field("diachi") String diaChi,
                                            @Field("sdt") int sdt);


    @FormUrlEncoded
    @POST("getdatacart.php")
    Observable<List<GioHang>> getDataCart(@Field("username") String username);

    @FormUrlEncoded
    @POST("updatesoluongsp.php")
    Observable<String> updateSoLuong(@Field("idsp") int idsp,
                                     @Field("username") String username,
                                     @Field("soluong") int soluong,
                                     @Field("tonggia") int tonggia);

    @FormUrlEncoded
    @POST("createcartbyidsp.php")
    Observable<String> createCartById(@Field("username") String username,
                                      @Field("idsp") int idsp,
                                      @Field("tonggia") int tonggia,
                                      @Field("tensp") String tensp,
                                      @Field("anhsp") String anhsp,
                                      @Field("dongia") int dongia);

    @FormUrlEncoded
    @POST("createcart.php")
    Observable<String> createCart(@Field("username") String username,
                                  @Field("idsp") int idsp,
                                  @Field("soluong") int soluong,
                                  @Field("tonggia") int tonggia,
                                  @Field("tensp") String tensp,
                                  @Field("anhsp") String anhsp,
                                  @Field("dongia") int dongia);

    @FormUrlEncoded
    @POST("deletegiohang.php")
    Observable<String> deleteGioHang(@Field("idsp") int idsp,
                                     @Field("username") String username);

    @FormUrlEncoded
    @POST("createdonhang.php")
    Observable<String> createDonHang(@Field("username") String username,
                                     @Field("idsp") int idsp,
                                     @Field("soluong") int soluong,
                                     @Field("tonggia") int tonggia,
                                     @Field("tensp") String tensp,
                                     @Field("date") String date,
                                     @Field("dongia") int dongia,
                                     @Field("anhsp") String anhsp);

    @FormUrlEncoded
    @POST("updatesolanmua.php")
    Observable<String> updateSoLanMua(@Field("idsp") int idsp,
                                      @Field("soluongmua") int soluongmua);

    @FormUrlEncoded
    @POST("getdatadonhang.php")
    Observable<List<DonHang>> getDataDonHang(@Field("username") String username);

    @FormUrlEncoded
    @POST("deletegiohangbyusername.php")
    Observable<String> deleteGioHangByUsername(@Field("username") String username);

    @FormUrlEncoded
    @POST("createthongbao.php")
    Observable<String> createThongBao(@Field("username") String username,
                                      @Field("date") String date,
                                      @Field("noidung") String noidung,
                                      @Field("loai") int loai);

    @FormUrlEncoded
    @POST("getdatathongbao.php")
    Observable<List<ThongBao>> getDataThongBao(@Field("username") String username);
}
