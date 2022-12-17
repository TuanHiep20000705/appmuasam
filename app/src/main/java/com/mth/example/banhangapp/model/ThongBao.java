package com.mth.example.banhangapp.model;

public class ThongBao implements Comparable<ThongBao> {
    private int id;
    private String username;
    private String date;
    private String noidung;
    private int loai;

    public ThongBao(int id, String username, String date, String noidung, int loai) {
        this.id = id;
        this.username = username;
        this.date = date;
        this.noidung = noidung;
        this.loai = loai;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public int getLoai() {
        return loai;
    }

    public void setLoai(int loai) {
        this.loai = loai;
    }

    @Override
    public int compareTo(ThongBao thongBao) {
        if (this.id < thongBao.id) {
            return 1;
        }
        if (this.id > thongBao.id) {
            return -1;
        }
        return 0;
    }
}
