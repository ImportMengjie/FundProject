package com.mengjie.bean;

import java.util.ArrayList;
import java.util.List;

public class FundListBean {

    private List<List<String>> datas;
    private List<Integer> count;
    private int record;
    private int pages;
    private int curpage;

    private List<FundBean> fundBeans=null;

    public List<FundBean> getFundBeanList(){
        if(fundBeans==null){
            fundBeans = new ArrayList<>(datas.size());

            for(List<String> data:datas)
                fundBeans.add(new FundBean(data));
        }
        return fundBeans;
    }

    public class FundBean{
        public String code;
        public String name;

        FundBean(List<String> data){
            code = data.get(0);
            name = data.get(1);
        }
        FundBean(){}

    }
    public List<List<String>> getDatas() {
        return datas;
    }

    public void setDatas(List<List<String>> datas) {
        this.datas = datas;
    }

    public List<Integer> getCount() {
        return count;
    }

    public void setCount(List<Integer> count) {
        this.count = count;
    }

    public int getRecord() {
        return record;
    }

    public void setRecord(int record) {
        this.record = record;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getCurpage() {
        return curpage;
    }

    public void setCurpage(int curpage) {
        this.curpage = curpage;
    }
}
