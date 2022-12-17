package com.mth.example.banhangapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mth.example.banhangapp.R;
import com.mth.example.banhangapp.adapter.SpAdapter;
import com.mth.example.banhangapp.model.LoaiSanPham;
import com.mth.example.banhangapp.model.SanPham;
import com.mth.example.banhangapp.pagination.PaginationScrollListener;
import com.mth.example.banhangapp.utils.ApiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ListSpActivity extends AppCompatActivity {
    private ImageButton butListSpBack, butListCart;
    private TextView edtSpSearch;
    private TextView txtListSpDanhMuc;
    private RecyclerView rcvListSp;

    private LoaiSanPham loaiSanPham;
    private SpAdapter spAdapter;
    private GridLayoutManager gridLayoutManager;
    private Disposable disposable;
    private List<SanPham> listSanPhamServer;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sp);
        anhXa();
        getInformation();
        getListSp();
        actionToolBar();
    }

    private void getListSp() {
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        rcvListSp.setLayoutManager(gridLayoutManager);
        rcvListSp.setAdapter(spAdapter);
        progressDialog.show();
        ApiUtils.getData().getSanPhamDanhMuc(loaiSanPham.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<SanPham>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull List<SanPham> sanPhams) {
                        listSanPhamServer = sanPhams;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        progressDialog.dismiss();
                        Toast.makeText(ListSpActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                        Collections.sort(listSanPhamServer);
                        spAdapter.setData(listSanPhamServer);
                    }
                });
    }


    private void anhXa() {
        butListSpBack = findViewById(R.id.but_list_sp_back);
        butListCart = findViewById(R.id.but_list_cart);
        edtSpSearch = findViewById(R.id.edt_list_sp_search);
        txtListSpDanhMuc = findViewById(R.id.txt_list_sp_danh_muc);
        rcvListSp = findViewById(R.id.rcv_list_sp);
        spAdapter = new SpAdapter(getApplicationContext());
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.txt_chochut));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    private void actionToolBar() {
        butListSpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edtSpSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListSpActivity.this, SearchSpActivity.class));
            }
        });
    }

    private void getInformation() {
        Intent intent = getIntent();
        loaiSanPham = (LoaiSanPham) intent.getSerializableExtra("dulieu");
        txtListSpDanhMuc.setText(getResources().getString(R.string.txt_muclistdanhsach) + " " + loaiSanPham.getTenloai().toLowerCase());
    }
}