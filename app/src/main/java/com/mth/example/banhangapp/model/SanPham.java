package com.mth.example.banhangapp.model;

import java.io.Serializable;

public class SanPham implements Serializable,Comparable<SanPham> {
    private int id;
    private String tensp;
    private String anh;
    private int giasp;
    private int solanduocmua;
    private String mota;

    public SanPham(int id, String tensp, String anh, int giasp, int solanduocmua, String mota) {
        this.id = id;
        this.tensp = tensp;
        this.anh = anh;
        this.giasp = giasp;
        this.solanduocmua = solanduocmua;
        this.mota = mota;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public int getGiasp() {
        return giasp;
    }

    public void setGiasp(int giasp) {
        this.giasp = giasp;
    }

    public int getSolanduocmua() {
        return solanduocmua;
    }

    public void setSolanduocmua(int solanduocmua) {
        this.solanduocmua = solanduocmua;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    @Override
    public int compareTo(SanPham sanPham) {
        if (this.solanduocmua < sanPham.getSolanduocmua()) {
            return 1;
        }
        if (this.solanduocmua > sanPham.getSolanduocmua()) {
            return -1;
        }
        return 0;
    }
}
