package com.mth.example.banhangapp.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mth.example.banhangapp.R;
import com.mth.example.banhangapp.model.User;
import com.mth.example.banhangapp.utils.ApiUtils;
import com.mth.example.banhangapp.utils.RealPathUtil;

import java.io.File;
import java.io.IOException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FixProfileActivity extends AppCompatActivity {
    private ImageButton butBack, butEdit;
    private ImageView imgAvatar;
    private EditText edtTen, edtDiaChi, edtSdt;
    private Button butXacNhan, butHuy;

    private ProgressDialog progressDialog;
    private User user;
    private static final int myRequestCode = 10;
    private Uri mUri;
    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) {
                            return;
                        } else {
                            Uri uri = data.getData();
                            mUri = uri;
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                imgAvatar.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
    );
    private Disposable disposable, disposable1;
    private String pathFromServer = "";
    private String response = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_profile);
        anhXa();
        getInformation();
        setAction();
    }

    private void anhXa() {
        butBack = findViewById(R.id.but_back_fixprofile);
        imgAvatar = findViewById(R.id.img_hinh_fixprofile_avatar);
        edtTen = findViewById(R.id.edt_ten_fixprofile);
        edtDiaChi = findViewById(R.id.edt_diachi_fixprofile);
        edtSdt = findViewById(R.id.edt_sdt_fixprofile);
        butEdit = findViewById(R.id.but_edit_fixprofile);
        butXacNhan = findViewById(R.id.but_xacnhan_fixprofile);
        butHuy = findViewById(R.id.but_huy_fixprofile);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.txt_chochut));
    }

    private void getInformation() {
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("dulieu");
        edtTen.setText(user.getTenuser());
        edtDiaChi.setText(user.getDiachi());
        edtSdt.setText(user.getSdt() + "");
        if (user.getAnh().length() > 0) {
            Glide.with(FixProfileActivity.this).load(user.getAnh()).into(imgAvatar);
        } else {
            return;
        }

    }

    private void setAction() {
        butBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(FixProfileActivity.this, MyProfileActivity.class);
//                intent.putExtra("dulieu", user.getId());
//                startActivity(intent);
                finish();
            }
        });
        butHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(FixProfileActivity.this, MyProfileActivity.class);
//                intent.putExtra("dulieu", user.getId());
//                startActivity(intent);
                finish();
            }
        });
        butEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });
        butXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(FixProfileActivity.this);
                alertDialog.setTitle(getResources().getString(R.string.txt_messagechinhsua));
                alertDialog.setIcon(R.drawable.ic_notification);
                alertDialog.setMessage(getResources().getString(R.string.txt_messagebanmuonsua));
                alertDialog.setPositiveButton(getResources().getString(R.string.txt_co), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (mUri == null) {
                            updateUserWithoutImage();
                        } else {
                            String realPath = RealPathUtil.getRealPath(FixProfileActivity.this, mUri);
                            File file = new File(realPath);
                            String file_path = file.getAbsolutePath();
                            String[] mangTenFile = file_path.split("\\.");
                            file_path = mangTenFile[0] + System.currentTimeMillis() + "." + mangTenFile[1];
                            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                            MultipartBody.Part photo = MultipartBody.Part.createFormData("hinhanh", file_path, requestBody);
                            progressDialog.show();
                            ApiUtils.getData().uploadPhoto(photo)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<String>() {
                                        @Override
                                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                            disposable = d;
                                        }

                                        @Override
                                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull String s) {
                                            pathFromServer = s;
                                        }

                                        @Override
                                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(FixProfileActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onComplete() {
                                            updateUser();
                                        }
                                    });
                        }
                    }
                });

                alertDialog.setNegativeButton(getResources().getString(R.string.txt_ko), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.show();
            }
        });
    }

    private void updateUserWithoutImage() {
        int id = user.getId();
        String tenMoi = edtTen.getText().toString().trim();
        String diaChiMoi = edtDiaChi.getText().toString().trim();
        int sdtMoi = Integer.parseInt(edtSdt.getText().toString().trim());
        if (diaChiMoi.isEmpty() || tenMoi.isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.txt_messagehaynhapdu), Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.show();
            ApiUtils.getData().updateUserWithoutImg(id, tenMoi, diaChiMoi, sdtMoi)
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
                            Toast.makeText(FixProfileActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
                            if (response.trim().equals("success")) {
                                progressDialog.dismiss();
                                Toast.makeText(FixProfileActivity.this, getResources().getString(R.string.txt_fixsuccess), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(FixProfileActivity.this, MyProfileActivity.class);
                                intent.putExtra("dulieu", user.getId());
                                startActivity(intent);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(FixProfileActivity.this, getResources().getString(R.string.txt_fixfail), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void updateUser() {
        String hinhAnh = ApiUtils.baseUrl + "image/" + pathFromServer;
        int id = user.getId();
        String tenMoi = edtTen.getText().toString().trim();
        String diaChiMoi = edtDiaChi.getText().toString().trim();
        int sdtMoi = Integer.parseInt(edtSdt.getText().toString().trim());
        if (diaChiMoi.isEmpty() || tenMoi.isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.txt_messagehaynhapdu), Toast.LENGTH_SHORT).show();
        } else {
            ApiUtils.getData().updateUser(id, tenMoi, diaChiMoi, sdtMoi, hinhAnh)
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
                            Toast.makeText(FixProfileActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
                            if (response.trim().equals("success")) {
                                progressDialog.dismiss();
                                Toast.makeText(FixProfileActivity.this, getResources().getString(R.string.txt_fixsuccess), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(FixProfileActivity.this, MyProfileActivity.class);
                                intent.putExtra("dulieu", user.getId());
                                startActivity(intent);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(FixProfileActivity.this, getResources().getString(R.string.txt_fixfail), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void onClickRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery();
            return;
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, myRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == myRequestCode && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
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
//        disposable.dispose();
//        disposable1.dispose();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FixProfileActivity.this, MyProfileActivity.class);
        intent.putExtra("dulieu", user.getId());
        startActivity(intent);
    }
}