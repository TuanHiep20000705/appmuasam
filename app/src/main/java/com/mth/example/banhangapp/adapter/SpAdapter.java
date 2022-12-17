package com.mth.example.banhangapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mth.example.banhangapp.R;
import com.mth.example.banhangapp.fragment.FragmentHome;
import com.mth.example.banhangapp.model.SanPham;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SpAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;
    private boolean isLoadingAdd;
    private Context context;
    private List<SanPham> sanPhamList;

    public SpAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<SanPham> list) {
        sanPhamList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (sanPhamList != null && position == sanPhamList.size() - 1 && isLoadingAdd) {
            return TYPE_LOADING;
        } else return TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp, parent, false);
            return new SpViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DecimalFormat dcf = new DecimalFormat("#,###");
        if (holder.getItemViewType() == TYPE_ITEM) {
            SanPham sanPham = sanPhamList.get(position);
            SpViewHolder spViewHolder = (SpViewHolder) holder;
            spViewHolder.txtTenSp.setText(sanPham.getTensp());
            spViewHolder.txtGiaSp.setText("đ" + dcf.format(sanPham.getGiasp()));
            Glide.with(context).load(sanPham.getAnh()).into(spViewHolder.imgHinhSp);
            spViewHolder.butnCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickCart(sanPham);
                }
            });
            spViewHolder.cardViewSp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickCardViewSp(sanPham);
                }
            });
        }
    }

    private void onClickCart(SanPham sanPham) {
        FragmentHome.moveSPToMain.giaTriSpFromCart(sanPham);
    }

    private void onClickCardViewSp(SanPham sanPham) {
        FragmentHome.moveToInfoSp.giaTriSp(sanPham);
    }

    @Override
    public int getItemCount() {
        if (sanPhamList != null) {
            return sanPhamList.size();
        }
        return 0;
    }

    public class SpViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgHinhSp;
        private TextView txtTenSp, txtGiaSp;
        private ImageButton butnCart;
        private CardView cardViewSp;

        public SpViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinhSp = itemView.findViewById(R.id.img_hinh_sp);
            txtTenSp = itemView.findViewById(R.id.txt_ten_sp);
            txtGiaSp = itemView.findViewById(R.id.txt_gia_sp);
            butnCart = itemView.findViewById(R.id.but_cart);
            cardViewSp = itemView.findViewById(R.id.card_view_sp);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_Bar);
        }
    }

}
