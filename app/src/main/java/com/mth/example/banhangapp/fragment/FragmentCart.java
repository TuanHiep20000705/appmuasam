package com.mth.example.banhangapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mth.example.banhangapp.R;
import com.mth.example.banhangapp.activity.MainActivity;
import com.mth.example.banhangapp.activity.ThanhToanActivity;
import com.mth.example.banhangapp.adapter.GioHangAdapter;
import com.mth.example.banhangapp.model.GioHang;
import com.mth.example.banhangapp.model.User;
import com.mth.example.banhangapp.utils.ApiUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FragmentCart extends Fragment {
    private View view;
    private RecyclerView rcvGioHang;
    private TextView txtTongGia;
    private Button butMuaHang;

    private GioHangAdapter gioHangAdapter;
    private User user;
    private Disposable disposable, disposable1, disposable2, disposable3;
    private List<GioHang> gioHangListServer;
    private int tongTienMoi = 0, tongTienCu = 0;
    private String response = "", response1 = "";
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        anhXa();
        setRcv();
        getListGioHang();
        xuLyThemBotSp();
        xuLyXoaSp();
        setAction();
        return view;
    }

    private void xuLyXoaSp() {
        gioHangAdapter.xuLyDelete(new GioHangAdapter.iClickDeleteListener() {
            @Override
            public void onClickDelete(int idsp, int donGia, int soLuong) {
                tongTienMoi = tongTienCu - (donGia * soLuong);
                DecimalFormat dcf = new DecimalFormat("#,###");
                txtTongGia.setText("đ" + dcf.format(tongTienMoi));
                ApiUtils.getData().deleteGioHang(idsp, user.getUsername())
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
                                Toast.makeText(getActivity(), getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                tongTienCu = tongTienMoi;
            }
        });
    }

    private void xuLyThemBotSp() {
        gioHangAdapter.xuLyCongTongTien(new GioHangAdapter.iClickPlusListener() {
            @Override
            public void onClickPlus(int donGia, int soLuong, int idSp) {
                tongTienMoi = tongTienCu + donGia;
                DecimalFormat dcf = new DecimalFormat("#,###");
                txtTongGia.setText("đ" + dcf.format(tongTienMoi));

                ApiUtils.getData().updateSoLuong(idSp, user.getUsername(), soLuong, tongTienMoi)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                disposable1 = d;
                            }

                            @Override
                            public void onNext(@io.reactivex.rxjava3.annotations.NonNull String s) {

                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });

                tongTienCu = tongTienMoi;
            }
        });

        gioHangAdapter.xuLyTruTongTien(new GioHangAdapter.iClickMinusListener() {
            @Override
            public void onClickMinus(int donGia, int soLuong, int idSp) {
                tongTienMoi = tongTienCu - donGia;
                DecimalFormat dcf = new DecimalFormat("#,###");
                txtTongGia.setText("đ" + dcf.format(tongTienMoi));

                ApiUtils.getData().updateSoLuong(idSp, user.getUsername(), soLuong, tongTienMoi)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                disposable2 = d;
                            }

                            @Override
                            public void onNext(@io.reactivex.rxjava3.annotations.NonNull String s) {

                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });

                tongTienCu = tongTienMoi;
            }
        });
    }

    private void getListGioHang() {
        Bundle bundle = getArguments();
        user = (User) bundle.getSerializable("dulieu");
        if (user != null) {
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
                            gioHangListServer = gioHangs;
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
                            progressDialog.dismiss();
                            gioHangAdapter.setData(gioHangListServer);
                            rcvGioHang.setAdapter(gioHangAdapter);

                            DecimalFormat dcf = new DecimalFormat("#,###");
                            for (GioHang gioHang :
                                    gioHangListServer) {
                                tongTienCu += gioHang.getDongia() * gioHang.getSoluong();
                                txtTongGia.setText(getResources().getString(R.string.txt_donvi) + dcf.format(tongTienCu));
                            }
                        }
                    });
        } else {
            gioHangAdapter = new GioHangAdapter(getActivity());
            gioHangAdapter.setData(getListNullGioHang());
            rcvGioHang.setAdapter(gioHangAdapter);
            Toast.makeText(getActivity(), getResources().getString(R.string.txt_messagedangnhap), Toast.LENGTH_SHORT).show();
        }

    }

    private void setRcv() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcvGioHang.setLayoutManager(linearLayoutManager);
        gioHangAdapter = new GioHangAdapter(getActivity());
    }

    private List<GioHang> getListNullGioHang() {
        List<GioHang> list = new ArrayList<>();
        return list;
    }

    private void anhXa() {
        rcvGioHang = view.findViewById(R.id.rcv_gio_hang_cart);
        txtTongGia = view.findViewById(R.id.txt_tong_gia_cart);
        butMuaHang = view.findViewById(R.id.but_mua_hang_cart);
        gioHangListServer = new ArrayList<>();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getResources().getString(R.string.txt_chochut));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null && disposable1 != null && disposable2 != null && disposable3 != null) {
            disposable.dispose();
            disposable1.dispose();
            disposable2.dispose();
            disposable3.dispose();
        } else return;
    }

    private void setAction() {
        butMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gioHangListServer.size() == 0) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.txt_chuacosp), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), ThanhToanActivity.class);
                    intent.putExtra("dulieu", user);
                    startActivity(intent);
                }
            }
        });
    }
}
