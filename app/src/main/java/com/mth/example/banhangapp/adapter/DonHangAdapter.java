package com.mth.example.banhangapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mth.example.banhangapp.R;
import com.mth.example.banhangapp.model.DonHang;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.DonHangViewHolder> {
    private Context context;
    private List<DonHang> donHangList;
    private iClickMuaLaiListener iClickMuaLaiListener;

    public DonHangAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<DonHang> list) {
        this.donHangList = list;
    }

    public interface iClickMuaLaiListener {
        void onClickMuaLai(int idsp,String username);
    }
    public void xuLyMuaLai(iClickMuaLaiListener listener){
        this.iClickMuaLaiListener = listener;
    }

    @NonNull
    @Override
    public DonHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lichsu, parent, false);
        return new DonHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonHangViewHolder holder, int position) {
        DecimalFormat dcf = new DecimalFormat("#,###");
        SimpleDateFormat scf = new SimpleDateFormat("dd/MM/yyyy");
        DonHang donHang = donHangList.get(position);
        if (donHang == null) {
            return;
        }
        holder.txtNgayMua.setText(scf.format(donHang.getDate()));
        holder.txtTenSp.setText(donHang.getTensp());
        holder.txtSoLuong.setText("x" + donHang.getSoluong());
        holder.txtDonGia.setText("đ" + dcf.format(donHang.getDongia()));
        holder.txtTongTien.setText("đ" + dcf.format(donHang.getTonggia() + 15000));
        Glide.with(context).load(donHang.getAnhsp()).into(holder.imgHinh);
        holder.butMuaLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickMuaLaiListener.onClickMuaLai(donHang.getIdsp(),donHang.getUsername());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (donHangList != null) {
            return donHangList.size();
        }
        return 0;
    }

    public class DonHangViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNgayMua, txtTenSp, txtSoLuong, txtDonGia, txtTongTien;
        private Button butMuaLai;
        private ImageView imgHinh;

        public DonHangViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNgayMua = itemView.findViewById(R.id.txt_ngay_mua_lich_su);
            txtTenSp = itemView.findViewById(R.id.txt_ten_sp_lich_su);
            txtSoLuong = itemView.findViewById(R.id.txt_so_luong_lich_su);
            txtDonGia = itemView.findViewById(R.id.txt_don_gia_lich_su);
            txtTongTien = itemView.findViewById(R.id.txt_tong_tien_lich_su);
            imgHinh = itemView.findViewById(R.id.img_hinh_lich_su);
            butMuaLai = itemView.findViewById(R.id.but_mua_lai_lich_su);
        }
    }
}
