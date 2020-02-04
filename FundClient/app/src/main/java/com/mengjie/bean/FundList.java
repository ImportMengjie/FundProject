package com.mengjie.bean;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FundList {


    private int totalCount=0;
    private int totalPage=0;
    private int pageSize;
    OkHttpClient client = new OkHttpClient();

    @SuppressLint("UseSparseArrays")
    private Map<Integer,FundListBean> mapPageFundListBean = new HashMap<Integer,FundListBean>();

    static public String FundListAPI = "http://10.0.2.2:8080/"+"api/fund";

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public interface FundListCallback{
        default void onFailure(Call call, IOException e){
            e.printStackTrace();
        }


        void onResponse(FundListBean fundListBean);
    }

    public FundList(int pageSize){
        this.pageSize=pageSize;
    }

    public FundList(){
        this.pageSize=100;
    }

    public void requestFundList(int page, FundListCallback callback){
        if(mapPageFundListBean.containsKey(page))
            callback.onResponse(mapPageFundListBean.get(page));
        else{
            String url = FundListAPI+"?page="+String.valueOf(page)+"&pagesize="+String.valueOf(pageSize);
            Request request = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    callback.onFailure(call,e);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Gson gson = new Gson();
                    String responseStringData = response.body().string();
                    Log.d("response", "onResponse: "+responseStringData);
                    FundListBean fundListBean = gson.fromJson(responseStringData,FundListBean.class);
                    FundList.this.totalCount = fundListBean.getRecord();
                    FundList.this.totalPage = fundListBean.getPages();
                    mapPageFundListBean.put(page, fundListBean);
                    callback.onResponse(fundListBean);
                }
            });

        }
    }


}
