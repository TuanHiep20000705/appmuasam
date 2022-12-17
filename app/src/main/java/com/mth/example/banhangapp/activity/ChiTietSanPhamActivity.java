package com.mth.example.banhangapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mth.example.banhangapp.R;
import com.mth.example.banhangapp.model.GioHang;
import com.mth.example.banhangapp.model.SanPham;
import com.mth.example.banhangapp.model.User;
import com.mth.example.banhangapp.utils.ApiUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChiTietSanPhamActivity extends AppCompatActivity {
    private ImageView imgHinh;
    private TextView txtTen, txtGia, txtmoTa;
    private Spinner spinner;
    private ImageButton butBack, butCart;
    private Button butThem;

    private SanPham sanPham;
    private User user;
    private List<GioHang> listGioHangServer;
    private Disposable disposable, disposable1, disposable2, disposable3;
    private String response = "", response1 = "";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);
        anhXa();
        getInformation();
        actionToolBar();
        catchEventSpinner();
        catchEventThemGioHang();
        catchEventGioHang();
    }

    private void getInformation() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("dulieu");
        sanPham = (SanPham) bundle.getSerializable("dulieusanpham");
        user = (User) bundle.getSerializable("dulieuuser");
        DecimalFormat dcf = new DecimalFormat("#,###");
        txtTen.setText(sanPham.getTensp());
        Glide.with(this).load(sanPham.getAnh()).into(imgHinh);
        txtGia.setText(getResources().getString(R.string.txt_gia) + " " + dcf.format(sanPham.getGiasp()) + " " + getResources().getString(R.string.txt_tiente));
        txtmoTa.setText(sanPham.getMota());
    }

    private void actionToolBar() {
        butBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void anhXa() {
        imgHinh = findViewById(R.id.img_hinh_info_sp);
        txtTen = findViewById(R.id.txt_ten_info_sp);
        txtGia = findViewById(R.id.txt_gia_info_sp);
        txtmoTa = findViewById(R.id.txt_thong_tin_info_sp);
        spinner = findViewById(R.id.spinner_so_luong_info_sp);
        butBack = findViewById(R.id.but_info_sp_back);
        butCart = findViewById(R.id.but_info_sp_cart);
        butThem = findViewById(R.id.but_info_them_gio_hang);
        listGioHangServer = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.txt_dangkiemtra));
    }

    private void catchEventSpinner() {
        Integer[] soLuongSpin = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, soLuongSpin);
        spinner.setAdapter(arrayAdapter);
    }

    private void catchEventThemGioHang() {
        butThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int soLuong = Integer.parseInt(spinner.getSelectedItem().toString());

                if (user == null) {
                    Toast.makeText(ChiTietSanPhamActivity.this, getResources().getString(R.string.txt_messagedangnhap), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ChiTietSanPhamActivity.this, LoginActivity.class));
                } else {
                    progressDialog.show();
                    ApiUtils.getData().getDataCart(user.getUsername())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<List<GioHang>>() {
                                @Override
                                public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                    disposable = d;
                                }

                                @Override
                                public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<GioHang> gioHangs) {
                                    listGioHangServer = gioHangs;
                                }

                                @Override
                                public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                    Toast.makeText(ChiTietSanPhamActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete() {
                                    int x = 0;
                                    for (GioHang gioHang :
                                            listGioHangServer) {
                                        if (gioHang.getIdsp() == sanPham.getId()) {
                                            if (gioHang.getSoluong() == 10) {
                                                progressDialog.dismiss();
                                                Toast.makeText(ChiTietSanPhamActivity.this, getResources().getString(R.string.txt_messagesoluongmax), Toast.LENGTH_SHORT).show();
                                            } else {
                                                int soLuongMoi = gioHang.getSoluong() + soLuong;
                                                if (soLuongMoi <= 10) {
                                                    int tongGia = sanPham.getGiasp() * soLuongMoi;
                                                    updateSoLuong(user.getUsername(), sanPham.getId(), soLuongMoi, tongGia, sanPham.getTensp());
                                                } else {
                                                    int tongGia = sanPham.getGiasp() * 10;
                                                    updateSoLuong(user.getUsername(), sanPham.getId(), 10, tongGia, sanPham.getTensp());
                                                }
                                            }
                                        } else x++;
                                    }
                                    if (x == listGioHangServer.size()) {
                                        createCart(user.getUsername(), sanPham.getId(), soLuong, sanPham.getGiasp() * soLuong, sanPham.getTensp(), sanPham.getAnh(), sanPham.getGiasp());
                                    }
                                }
                            });
                }
            }
        });
    }

    private void catchEventGioHang() {
        butCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChiTietSanPhamActivity.this, MainActivity.class);
                intent.putExtra("dulieufromsanphaminfo", user);
                startActivity(intent);
            }
        });
    }

    private void createCart(String username, int idsp, int soLuong, int tongGia, String tenSp, String anhSp, int donGia) {
        ApiUtils.getData().createCart(username, idsp, soLuong, tongGia, tenSp, anhSp, donGia)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable1 = d;
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull String s) {
                        response = s;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();
                        Toast.makeText(ChiTietSanPhamActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        if (response.trim().equals("success")) {
                            createThongBao(username, tenSp);
                        }
                    }
                });
    }

    private void updateSoLuong(String username, int idSp, int soLuong, int tongGia, String tenSp) {
        ApiUtils.getData().updateSoLuong(idSp, username, soLuong, tongGia)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable2 = d;
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull String s) {
                        response1 = s;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();
                        Toast.makeText(ChiTietSanPhamActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        if (response1.trim().equals("success")) {
                            createThongBao(username, tenSp);
                        }
                    }
                });
    }

    private void createThongBao(String username, String tenSp) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy   HH:mm");
        Date t = cal.getTime();
        String noiDung = tenSp + " " + getResources().getString(R.string.txt_messagethemsp);
        ApiUtils.getData().createThongBao(username, sdf.format(t), noiDung, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable3 = d;
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull String s) {

                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();
                        Toast.makeText(ChiTietSanPhamActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                        Toast.makeText(ChiTietSanPhamActivity.this, getResources().getString(R.string.txt_messagethemsp1), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
        if (disposable1 != null) {
            disposable1.dispose();
        }
        if (disposable2 != null) {
            disposable2.dispose();
        }
        if (disposable3 != null) {
            disposable3.dispose();
        }
    }
}