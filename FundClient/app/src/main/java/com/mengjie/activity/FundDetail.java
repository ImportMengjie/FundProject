package com.mengjie.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.mengjie.bean.FundBean;
import com.mengjie.dao.Fund;
import com.mengjie.view.LineChartMarkView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FundDetail extends AppCompatActivity {

    private Fund fund=null;

    private Handler handler;
    private List<Entry> netWorthEntries=null;
    private List<Entry> acNetWorthEntries=null;
    private LineChart netWorthChart;
    private LineChart acNetWorthChart;

    private ProgressDialog progressDialog = null;


    private ValueFormatter acNetWorthValueFormatterDate = new ValueFormatter() {
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            Date date = new Date(FundDetail.this.fund.getFundBean().getData_ACWorthTrend().get((int)value).get(0).longValue());
            return String.format("%02d月%02d日",date.getMonth()+1,date.getDate());
        }
    };
    private ValueFormatter acNetWorthValueFormatterYear =new ValueFormatter() {
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            Date date = new Date(FundDetail.this.fund.getFundBean().getData_ACWorthTrend().get((int)value).get(0).longValue());
            return String.format("%04d年%02d月",date.getYear()+1900,date.getMonth()+1);
        }
    };

    private ValueFormatter netWorthValueFormatterDate = new ValueFormatter() {
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            Date date = new Date(FundDetail.this.fund.getFundBean().getData_netWorthTrend().get((int)value).getX());
            return String.format("%02d月%02d日",date.getMonth()+1,date.getDate());
        }
    };
    private ValueFormatter netWorthValueFormatterYear =new ValueFormatter() {
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            Date date = new Date(FundDetail.this.fund.getFundBean().getData_netWorthTrend().get((int)value).getX());
            return String.format("%04d年%02d月",date.getYear()+1900,date.getMonth()+1);
        }
    };

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

        setNetWorthButton();
        setAcNetWorthButton();
    }

    public void setNetWorthButton(){
        Button netWorthChartAllButton = findViewById(R.id.netWorthChartAllButton);
        Button netWorthChartOneYearButton = findViewById(R.id.netWorthChartOneYearButton);
        Button netWorthChartHalfYearButton = findViewById(R.id.netWorthChartHalfYearButton);
        Button netWorthChartOneMouthButton= findViewById(R.id.netWorthChartOneMouthButton);

        netWorthChartAllButton.setOnClickListener(v -> {
            if(netWorthEntries!=null)
                drawChart(netWorthEntries, netWorthChart, netWorthValueFormatterYear,"netWorth",720);
        });
        netWorthChartOneYearButton.setOnClickListener(v -> {
            if(netWorthEntries!=null)
                drawChart(netWorthEntries.subList(netWorthEntries.size()-360,netWorthEntries.size()), netWorthChart, netWorthValueFormatterYear,"netWorth",120);
        });
        netWorthChartHalfYearButton.setOnClickListener(v -> {
            if(netWorthEntries!=null)
                drawChart(netWorthEntries.subList(netWorthEntries.size()-180,netWorthEntries.size()), netWorthChart, netWorthValueFormatterDate,"netWorth",60);
        });
        netWorthChartOneMouthButton.setOnClickListener(v -> {
            if(netWorthEntries!=null)
                drawChart(netWorthEntries.subList(netWorthEntries.size()-30,netWorthEntries.size()), netWorthChart, netWorthValueFormatterDate,"netWorth",10);
        });
    }


    public void setAcNetWorthButton(){
        Button acNetWorthChartAllButton = findViewById(R.id.acNetWorthChartAllButton);
        Button acNetWorthChartOneYearButton = findViewById(R.id.acNetWorthChartOneYearButton);
        Button acNetWorthChartHalfYearButton = findViewById(R.id.acNetWorthChartHalfYearButton);
        Button acNetWorthChartOneMouthButton= findViewById(R.id.acNetWorthChartOneMouthButton);

        acNetWorthChartAllButton.setOnClickListener(v -> {
            if(acNetWorthEntries!=null)
                drawChart(acNetWorthEntries, acNetWorthChart, netWorthValueFormatterYear,"netWorth",720);
        });
        acNetWorthChartOneYearButton.setOnClickListener(v -> {
            if(acNetWorthEntries!=null)
                drawChart(acNetWorthEntries.subList(acNetWorthEntries.size()-360,acNetWorthEntries.size()), acNetWorthChart, acNetWorthValueFormatterYear,"acNetWorth",120);
        });
        acNetWorthChartHalfYearButton.setOnClickListener(v -> {
            if(acNetWorthEntries!=null)
                drawChart(acNetWorthEntries.subList(acNetWorthEntries.size()-180,acNetWorthEntries.size()), acNetWorthChart, acNetWorthValueFormatterDate,"acNetWorth",60);
        });
        acNetWorthChartOneMouthButton.setOnClickListener(v -> {
            if(acNetWorthEntries!=null)
                drawChart(acNetWorthEntries.subList(acNetWorthEntries.size()-30,acNetWorthEntries.size()), acNetWorthChart, acNetWorthValueFormatterDate,"acNetWorth",10);
        });
    }

    public void drawChart(List<Entry> data, LineChart lineChart, ValueFormatter valueFormatter, String label, int Granularity){

        lineChart.setDrawGridBackground(false);
        lineChart.setDrawBorders(true);
        lineChart.setDragEnabled(true);
        lineChart.animateXY(300,300);

        LineChartMarkView lineChartMarkView = new LineChartMarkView(this,valueFormatter,label);
        lineChartMarkView.setChartView(lineChart);
        lineChart.setMarker(lineChartMarkView);

        LineDataSet lineDataSet = new LineDataSet(data,"");

        lineDataSet.setCircleHoleRadius(0.1f);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setDrawValues(false);
        lineDataSet.setFillDrawable(getResources().getDrawable(R.drawable.fade_blue));

        LineData lineData = new LineData(lineDataSet);


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(Granularity);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);


        xAxis.setValueFormatter(valueFormatter);

        Legend legend = lineChart.getLegend();

        legend.setForm(Legend.LegendForm.LINE);
        legend.setDrawInside(false);

        lineChart.setData(lineData);
        lineChart.invalidate();

    }

    public void paintData(){
        netWorthChart = findViewById(R.id.netWorthChart);
        acNetWorthChart = findViewById(R.id.acNetWorthChart);

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

        netWorthEntries = new ArrayList<>();
        List<FundBean.NetWorth> data_netWorthTrend = fund.getFundBean().getData_netWorthTrend();
        for (int i = 0; i < data_netWorthTrend.size(); i++) {
            FundBean.NetWorth netWorth = data_netWorthTrend.get(i);
            if(netWorth.getY()>=0)
                netWorthEntries.add(new Entry(i, netWorth.getY().floatValue()));
        }
        this.drawChart(netWorthEntries.subList(netWorthEntries.size()-30,netWorthEntries.size()),netWorthChart, netWorthValueFormatterDate,"netWorth",3);

        acNetWorthEntries = new ArrayList<>();
        List<List<Double>> data_acWorthTrend = fund.getFundBean().getData_ACWorthTrend();
        for(int i=0;i<data_acWorthTrend.size();i++){
            if(data_acWorthTrend.get(i).get(1)>=0){
                acNetWorthEntries.add(new Entry(i,data_acWorthTrend.get(i).get(1).floatValue()));
            }
        }
        this.drawChart(acNetWorthEntries.subList(acNetWorthEntries.size()-30,acNetWorthEntries.size()),acNetWorthChart, acNetWorthValueFormatterDate,"acNetWorth",3);


    }

}
