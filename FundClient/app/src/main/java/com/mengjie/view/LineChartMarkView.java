package com.mengjie.view;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.mengjie.activity.R;

public class LineChartMarkView extends MarkerView {

    private String xLabel;

    private TextView tvDate;
    private TextView tvValue;
    private ValueFormatter valueFormatter;

    public LineChartMarkView(Context context, ValueFormatter valueFormatter, String xLabel) {
        super(context, R.layout.layout_chart_markview);
        this.xLabel=xLabel;
        this.valueFormatter=valueFormatter;

        tvDate = findViewById(R.id.tv_date);
        tvValue = findViewById(R.id.tv_value);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        tvDate.setText(valueFormatter.getAxisLabel(e.getX(),null));
        tvValue.setText(xLabel+": " + e.getY());
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

}
