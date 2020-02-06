package com.mengjie.dao;

import com.google.gson.Gson;
import com.mengjie.bean.FundBean;
import com.mengjie.config.API;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Fund {

    public interface FundCallback{
        default void onFailure(Call call, IOException e){
            e.printStackTrace();
        }


        void onResponse(FundBean fundBean);
    }

    private OkHttpClient client = new OkHttpClient();

    private String fundCode;
    private FundBean fundBean=null;

    public FundBean getFundBean() {
        return fundBean;
    }

    public Fund(String fundCode){
        this.fundCode=fundCode;
    }

    public void requestFund(FundCallback callback){
        Request request = new Request.Builder().url(String.format(API.fundURL,fundCode)).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onFailure(call,e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(Fund.this.fundBean==null){
                    Gson gson = new Gson();
                    String responseStringData = response.body().string();
                    Fund.this.fundBean = gson.fromJson(responseStringData, FundBean.class);
                }
                callback.onResponse(fundBean);
            }
        });
    }


}
