package com.mth.example.banhangapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mth.example.banhangapp.R;
import com.mth.example.banhangapp.adapter.GioHangAdapter;
import com.mth.example.banhangapp.adapter.ThongBaoAdapter;
import com.mth.example.banhangapp.model.GioHang;
import com.mth.example.banhangapp.model.ThongBao;
import com.mth.example.banhangapp.model.User;
import com.mth.example.banhangapp.utils.ApiUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FragmentNotification extends Fragment {
    private RecyclerView rcvThongBao;
    private View view;

    private ThongBaoAdapter thongBaoAdapter;
    private List<ThongBao> thongBaoListServer;
    private Disposable disposable;
    private User user;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notification, container, false);
        anhXa();
        getInformation();
        return view;
    }

    private void getInformation() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rcvThongBao.setLayoutManager(linearLayoutManager);
        rcvThongBao.addItemDecoration(dividerItemDecoration);

        Bundle bundle = getArguments();
        user = (User) bundle.getSerializable("dulieu");
        if (user != null) {
            progressDialog.show();
            ApiUtils.getData().getDataThongBao(user.getUsername())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<ThongBao>>() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                            disposable = d;
                        }

                        @Override
                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<ThongBao> thongBaos) {
                            thongBaoListServer = thongBaos;
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
                            progressDialog.dismiss();
                            Collections.sort(thongBaoListServer);
                            thongBaoAdapter = new ThongBaoAdapter(thongBaoListServer);
                            rcvThongBao.setAdapter(thongBaoAdapter);
                        }
                    });
        } else {
            thongBaoAdapter = new ThongBaoAdapter(getListNullThongBao());
            rcvThongBao.setAdapter(thongBaoAdapter);
            Toast.makeText(getActivity(), getResources().getString(R.string.txt_messagedangnhap), Toast.LENGTH_SHORT).show();
        }

    }

    private void anhXa() {
        rcvThongBao = view.findViewById(R.id.rcv_thong_bao);
        thongBaoListServer = new ArrayList<>();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getResources().getString(R.string.txt_chochut));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    private List<ThongBao> getListNullThongBao() {
        List<ThongBao> list = new ArrayList<>();
        return list;
    }
}
