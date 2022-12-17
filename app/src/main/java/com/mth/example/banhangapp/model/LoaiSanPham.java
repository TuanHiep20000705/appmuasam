package com.mth.example.banhangapp.model;

import java.io.Serializable;

public class LoaiSanPham implements Serializable {
    private int id;
    private String tenloai;
    private String anh;

    public LoaiSanPham(int id, String tenloai, String anh) {
        this.id = id;
        this.tenloai = tenloai;
        this.anh = anh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenloai() {
        return tenloai;
    }

    public void setTenloai(String tenloai) {
        this.tenloai = tenloai;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }
}
