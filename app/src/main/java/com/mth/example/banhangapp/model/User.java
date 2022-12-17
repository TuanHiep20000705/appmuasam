package com.mth.example.banhangapp.model;

import android.text.TextUtils;
import android.util.Patterns;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String username;
    private String password;
    private String tenuser;
    private String diachi;
    private String anh;
    private int damua;
    private int sdt;

    public User(int id, String username, String password, String tenuser, String diachi, String anh, int damua, int sdt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.tenuser = tenuser;
        this.diachi = diachi;
        this.anh = anh;
        this.damua = damua;
        this.sdt = sdt;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTenuser() {
        return tenuser;
    }

    public void setTenuser(String tenuser) {
        this.tenuser = tenuser;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public int getDamua() {
        return damua;
    }

    public void setDamua(int damua) {
        this.damua = damua;
    }

    public int getSdt() {
        return sdt;
    }

    public void setSdt(int sdt) {
        this.sdt = sdt;
    }
}
