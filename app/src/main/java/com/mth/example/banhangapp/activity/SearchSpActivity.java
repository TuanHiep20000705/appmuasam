package com.mth.example.banhangapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mth.example.banhangapp.R;
import com.mth.example.banhangapp.adapter.SpAdapter;
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

public class SearchSpActivity extends AppCompatActivity {
    private RecyclerView rcvSanPham;
    private ImageButton butBack;
    private GridLayoutManager gridLayoutManager;
    private EditText edtSearch;

    private SpAdapter spAdapter;
    private Disposable disposable;
    private List<SanPham> listSpServer;
    private List<SanPham> filterList;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_sp);
        anhXa();
        getListSp();
        setAction();
    }

    private void setAction(){
        butBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterList.clear();
                if (editable.toString().isEmpty()) {
                    SpAdapter spAdapter1 = new SpAdapter(SearchSpActivity.this);
                    spAdapter1.setData(listSpServer);
                    rcvSanPham.setAdapter(spAdapter1);
                    spAdapter.notifyDataSetChanged();
                } else {
                    searchFilter(editable.toString());
                }
            }
        });
    }

    private void searchFilter(String text) {
        for (SanPham sanPham :
                listSpServer) {
            if (sanPham.getTensp().toLowerCase().contains(text.toLowerCase())) {
                filterList.add(sanPham);
            }
        }
        SpAdapter spAdapter2 = new SpAdapter(this);
        spAdapter2.setData(filterList);
        rcvSanPham.setAdapter(spAdapter2);
        spAdapter.notifyDataSetChanged();
    }

    private void anhXa() {
        butBack = findViewById(R.id.but_back_search);
        rcvSanPham = findViewById(R.id.rcv_search);
        spAdapter = new SpAdapter(SearchSpActivity.this);
        listSpServer = new ArrayList<>();
        filterList = new ArrayList<>();
        edtSearch = findViewById(R.id.edt_search_searchactivity);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.txt_chochut));
    }

    private void getListSp() {
        gridLayoutManager = new GridLayoutManager(this, 2);
        rcvSanPham.setLayoutManager(gridLayoutManager);
        rcvSanPham.setAdapter(spAdapter);
        progressDialog.show();
        ApiUtils.getData().getSanPham()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<SanPham>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<SanPham> sanPhams) {
                        listSpServer = sanPhams;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();
                        Toast.makeText(SearchSpActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                        Collections.sort(listSpServer);
                        spAdapter.setData(listSpServer);
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
}