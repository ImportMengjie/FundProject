package com.mengjie.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mengjie.bean.FundList;
import com.mengjie.bean.FundListBean;
import com.mengjie.util.PagingFundListAdapter;
import com.paging.listview.PagingListView;


public class MainActivity extends AppCompatActivity {


    public int currentPage = 0;

    public FundList fundList = new FundList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_fund);
        final PagingListView fundListView = findViewById(R.id.fundList);

        fundListView.setAdapter(new PagingFundListAdapter());
        fundListView.setHasMoreItems(true);

        fundListView.setPagingableListener(new PagingListView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                if(fundList.getTotalPage()==0||currentPage<=fundList.getTotalPage()){
                    fundList.requestFundList(currentPage, new FundList.FundListCallback() {
                        @Override
                        public void onResponse(FundListBean fundListBean) {
                            fundListView.post(new Runnable() {
                                @Override
                                public void run() {
                                    fundListView.onFinishLoading(true, fundListBean.getFundBeanList());
                                }
                            });
                        }
                    });

                }else{
                    fundListView.post(new Runnable() {
                        @Override
                        public void run() {
                            fundListView.onFinishLoading(true, null);
                        }
                    });
                }
            }
        });


    }
}
