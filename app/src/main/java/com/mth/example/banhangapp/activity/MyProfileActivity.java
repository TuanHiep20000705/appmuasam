package com.mth.example.banhangapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mth.example.banhangapp.R;
import com.mth.example.banhangapp.model.User;
import com.mth.example.banhangapp.utils.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MyProfileActivity extends AppCompatActivity {
    private ImageView imgAvatar;
    private TextView txtTen, txtEmail, txtDiaChi, txtDaMua, txtSdt;
    private Button butSua, butLogout;
    private ImageButton butBack;

    private Disposable disposable;
    private User user;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        anhXa();
        getInformation();
        setAction();
    }

    private void anhXa() {
        imgAvatar = findViewById(R.id.img_hinh_profile_avatar);
        txtTen = findViewById(R.id.txt_ten_profile);
        txtEmail = findViewById(R.id.txt_email_profile);
        txtDaMua = findViewById(R.id.txt_damua_profile);
        txtDiaChi = findViewById(R.id.txt_diachi_profile);
        butBack = findViewById(R.id.but_back_profile);
        butSua = findViewById(R.id.but_sua_profile);
        butLogout = findViewById(R.id.but_logout_profile);
        txtSdt = findViewById(R.id.txt_sdt_profile);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.txt_chochut));
    }

    private void getInformation() {
        Intent intent = getIntent();
        int idUser = intent.getIntExtra("dulieu", 1);
        progressDialog.show();
        ApiUtils.getData().getUserById(idUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<User>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull List<User> users) {
                        user = users.get(0);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        progressDialog.dismiss();
                        Toast.makeText(MyProfileActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                        txtTen.setText(getResources().getString(R.string.txt_name_profile) + " " + user.getTenuser());
                        txtEmail.setText(getResources().getString(R.string.txt_email_profile) + " " + user.getUsername());
                        txtDiaChi.setText(getResources().getString(R.string.txt_address_profile) + " " + user.getDiachi());
                        txtSdt.setText(getResources().getString(R.string.txt_sdt_profile) + " " + user.getSdt());
                        txtDaMua.setText(getResources().getString(R.string.txt_damua_profile) + " " + user.getDamua() + " " + getResources().getString(R.string.txt_sanpham));
                        if (user.getAnh().length() > 0) {
                            Glide.with(MyProfileActivity.this).load(user.getAnh()).into(imgAvatar);
                        } else {
                            return;
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    private void setAction() {
        butBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfileActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("dulieuuser", user);
                bundle.putBoolean("tinhtrangdangnhap", true);
                intent.putExtra("dulieu", bundle);
                startActivity(intent);
            }
        });

        butSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfileActivity.this, FixProfileActivity.class);
                intent.putExtra("dulieu", user);
                startActivity(intent);
            }
        });
        butLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfileActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("dulieuuser", user);
                bundle.putBoolean("tinhtrangdangnhap", false);
                intent.putExtra("dulieu", bundle);
                startActivity(intent);
            }
        });
    }
}