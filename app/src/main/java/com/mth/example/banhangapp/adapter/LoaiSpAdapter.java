package com.mth.example.banhangapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mth.example.banhangapp.R;
import com.mth.example.banhangapp.fragment.FragmentHome;
import com.mth.example.banhangapp.model.LoaiSanPham;

import java.util.List;

public class LoaiSpAdapter extends RecyclerView.Adapter<LoaiSpAdapter.LoaiSpViewHolder> {
    private Context context;
    private final List<LoaiSanPham> loaiSanPhamList;

    public LoaiSpAdapter(Context context, List<LoaiSanPham> loaiSanPhamList) {
        this.loaiSanPhamList = loaiSanPhamList;
        this.context = context;
    }

    @NonNull
    @Override
    public LoaiSpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loaisp, parent, false);
        return new LoaiSpViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoaiSpViewHolder holder, int position) {
        LoaiSanPham loaiSanPham = loaiSanPhamList.get(position);
        if (loaiSanPham == null) {
            return;
        } else {
            holder.txtTenLoaiSp.setText(loaiSanPham.getTenloai());
            Glide.with(context).load(loaiSanPham.getAnh()).into(holder.imgHinhLoaiSp);
        }
        holder.cardViewLoaiSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCardViewLoaiSp(loaiSanPham);
            }
        });
    }

    private void onClickCardViewLoaiSp(LoaiSanPham loaiSanPham) {
        FragmentHome.moveListSp.giaTriLoai(loaiSanPham);
    }

    @Override
    public int getItemCount() {
        if (loaiSanPhamList != null) {
            return loaiSanPhamList.size();
        }
        return 0;
    }

    public class LoaiSpViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgHinhLoaiSp;
        private TextView txtTenLoaiSp;
        private CardView cardViewLoaiSp;

        public LoaiSpViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinhLoaiSp = itemView.findViewById(R.id.img_hinh_loaisp);
            txtTenLoaiSp = itemView.findViewById(R.id.txt_ten_loaisp);
            cardViewLoaiSp = itemView.findViewById(R.id.card_view_loai_sp);
        }
    }
}
