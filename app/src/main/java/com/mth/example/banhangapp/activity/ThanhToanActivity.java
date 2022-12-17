package com.mth.example.banhangapp.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mth.example.banhangapp.R;
import com.mth.example.banhangapp.adapter.ThanhToanAdapter;
import com.mth.example.banhangapp.model.GioHang;
import com.mth.example.banhangapp.model.User;
import com.mth.example.banhangapp.utils.ApiUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToanActivity extends AppCompatActivity {
    private ImageButton butBack;
    private TextView txtTen, txtSdt, txtDiaChi, txtPhiShip, txtTongTien;
    private RecyclerView rcvThanhToan;
    private Button butDatHang;

    private ThanhToanAdapter thanhToanAdapter;
    private Disposable disposable, disposable1, disposable2, disposable3, disposable4, disposable5, disposable6;
    private List<GioHang> gioHangListServer;
    private User user;
    int tongDonMuaThem = 0;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        anhXa();
        getInformation();
        setAction();
    }

    private void anhXa() {
        butBack = findViewById(R.id.but_thanh_toan_back);
        txtTen = findViewById(R.id.txt_ten_thanh_toan);
        txtSdt = findViewById(R.id.txt_sdt_thanh_toan);
        txtDiaChi = findViewById(R.id.txt_dia_chi_thanh_toan);
        txtPhiShip = findViewById(R.id.txt_phi_ship);
        txtTongTien = findViewById(R.id.txt_tong_gia_thanh_toan);
        rcvThanhToan = findViewById(R.id.rcv_thanh_toan);
        butDatHang = findViewById(R.id.but_dat_hang_thanh_toan);
        gioHangListServer = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.txt_chochut));
    }

    private void getInformation() {
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("dulieu");
        txtTen.setText(user.getTenuser());
        txtSdt.setText(user.getSdt() + "");
        txtDiaChi.setText(user.getDiachi());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvThanhToan.setLayoutManager(linearLayoutManager);
        thanhToanAdapter = new ThanhToanAdapter(this);
        progressDialog.show();
        ApiUtils.getData().getDataCart(user.getUsername())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<GioHang>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull List<GioHang> gioHangs) {
                        gioHangListServer = gioHangs;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        progressDialog.dismiss();
                        Toast.makeText(ThanhToanActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                        thanhToanAdapter.setData(gioHangListServer);
                        rcvThanhToan.setAdapter(thanhToanAdapter);

                        int soLuongMatHang = gioHangListServer.size();
                        txtPhiShip.setText(getResources().getString(R.string.txt_phiship) + " " + getResources().getString(R.string.txt_15000) + " x" + soLuongMatHang);

                        DecimalFormat dcf = new DecimalFormat("#,###");
                        int tongTien = 15000 * soLuongMatHang;
                        for (GioHang gioHang :
                                gioHangListServer) {
                            tongTien += gioHang.getTonggia();
                        }
                        txtTongTien.setText(getResources().getString(R.string.txt_donvi) + dcf.format(tongTien));

                        onClickDatHang(gioHangListServer);
                    }
                });
    }

    private void onClickDatHang(List<GioHang> gioHangList) {
        butDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                Date t = cal.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy   HH:mm");
                for (GioHang gioHang :
                        gioHangList) {
                    ApiUtils.getData().createDonHang(gioHang.getUsername(), gioHang.getIdsp(), gioHang.getSoluong(),
                                    gioHang.getTonggia(), gioHang.getTensp(), sdf.format(t), gioHang.getDongia(), gioHang.getAnhsp())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<String>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {
                                    disposable1 = d;
                                }

                                @Override
                                public void onNext(@NonNull String s) {

                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    Toast.makeText(ThanhToanActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete() {
                                    updateSoLanMua(gioHang.getIdsp(), gioHang.getSoluong());
                                }
                            });

                    String noiDung = gioHang.getTensp() + " " + getResources().getString(R.string.txt_daduocdat);
                    ApiUtils.getData().createThongBao(gioHang.getUsername(), sdf1.format(t), noiDung, 2)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<String>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {
                                    disposable4 = d;
                                }

                                @Override
                                public void onNext(@NonNull String s) {

                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    Toast.makeText(ThanhToanActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete() {

                                }
                            });

                    tongDonMuaThem += gioHang.getSoluong();
                }

                ApiUtils.getData().getUserByUsername(gioHangList.get(0).getUsername())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<User>>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                disposable5 = d;
                            }

                            @Override
                            public void onNext(@NonNull List<User> users) {
                                user = users.get(0);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Toast.makeText(ThanhToanActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete() {
                                int userDaMua = user.getDamua() + tongDonMuaThem;
                                updateUserDaMua(userDaMua, user.getUsername());
                            }
                        });

                ApiUtils.getData().deleteGioHangByUsername(gioHangList.get(0).getUsername())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                disposable2 = d;
                            }

                            @Override
                            public void onNext(@NonNull String s) {

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Toast.makeText(ThanhToanActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ThanhToanActivity.this);
                alertDialog.setTitle(getResources().getString(R.string.txt_dathangthanhcong));
                alertDialog.setIcon(R.drawable.icon_shoppingbag);
                alertDialog.setMessage(getResources().getString(R.string.txt_ghinhandonhang));
                alertDialog.setPositiveButton(getResources().getString(R.string.but_tieptucmua), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(ThanhToanActivity.this, MainActivity.class));
                    }
                });
                alertDialog.show();
            }
        });

    }

    private void updateUserDaMua(int soLanMua, String username) {
        ApiUtils.getData().updateLanMuaUser(username, soLanMua)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable6 = d;
                    }

                    @Override
                    public void onNext(@NonNull String s) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(ThanhToanActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void updateSoLanMua(int idsp, int soluong) {
        ApiUtils.getData().updateSoLanMua(idsp, soluong)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable3 = d;
                    }

                    @Override
                    public void onNext(@NonNull String s) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(ThanhToanActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

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
        if (disposable4 != null) {
            disposable4.dispose();
        }
        if (disposable5 != null) {
            disposable5.dispose();
        }
        if (disposable6 != null) {
            disposable6.dispose();
        }
    }

    private void setAction() {
        butBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

}