package com.mengjie.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mengjie.bean.FundBean;
import com.mengjie.dao.Fund;

public class FundDetail extends AppCompatActivity {

    private Fund fund=null;

    private ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_detail);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("request data!");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        fund = new Fund(getIntent().getStringExtra("code"));

        fund.requestFund(new Fund.FundCallback() {
            @Override
            public void onResponse(FundBean fundBean) {
                FundDetail.this.paintData();
                progressDialog.cancel();
            }
        });

    }

    public void paintData(){
        TextView fundNameView = findViewById(R.id.fundName);
        TextView fundSourceRate = findViewById(R.id.fund_sourceRate);
        TextView fundRate = findViewById(R.id.fund_Rate);
        TextView syl6y = findViewById(R.id.syl_6y);
        TextView syl3y = findViewById(R.id.syl_3y);
        TextView syl1y = findViewById(R.id.syl_1y);
        TextView syl1n = findViewById(R.id.syl_1n);

        FundBean.Detail detail = fund.getFundBean().getDetail();
        fundNameView.setText(detail.getfS_name());
        fundSourceRate.setText(String.format("原费率: %s",detail.getFund_sourceRate()));
        fundRate.setText(String.format("费率: %s",detail.getFund_Rate()));
        syl1n.setText(String.format("1年:%s",detail.getSyl_1n()));
        syl1y.setText(String.format("1月:%s",detail.getSyl_1y()));
        syl3y.setText(String.format("3月:%s",detail.getSyl_3y()));
        syl6y.setText(String.format("6月:%s",detail.getSyl_6y()));

        ((TextView)findViewById(R.id.label_rate)).setText("收益率");


    }

}
