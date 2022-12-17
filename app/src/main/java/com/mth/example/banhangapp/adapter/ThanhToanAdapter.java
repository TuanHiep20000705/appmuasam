package com.mth.example.banhangapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mth.example.banhangapp.R;
import com.mth.example.banhangapp.model.GioHang;

import java.text.DecimalFormat;
import java.util.List;

public class ThanhToanAdapter extends RecyclerView.Adapter<ThanhToanAdapter.ThanhTonViewHolder> {
    private Context context;
    private List<GioHang> gioHangList;

    public ThanhToanAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<GioHang> list) {
        this.gioHangList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ThanhTonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thanhtoan, parent, false);
        return new ThanhTonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThanhTonViewHolder holder, int position) {
        DecimalFormat dcf = new DecimalFormat("#,###");
        GioHang gioHang = gioHangList.get(position);
        if (gioHang == null) {
            return;
        }
        holder.txtTen.setText(gioHang.getTensp());
        holder.txtSoLuong.setText("x" + gioHang.getSoluong());
        holder.txtDonGia.setText("đ" + dcf.format(gioHang.getDongia()));
        Glide.with(context).load(gioHang.getAnhsp()).into(holder.imgHinh);
    }

    @Override
    public int getItemCount() {
        if (gioHangList != null) {
            return gioHangList.size();
        }
        return 0;
    }

    public class ThanhTonViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgHinh;
        private TextView txtTen, txtSoLuong, txtDonGia;

        public ThanhTonViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinh = itemView.findViewById(R.id.img_hinh_thanh_toan);
            txtTen = itemView.findViewById(R.id.txt_ten_sp_thanh_toan);
            txtSoLuong = itemView.findViewById(R.id.txt_so_luong_thanh_toan);
            txtDonGia = itemView.findViewById(R.id.txt_don_gia_sp_thanh_toan);
        }
    }
}
