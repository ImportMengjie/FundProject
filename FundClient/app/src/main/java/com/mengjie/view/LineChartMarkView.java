package com.mengjie.view;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.mengjie.activity.R;

import java.util.List;

public class LineChartMarkView extends MarkerView {

    private TextView tvDate;
    private TextView tvValue;

    private LineChartView.DateFormatter dateFormatter;
    private List<LineChartView.EntryList> entryListData;

    public LineChartMarkView(Context context, List<LineChartView.EntryList> entryListData, LineChartView.DateFormatter dateFormatter) {
        super(context, R.layout.layout_chart_markview);

        tvDate = findViewById(R.id.tv_date);
        tvValue = findViewById(R.id.tv_value);

        this.dateFormatter = dateFormatter;
        this.entryListData = entryListData;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if(entryListData.size()>0){
            tvDate.setText(entryListData.get(0).getValueFormatter(dateFormatter).getAxisLabel(e.getX(),null));
            StringBuilder value= new StringBuilder();
            for (int i = 0; i < entryListData.size(); i++) {
                LineChartView.EntryList entryList = entryListData.get(i);
                String t = entryList.getPrefix()+":"+entryList.getEntry((int)e.getX()).getY()+entryList.getSuffix()+"\n";
                value.append(t);
            }
            tvValue.setText(value.toString());
        }else{
            tvDate.setText("null");
            tvValue.setText("null");
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

}
