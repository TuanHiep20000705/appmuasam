package com.mth.example.banhangapp.utils;

import com.mth.example.banhangapp.retrofit.DataClient;
import com.mth.example.banhangapp.retrofit.RetrofitClient;

public class ApiUtils {
    public static String baseUrl = "https://hostcuahiep123.000webhostapp.com/banhangapp/";
    public static DataClient getData(){
        return RetrofitClient.getClient(baseUrl).create(DataClient.class);
    }
}
