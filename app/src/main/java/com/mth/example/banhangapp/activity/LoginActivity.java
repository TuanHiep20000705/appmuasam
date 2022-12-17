package com.mth.example.banhangapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mth.example.banhangapp.R;
import com.mth.example.banhangapp.model.User;
import com.mth.example.banhangapp.utils.ApiUtils;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    private ImageButton butBack;
    private EditText edtUser, edtPass;
    private Button butLogin, butRegister;
    private Disposable disposable;
    private List<User> listUserServer;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhXa();
        setAction();
    }

    private void anhXa() {
        butBack = findViewById(R.id.but_back_login);
        butLogin = findViewById(R.id.but_login);
        butRegister = findViewById(R.id.but_register);
        edtUser = findViewById(R.id.edt_taikhoan);
        edtPass = findViewById(R.id.edt_matkhau);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.txt_chochut));
    }

    private void setAction() {
        butBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        butRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        butLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtUser.getText().toString().trim();
                String password = edtPass.getText().toString().trim();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.txt_messagehaynhapdu), Toast.LENGTH_SHORT).show();
                } else {
                    login(email, password);
                }
            }
        });
    }

    private void login(String email, String password) {
        progressDialog.show();
        ApiUtils.getData().getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<User>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull List<User> users) {
                        listUserServer = users;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        int x = 0;
                        for (User user :
                                listUserServer) {
                            if (user.getUsername().equals(email) && user.getPassword().equals(password)) {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.txt_loginsuccess), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("dulieuuser",user);
                                bundle.putBoolean("tinhtrangdangnhap",true);
                                intent.putExtra("dulieu",bundle);
                                startActivity(intent);
                                break;
                            } else x++;
                        }
                        if (x == listUserServer.size()) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.txt_loginfail), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}