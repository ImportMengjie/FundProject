package com.mengjie.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.mengjie.bean.FundBean;
import com.mengjie.dao.Fund;
import com.mengjie.view.LineChartView;

import java.util.List;

public class FundDetail extends AppCompatActivity {

    private Fund fund=null;

    private Handler handler;
    private LineChartView netWorthChart;
    private LineChartView grandTotalChart;

    private ProgressDialog progressDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        netWorthChart = findViewById(R.id.netWorthChart);
        handler = new Handler();

        setContentView(R.layout.activity_fund_detail);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("request data!");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        fund = new Fund(getIntent().getStringExtra("code"));

        fund.requestFund(fundBean -> FundDetail.this.handler.post(()->{
            FundDetail.this.paintData();
            progressDialog.cancel();
        }));
    }


    public void paintData(){
        netWorthChart = findViewById(R.id.netWorthChart);
        grandTotalChart = findViewById(R.id.grandTotalChart);

        TextView fundNameView = findViewById(R.id.fundName);
        TextView fundSourceRate = findViewById(R.id.fund_sourceRate);
        TextView fundRate = findViewById(R.id.fund_Rate);
        TextView syl6y = findViewById(R.id.syl_6y);
        TextView syl1y = findViewById(R.id.syl_1y);
        TextView syl1n = findViewById(R.id.syl_1n);

        FundBean.Detail detail = fund.getFundBean().getDetail();
        fundNameView.setText(detail.getfS_name());
        fundSourceRate.setText(detail.getFund_sourceRate());
        fundRate.setText(detail.getFund_Rate());
        syl1n.setText(detail.getSyl_1n()+"%");
        syl1y.setText(detail.getSyl_1y()+"%");
        syl6y.setText(detail.getSyl_6y()+"%");

        LineChartView.EntryList netWorthEntries = new LineChartView.EntryList("单位净值","", getResources().getColor(R.color.orange));
        List<FundBean.NetWorth> data_netWorthTrend = fund.getFundBean().getData_netWorthTrend();
        for (int i = 0; i < data_netWorthTrend.size(); i++) {
            FundBean.NetWorth netWorth = data_netWorthTrend.get(i);
            if(netWorth.getY()>=0)
               netWorthEntries.addEntry(netWorth.getY().floatValue(),netWorth.getX());
            else
                netWorthEntries.addEntry(null, null);
        }
        netWorthChart.addLine(netWorthEntries);
        netWorthChart.drawChart(30);

        LineChartView.EntryList grandTotalEntries = new LineChartView.EntryList("收益率","%",getResources().getColor(R.color.orange));
        LineChartView.EntryList peerGrandTotalEntries = new LineChartView.EntryList("同类平均","%",getResources().getColor(R.color.blue));
        LineChartView.EntryList hsGrandTotalEntries = new LineChartView.EntryList("沪深300","%",getResources().getColor(R.color.green));
        List<FundBean.GrandTotal> grandTotals = fund.getFundBean().getData_grandTotal();
        for (int i = 0; i < grandTotals.get(0).getData().size(); i++) {
            if(grandTotals.get(0).getData().get(i).get(1)>-999){
                grandTotalEntries.addEntry(grandTotals.get(0).getData().get(i).get(1).floatValue(),grandTotals.get(0).getData().get(i).get(0).longValue());
            }
            else{
                grandTotalEntries.addEntry(null,null);
            }
            if(i<grandTotals.get(1).getData().size() && grandTotals.get(1).getData().get(i).get(1)>-999){
                peerGrandTotalEntries.addEntry(grandTotals.get(1).getData().get(i).get(1).floatValue(),grandTotals.get(0).getData().get(i).get(0).longValue());
            }
            else{
                peerGrandTotalEntries.addEntry(null,null);
            }
            if(i<grandTotals.get(2).getData().size() &&grandTotals.get(2).getData().get(i).get(1)>-999){
                hsGrandTotalEntries.addEntry(grandTotals.get(2).getData().get(i).get(1).floatValue(),grandTotals.get(0).getData().get(i).get(0).longValue());
            }
            else{
                hsGrandTotalEntries.addEntry(null,null);
            }
        }
        grandTotalChart.addLine(grandTotalEntries);
        grandTotalChart.addLine(peerGrandTotalEntries);
        grandTotalChart.addLine(hsGrandTotalEntries);
        grandTotalChart.drawChart(30);

    }

}
