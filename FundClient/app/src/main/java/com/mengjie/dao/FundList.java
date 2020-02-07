package com.mengjie.dao;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.Gson;
import com.mengjie.bean.FundListBean;
import com.mengjie.config.API;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FundList {


    private int totalCount=0;
    private int totalPage=0;
    private int pageSize;
    private OkHttpClient client = new OkHttpClient();

    @SuppressLint("UseSparseArrays")
    private Map<Integer, FundListBean> mapPageFundListBean = new HashMap<Integer,FundListBean>();

    private Map<String, FundListBean> mapKeywordFundListBean = new HashMap<>();


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

    public FundListBean.FundBean getFundBean(int position, String keyword){
        if(keyword==null&&mapPageFundListBean.containsKey(position/pageSize +1 )){
            return Objects.requireNonNull(mapPageFundListBean.get((position / pageSize) +1)).getFundBeanList().get(position%pageSize);
        }else if(keyword!=null&&mapKeywordFundListBean.containsKey(keyword)){
            return Objects.requireNonNull(mapKeywordFundListBean.get(keyword)).getFundBeanList().get(position);
        }
        return null;
    }

    public void requestFundList(int page, FundListCallback callback){
        requestFundList(page,callback,null);
    }

    public void requestFundList(int page, FundListCallback callback, String keyword){
        if(mapPageFundListBean.containsKey(page) && keyword==null)
            callback.onResponse(mapPageFundListBean.get(page));
        else if(keyword!=null && mapKeywordFundListBean.containsKey(keyword))
            callback.onResponse(mapKeywordFundListBean.get(keyword));
        else{
            String url;
            if(keyword==null)
                url = String.format(API.fundListURL, page, pageSize);
            else
                url = String.format(API.searchFundListURL,keyword);
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
                    if(keyword==null)
                        mapPageFundListBean.put(page, fundListBean);
                    else
                        mapKeywordFundListBean.put(keyword,fundListBean);
                    callback.onResponse(fundListBean);
                }
            });

        }
    }


}
