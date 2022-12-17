package com.mth.example.banhangapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mth.example.banhangapp.R;
import com.mth.example.banhangapp.adapter.DonHangAdapter;
import com.mth.example.banhangapp.model.DonHang;
import com.mth.example.banhangapp.model.SanPham;
import com.mth.example.banhangapp.model.User;
import com.mth.example.banhangapp.utils.ApiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LichSuActivity extends AppCompatActivity {
    private ImageButton butBack;
    private RecyclerView rcvLichSu;

    private DonHangAdapter donHangAdapter;
    private User user;
    private List<DonHang> donHangListServer;
    private Disposable disposable, disposable1, disposable2;
    private List<SanPham> sanPhamListServer;
    private List<User> userListServer;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su);
        anhXa();
        getInformation();
        setAction();
    }

    private void anhXa() {
        butBack = findViewById(R.id.but_lich_su_back);
        rcvLichSu = findViewById(R.id.rcv_lich_su);
        donHangListServer = new ArrayList<>();
        sanPhamListServer = new ArrayList<>();
        userListServer = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.txt_chochut));
    }

    private void getInformation() {
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("dulieu");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvLichSu.setLayoutManager(linearLayoutManager);
        rcvLichSu.addItemDecoration(dividerItemDecoration);
        donHangAdapter = new DonHangAdapter(this);
        progressDialog.show();
        ApiUtils.getData().getDataDonHang(user.getUsername())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<DonHang>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull List<DonHang> donHangs) {
                        donHangListServer = donHangs;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        progressDialog.dismiss();
                        Toast.makeText(LichSuActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                        Collections.sort(donHangListServer);
                        donHangAdapter.setData(donHangListServer);
                        rcvLichSu.setAdapter(donHangAdapter);
                    }
                });
    }

    private void setAction() {
        butBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        donHangAdapter.xuLyMuaLai(new DonHangAdapter.iClickMuaLaiListener() {
            @Override
            public void onClickMuaLai(int idsp, String username) {
                Bundle bundle = new Bundle();
                ApiUtils.getData().getSanPham()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<SanPham>>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                disposable1 = d;
                            }

                            @Override
                            public void onNext(@NonNull List<SanPham> sanPhams) {
                                sanPhamListServer = sanPhams;
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Toast.makeText(LichSuActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete() {
                                for (SanPham sanPham :
                                        sanPhamListServer) {
                                    if (sanPham.getId() == idsp) {
                                        bundle.putSerializable("dulieusanpham", sanPham);
                                    }
                                }
                                moveToChiTietSpActivity(bundle, username);
                            }
                        });

            }
        });
    }

    private void moveToChiTietSpActivity(Bundle bundle, String username) {
        ApiUtils.getData().getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<User>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable2 = d;
                    }

                    @Override
                    public void onNext(@NonNull List<User> users) {
                        userListServer = users;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(LichSuActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        for (User user :
                                userListServer) {
                            if (user.getUsername().trim().equals(username)) {
                                bundle.putSerializable("dulieuuser", user);
                                Intent intent = new Intent(LichSuActivity.this, ChiTietSanPhamActivity.class);
                                intent.putExtra("dulieu", bundle);
                                startActivity(intent);
                            }
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        if (disposable1 != null && disposable2 != null) {
            disposable1.dispose();
            disposable2.dispose();
        }
    }
}