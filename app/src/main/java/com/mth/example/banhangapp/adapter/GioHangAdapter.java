package com.mth.example.banhangapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mth.example.banhangapp.R;
import com.mth.example.banhangapp.model.GioHang;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.GioHangViewHolder> {
    private Context context;
    private List<GioHang> gioHangList;
    private iClickPlusListener iClickPlusListener;
    private iClickMinusListener iClickMinusListener;
    private iClickDeleteListener iClickDeleteListener;

    public GioHangAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<GioHang> list) {
        gioHangList = list;
        notifyDataSetChanged();
    }

    public interface iClickPlusListener {
        void onClickPlus(int donGia, int soLuong, int idSp);
    }

    public void xuLyCongTongTien(iClickPlusListener listener) {
        this.iClickPlusListener = listener;
    }

    public interface iClickMinusListener {
        void onClickMinus(int donGia, int soLuong, int idSp);
    }

    public void xuLyTruTongTien(iClickMinusListener listener) {
        this.iClickMinusListener = listener;
    }

    public interface iClickDeleteListener {
        void onClickDelete(int idsp, int donGia, int soLuong);
    }

    public void xuLyDelete(iClickDeleteListener listener) {
        this.iClickDeleteListener = listener;
    }

    @NonNull
    @Override
    public GioHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang, parent, false);
        return new GioHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GioHangViewHolder holder, int position) {
        int viTri = position;
        DecimalFormat dcf = new DecimalFormat("#,###");
        GioHang gioHang = gioHangList.get(position);
        if (gioHang == null) {
            return;
        }
        holder.txtTen.setText(gioHang.getTensp());
        holder.txtGia.setText("đ" + dcf.format(gioHang.getDongia()));
        holder.butValue.setText(gioHang.getSoluong() + "");
        Glide.with(context).load(gioHang.getAnhsp()).into(holder.imgHinh);
        holder.butPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = Integer.parseInt(holder.butValue.getText().toString());
                value += 1;
                if (value <= 10) {
                    holder.butValue.setText(value + "");
                    iClickPlusListener.onClickPlus(gioHang.getDongia(), value, gioHang.getIdsp());
                } else {
                    Toast.makeText(context, "Số lượng đã tối đa!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.butMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = Integer.parseInt(holder.butValue.getText().toString());
                value -= 1;
                if (value >= 1) {
                    holder.butValue.setText(value + "");
                    iClickMinusListener.onClickMinus(gioHang.getDongia(), value, gioHang.getIdsp());
                } else {
                    Toast.makeText(context, "Số lượng đã tối thiểu!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.butDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = Integer.parseInt(holder.butValue.getText().toString());
                iClickDeleteListener.onClickDelete(gioHang.getIdsp(), gioHang.getDongia(), value);
                gioHangList.remove(viTri);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (gioHangList != null) {
            return gioHangList.size();
        }
        return 0;
    }

    public class GioHangViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgHinh;
        private ImageButton butDelete;
        private TextView txtTen, txtGia;
        private Button butMinus, butValue, butPlus;

        public GioHangViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinh = itemView.findViewById(R.id.img_hinh_gio_hang);
            butDelete = itemView.findViewById(R.id.but_delete_gio_hang);
            txtTen = itemView.findViewById(R.id.txt_ten_sp_gio_hang);
            txtGia = itemView.findViewById(R.id.txt_don_gia_sp_gio_hang);
            butMinus = itemView.findViewById(R.id.but_minus);
            butValue = itemView.findViewById(R.id.but_value);
            butPlus = itemView.findViewById(R.id.but_plus);
        }
    }
}
