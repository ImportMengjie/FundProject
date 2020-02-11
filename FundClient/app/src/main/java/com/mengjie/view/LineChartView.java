package com.mengjie.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.IntegerRes;
import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.mengjie.activity.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LineChartView extends LinearLayout {

    private Button chartAllButton;
    private Button chartOneYearButton;
    private Button chartHalfYearButton;
    private Button chartOneMouthButton;
    private LineChart lineChart;
    private List<Button> buttons = new ArrayList<>();
    private int currentButtonIds=0;
    private static int[] intervalDayButtons = new int[]{30,180,360,-1};

    private List<EntryList> linesEntryList = new ArrayList<>();

    public interface DateFormatter{

        String getAxisLabel(int year, int mouth, int day, AxisBase axisBase);

    }

    private static DateFormatter yearDateFormatter = new DateFormatter() {
        @Override
        public String getAxisLabel(int year, int mouth, int day, AxisBase axisBase) {
            return String.format("%04d年%02d月",year,mouth);
        }
    };

    private static DateFormatter mouthDateFormatter = new DateFormatter() {
        @Override
        public String getAxisLabel(int year, int mouth, int day, AxisBase axisBase) {
            return String.format("%02d月%02d日",mouth,day);
        }
    };

    private static DateFormatter allDateFormatter = new DateFormatter() {
        @Override
        public String getAxisLabel(int year, int mouth, int day, AxisBase axisBase) {
            return String.format("%04d年%02d月%02d日",year,mouth,day);
        }
    };

    public static class EntryList {
        private String prefix;
        private String suffix;

        private List<Entry> entries = new ArrayList<>();
        private List<Long> timestamps = new ArrayList<>();
        private List<Integer> ids = new ArrayList<>();
        private int currentIndex = 0;
        private int color;


        public EntryList(String prefix, String suffix, int color){
            this.prefix=prefix;
            this.suffix=suffix;
            this.color = color;
        }
        public void addEntry(Float y, Long timestamp){
            if(y!=null && timestamp != null){
                entries.add(new Entry(ids.size(), y));
                timestamps.add(timestamp);
                ids.add(currentIndex);
                currentIndex++;
            }
            else
                ids.add(currentIndex);
        }

        public ValueFormatter getValueFormatter(DateFormatter dateFormatter){
            return new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    Date date = new Date(timestamps.get(ids.get((int)value)));
                    return dateFormatter.getAxisLabel(date.getYear()+1900,date.getMonth()+1,date.getDate(),axis);
                }
            };
        }

        public Entry getEntry(int index){
            return entries.get(ids.get(index));
        }

        public String getPrefix() {
            return prefix;
        }

        public String getSuffix() {
            return suffix;
        }


        public List<Entry> getEntries() {
            return entries;
        }

        public List<Entry> getEntries(Integer intervalDays){
            if(intervalDays==null) return entries;
            if(intervalDays>ids.size()) return null;
            return entries.subList(ids.get(ids.size()-intervalDays),entries.size());
        }

        public float getGranularity(Integer intervalDays, int count){
            if(intervalDays!=null) return intervalDays;
            return 361;
        }

        public int getColor() {
            return color;
        }
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.activity_fund_chart, this);

        chartAllButton = findViewById(R.id.chartAllButton);
        lineChart = findViewById(R.id.chart);
        chartOneYearButton = findViewById(R.id.chartOneYearButton);
        chartHalfYearButton = findViewById(R.id.chartHalfYearButton);
        chartOneMouthButton= findViewById(R.id.chartOneMouthButton);
        buttons.add(chartOneMouthButton);
        buttons.add(chartHalfYearButton);
        buttons.add(chartOneYearButton);
        buttons.add(chartAllButton);
        chartOneMouthButton.setEnabled(false);
        this.initButtons();
    }


    public void addLine(EntryList entryList){
        this.linesEntryList.add(entryList);
    }

    public boolean drawChart(Integer intervalDays){
        lineChart = findViewById(R.id.chart);
        this.initLineChart();
        LineChartMarkView lineChartMarkView = new LineChartMarkView(this.getContext(), linesEntryList,allDateFormatter);
        lineChartMarkView.setChartView(lineChart);
        lineChart.setMarker(lineChartMarkView);
        List<ILineDataSet> dataSets = new ArrayList<>();
        for (int i = 0; i < linesEntryList.size(); i++) {
            EntryList entryList = linesEntryList.get(i);
            List<Entry> entries = entryList.getEntries(intervalDays);
            if(entries==null) continue;
            LineDataSet lineDataSet = new LineDataSet(entries, entryList.getPrefix());
            lineDataSet.setCircleHoleRadius(0.5f);
            lineDataSet.setDrawCircles(false);
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFormLineWidth(1f);
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet.setDrawValues(false);
//            lineDataSet.setFillDrawable(getResources().getDrawable(R.drawable.fade_blue));
            lineDataSet.setColor(entryList.color);
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFormLineWidth(1f);
            lineDataSet.setFormSize(15.f);

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setGranularity(entryList.getGranularity(intervalDays, 3));
            xAxis.setValueFormatter(entryList.getValueFormatter(intervalDays==null||intervalDays>180?yearDateFormatter:mouthDateFormatter));

            dataSets.add(lineDataSet);
        }
        if(dataSets.size()==0)
            return false;
        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);
        lineChart.invalidate();
        return true;
    }

    private void initLineChart(){
        lineChart.setDrawGridBackground(false);
        lineChart.setDrawBorders(true);
        lineChart.setDragEnabled(true);
        lineChart.animateXY(300,300);


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        YAxis rightYAxis = lineChart.getAxisRight();
        rightYAxis.setDrawGridLines(false);
        rightYAxis.setEnabled(false);

        YAxis leftYAxis = lineChart.getAxisLeft();
        leftYAxis.setDrawGridLines(true);
        leftYAxis.enableGridDashedLine(10f, 10f, 0f);


        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setDrawInside(false);

        Description description = new Description();
        description.setEnabled(false);
        lineChart.setDescription(description);
    }

    private void buttonClick(int num){
        buttons.get(currentButtonIds).setEnabled(true);
        currentButtonIds=num;
        buttons.get(num).setEnabled(false);
        drawChart(intervalDayButtons[num]>0?intervalDayButtons[num]:null);
    }

    private void initButtons(){
        chartOneMouthButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick(0);
            }
        });
        chartHalfYearButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick(1);
            }
        });
        chartOneYearButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick(2);
            }
        });
        chartAllButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick(3);
            }
        });

    }
}
