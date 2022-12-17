package com.mth.example.banhangapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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

public class RegisterActivity extends AppCompatActivity {
    private ImageButton butBack;
    private Button butRegister;
    private EditText edtUserName, edtEmail, edtPass, edtConfirmPass, edtAddress, edtPhone;
    private Disposable disposable, disposable1;
    private List<User> listUserServer;
    private String response = "";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        anhXa();
        setAction();
    }

    private void anhXa() {
        butBack = findViewById(R.id.but_back_register);
        butRegister = findViewById(R.id.but_dangky_register);
        edtUserName = findViewById(R.id.edt_name_register);
        edtEmail = findViewById(R.id.edt_email_register);
        edtPass = findViewById(R.id.edt_matkhau_register);
        edtConfirmPass = findViewById(R.id.edt_matkhau_xacnhan_register);
        edtAddress = findViewById(R.id.edt_address_register);
        edtPhone = findViewById(R.id.edt_phone_register);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.txt_chochut));
    }

    private void setAction() {
        butBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        butRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPass.getText().toString().trim();
                String userName = edtUserName.getText().toString().trim();
                String address = edtAddress.getText().toString().trim();
                String confirmPassword = edtConfirmPass.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                progressDialog.show();
                if (email.isEmpty() || password.isEmpty() || userName.isEmpty() || address.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.txt_messagehaynhapdu), Toast.LENGTH_SHORT).show();
                } else {

                    if (isValidEmail(email) && isValidPassword(password)) {

                        if (isCorrectPassword(password, confirmPassword)) {
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
                                            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onComplete() {
                                            int x = 0;
                                            for (User user :
                                                    listUserServer) {
                                                if (user.getUsername().equals(email)) {
                                                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.txt_emailexist), Toast.LENGTH_SHORT).show();
                                                    break;
                                                } else x++;
                                            }
                                            if (x == listUserServer.size()) {
                                                registerUser(email, password, userName, address, phone);
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.txt_messagexacnhanpass), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(RegisterActivity.this, getResources().getString(R.string.txt_messagevalid), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void registerUser(String email, String password, String userName, String address, String phone) {
        int sdt = Integer.parseInt(phone);
        ApiUtils.getData().registerUser(email, password, userName, address, sdt)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable1 = d;
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        response = s;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        if (response.trim().equals("success")) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.txt_registersuccess), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        disposable1.dispose();
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 6;
    }

    private boolean isCorrectPassword(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
}