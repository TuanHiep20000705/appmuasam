package com.mth.example.banhangapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mth.example.banhangapp.R;
import com.mth.example.banhangapp.activity.MoveListSp;
import com.mth.example.banhangapp.activity.MoveSPToMain;
import com.mth.example.banhangapp.activity.MoveToInfoSp;
import com.mth.example.banhangapp.adapter.LoaiSpAdapter;
import com.mth.example.banhangapp.adapter.SpAdapter;
import com.mth.example.banhangapp.model.LoaiSanPham;
import com.mth.example.banhangapp.model.QuangCao;
import com.mth.example.banhangapp.model.SanPham;
import com.mth.example.banhangapp.pagination.PaginationScrollListener;
import com.mth.example.banhangapp.utils.ApiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FragmentHome extends Fragment {
    private ViewFlipper viewFlipper;
    private RecyclerView rcvCategory;
    private RecyclerView rcvSanPham;

    private List<QuangCao> arrQuangCao;
    private Disposable disposable, disposable1, disposable2;
    private LoaiSpAdapter loaiSpAdapter;
    private SpAdapter spAdapter;
    private List<LoaiSanPham> listLoaiSp;
    private List<SanPham> listSpServer;
    public static MoveListSp moveListSp;
    public static MoveToInfoSp moveToInfoSp;
    public static MoveSPToMain moveSPToMain;
    private GridLayoutManager gridLayoutManager;
    private View view;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        translateData();
        anhXa();
        actionViewFlipper();
        getListLoaiSp();
        getListSp();
        return view;
    }

    private void translateData() {
        moveListSp = (MoveListSp) getActivity();
        moveToInfoSp = (MoveToInfoSp) getActivity();
        moveSPToMain = (MoveSPToMain) getActivity();
    }

    private void getListSp() {
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rcvSanPham.setLayoutManager(gridLayoutManager);
        rcvSanPham.setAdapter(spAdapter);
        ApiUtils.getData().getSanPham()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<SanPham>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable2 = d;
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<SanPham> sanPhams) {
                        listSpServer = sanPhams;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Collections.sort(listSpServer);
                        spAdapter.setData(listSpServer);
                    }
                });
    }


    private void getListLoaiSp() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        rcvCategory.setLayoutManager(linearLayoutManager);
        ApiUtils.getData().getLoaiSanPham()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<LoaiSanPham>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable1 = d;
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<LoaiSanPham> list) {
                        listLoaiSp = list;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                        loaiSpAdapter = new LoaiSpAdapter(getActivity(), listLoaiSp);
                        rcvCategory.setAdapter(loaiSpAdapter);
                    }
                });
    }


    private void actionViewFlipper() {
        progressDialog.show();
//        ApiUtils.getData().getQuangCao()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<QuangCao>>() {
//                    @Override
//                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
//                        disposable = d;
//                    }
//
//                    @Override
//                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<QuangCao> quangCaos) {
//                        arrQuangCao = quangCaos;
//                    }
//
//                    @Override
//                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
//                        Toast.makeText(getActivity(), getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        for (QuangCao quangCao :
//                                arrQuangCao) {
//                            ImageView imageView = new ImageView(getActivity());
//                            Glide.with(getActivity()).load(quangCao.getAnh()).into(imageView);
//                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                            viewFlipper.addView(imageView);
//                        }
//                        viewFlipper.setFlipInterval(3000);
//                        viewFlipper.setAutoStart(true);
//                        Animation slide_in = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_in_right);
//                        Animation slide_out = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_out_right);
//                        viewFlipper.setInAnimation(slide_in);
//                        viewFlipper.setOutAnimation(slide_out);
//                    }
//                });
        List listQuangCao = new ArrayList<String>();
        listQuangCao.add("http://channel.mediacdn.vn/thumb_w/640/2019/1/14/photo-1-1547460160838884501380.jpg?fbclid=IwAR3EYD9Hfx5-NtCT1FKnzYHlJhwgnmfGb0GORyB4yEm_y9dgN2y4h28rzpQ");
        listQuangCao.add("https://img.global.news.samsung.com/vn/wp-content/uploads/2021/05/KV-final-12-1024x666.jpg");
        listQuangCao.add("https://uploads-ssl.webflow.com/6073fad993ae97919f0b0772/609fa687874b84361fc495db_%C4%91t.jpg?fbclid=IwAR0jEPrRpozR-1XJ169l3DBblthpisUN5b72U0jROw2XPjyvq6IaiJvmnaQ");
        listQuangCao.add("https://www.informalnewz.com/wp-content/uploads/2022/08/Samsung-Galaxy-Z-Fold-4-and-Z-Flip-4-.jpg");
        for (int i = 0; i < listQuangCao.size(); i++) {
            ImageView imageView = new ImageView(getActivity());
            Glide.with(getActivity()).load(listQuangCao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }


    private void anhXa() {
        viewFlipper = view.findViewById(R.id.view_flipper);
        rcvCategory = view.findViewById(R.id.rcv_category);
        rcvSanPham = view.findViewById(R.id.rcv_sanpham);
        arrQuangCao = new ArrayList<>();
        listLoaiSp = new ArrayList<>();
        listSpServer = new ArrayList<>();
        spAdapter = new SpAdapter(getActivity());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getResources().getString(R.string.txt_chochut));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        disposable.dispose();
        disposable1.dispose();
        disposable2.dispose();
    }

}
