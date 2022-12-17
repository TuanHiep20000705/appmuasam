package com.mth.example.banhangapp.model;

public class GioHang {
    private int id;
    private String username;
    private int idsp;
    private int soluong;
    private int tonggia;
    private String tensp;
    private String anhsp;
    private int dongia;

    public GioHang(int id, String username, int idsp, int soluong, int tonggia, String tensp, String anhsp, int dongia) {
        this.id = id;
        this.username = username;
        this.idsp = idsp;
        this.soluong = soluong;
        this.tonggia = tonggia;
        this.tensp = tensp;
        this.anhsp = anhsp;
        this.dongia = dongia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getIdsp() {
        return idsp;
    }

    public void setIdsp(int idsp) {
        this.idsp = idsp;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public int getTonggia() {
        return tonggia;
    }

    public void setTonggia(int tonggia) {
        this.tonggia = tonggia;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public String getAnhsp() {
        return anhsp;
    }

    public void setAnhsp(String anhsp) {
        this.anhsp = anhsp;
    }

    public int getDongia() {
        return dongia;
    }

    public void setDongia(int dongia) {
        this.dongia = dongia;
    }
}
