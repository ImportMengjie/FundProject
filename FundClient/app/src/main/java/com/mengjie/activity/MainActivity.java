package com.mengjie.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.mengjie.dao.FundList;
import com.mengjie.bean.FundListBean;
import com.mengjie.util.PagingFundListAdapter;
import com.paging.listview.PagingListView;


public class MainActivity extends AppCompatActivity {


    public int currentPage = 1;

    public FundList fundList = new FundList();

    public static String fundListMessage = "total count:%d, total page:%d, current page:%d";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_list);
        final PagingListView fundListView = findViewById(R.id.fundList);
        final TextView fundListMessageView = findViewById(R.id.fundListMessage);
        fundListMessageView.setText(String.format(fundListMessage,0,0,0));

        fundListView.setAdapter(new PagingFundListAdapter());
        fundListView.setHasMoreItems(true);

        fundListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this, String.format("onItemClick: %d name: %s", position, fundList.getFundBean(position).name),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, FundDetail.class);
                intent.putExtra("code",fundList.getFundBean(position).code);
                startActivity(intent);

            }
        });


        fundListView.setPagingableListener(() -> {
            if(fundList.getTotalPage()==0||currentPage<=fundList.getTotalPage()){
                fundList.requestFundList(currentPage, new FundList.FundListCallback() {
                    @Override
                    public void onResponse(FundListBean fundListBean) {
                        fundListView.post(() -> fundListView.onFinishLoading(true, fundListBean.getFundBeanList()));

                        fundListMessageView.post(()->fundListMessageView.setText(String.format(fundListMessage,fundListBean.getRecord(), fundListBean.getPages(), fundListBean.getCurpage())));
                    }
                });
                currentPage++;

            }else{
                fundListView.post(() -> fundListView.onFinishLoading(true, null));
            }
        });


    }
}
