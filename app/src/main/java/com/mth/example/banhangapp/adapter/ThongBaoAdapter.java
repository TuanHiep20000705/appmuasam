package com.mth.example.banhangapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mth.example.banhangapp.R;
import com.mth.example.banhangapp.model.ThongBao;

import java.util.List;

public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.ThongBaoViewHolder> {
    private List<ThongBao> thongBaoList;

    public ThongBaoAdapter(List<ThongBao> thongBaoList) {
        this.thongBaoList = thongBaoList;
    }

    @NonNull
    @Override
    public ThongBaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thongbao, parent, false);
        return new ThongBaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThongBaoViewHolder holder, int position) {
        ThongBao thongBao = thongBaoList.get(position);
        if (thongBao == null) {
            return;
        }
        holder.txtDate.setText(thongBao.getDate());
        holder.txtNoiDung.setText(thongBao.getNoidung());
        if (thongBao.getLoai() == 1) {
            holder.imgHinh.setImageResource(R.drawable.ic_carttt);
        } else holder.imgHinh.setImageResource(R.drawable.icon_shoppingbag);
    }

    @Override
    public int getItemCount() {
        if (thongBaoList != null) {
            return thongBaoList.size();
        }
        return 0;
    }

    public class ThongBaoViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDate, txtNoiDung;
        private ImageView imgHinh;

        public ThongBaoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txt_date_thong_bao);
            txtNoiDung = itemView.findViewById(R.id.txt_noi_dung_thong_bao);
            imgHinh = itemView.findViewById(R.id.img_hinh_thong_bao);
        }
    }
}
