package com.mengjie.bean;

import java.util.List;

public class FundBean {

    private Detail detail;
    private DataHolderStructure Data_holderStructure;
    private DataPerformanceEvaluation Data_performanceEvaluation;
    private List<FundManager> Data_currentFundManager;

    private List<NetWorth> Data_netWorthTrend;
    private List<List<Double>> Data_ACWorthTrend;

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public DataHolderStructure getData_holderStructure() {
        return Data_holderStructure;
    }

    public void setData_holderStructure(DataHolderStructure data_holderStructure) {
        Data_holderStructure = data_holderStructure;
    }

    public DataPerformanceEvaluation getData_performanceEvaluation() {
        return Data_performanceEvaluation;
    }

    public void setData_performanceEvaluation(DataPerformanceEvaluation data_performanceEvaluation) {
        Data_performanceEvaluation = data_performanceEvaluation;
    }

    public List<FundManager> getData_currentFundManager() {
        return Data_currentFundManager;
    }

    public void setData_currentFundManager(List<FundManager> data_currentFundManager) {
        Data_currentFundManager = data_currentFundManager;
    }

    public List<NetWorth> getData_netWorthTrend() {
        return Data_netWorthTrend;
    }

    public void setData_netWorthTrend(List<NetWorth> data_netWorthTrend) {
        Data_netWorthTrend = data_netWorthTrend;
    }

    public List<List<Double>> getData_ACWorthTrend() {
        return Data_ACWorthTrend;
    }

    public void setData_ACWorthTrend(List<List<Double>> data_ACWorthTrend) {
        Data_ACWorthTrend = data_ACWorthTrend;
    }


    public class Detail{
        private String fS_name;
        private String fS_code;
        private String fund_sourceRate;
        private String fund_Rate;
        private String syl_1n;
        private String syl_6y;
        private String syl_3y;
        private String syl_1y;

        public String getfS_name() {
            return fS_name;
        }

        public void setfS_name(String fS_name) {
            this.fS_name = fS_name;
        }

        public String getfS_code() {
            return fS_code;
        }

        public void setfS_code(String fS_code) {
            this.fS_code = fS_code;
        }

        public String getFund_sourceRate() {
            return fund_sourceRate;
        }

        public void setFund_sourceRate(String fund_sourceRate) {
            this.fund_sourceRate = fund_sourceRate;
        }

        public String getFund_Rate() {
            return fund_Rate;
        }

        public void setFund_Rate(String fund_Rate) {
            this.fund_Rate = fund_Rate;
        }

        public String getSyl_1n() {
            return syl_1n;
        }

        public void setSyl_1n(String syl_1n) {
            this.syl_1n = syl_1n;
        }

        public String getSyl_6y() {
            return syl_6y;
        }

        public void setSyl_6y(String syl_6y) {
            this.syl_6y = syl_6y;
        }

        public String getSyl_3y() {
            return syl_3y;
        }

        public void setSyl_3y(String syl_3y) {
            this.syl_3y = syl_3y;
        }

        public String getSyl_1y() {
            return syl_1y;
        }

        public void setSyl_1y(String syl_1y) {
            this.syl_1y = syl_1y;
        }
    }

    public class DataPerformanceEvaluation{
        private String avr;
        private List<String> categories;
        private List<String> dsc;
        private List<String> data;

        public String getAvr() {
            return avr;
        }

        public void setAvr(String avr) {
            this.avr = avr;
        }

        public List<String> getCategories() {
            return categories;
        }

        public void setCategories(List<String> categories) {
            this.categories = categories;
        }

        public List<String> getDsc() {
            return dsc;
        }

        public void setDsc(List<String> dsc) {
            this.dsc = dsc;
        }

        public List<String> getData() {
            return data;
        }

        public void setData(List<String> data) {
            this.data = data;
        }
    }



    public class FundManager{
        private String id;
        private String pic;
        private String name;
        private String workTime;
        private String fundSize;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getWorkTime() {
            return workTime;
        }

        public void setWorkTime(String workTime) {
            this.workTime = workTime;
        }

        public String getFundSize() {
            return fundSize;
        }

        public void setFundSize(String fundSize) {
            this.fundSize = fundSize;
        }
    }

    public class NetWorth{
        private Long x;
        private Double y;
        private String equityReturn;
        private String unitMoney;

        public Long getX() {
            return x;
        }

        public void setX(Long x) {
            this.x = x;
        }

        public Double getY() {
            return y;
        }

        public void setY(Double y) {
            this.y = y;
        }

        public String getEquityReturn() {
            return equityReturn;
        }

        public void setEquityReturn(String equityReturn) {
            this.equityReturn = equityReturn;
        }

        public String getUnitMoney() {
            return unitMoney;
        }

        public void setUnitMoney(String unitMoney) {
            this.unitMoney = unitMoney;
        }
    }

    public class DataHolderStructure{
        private List<Series> series;

        public List<Series> getSeries() {
            return series;
        }

        public void setSeries(List<Series> series) {
            this.series = series;
        }

        public class Series{
            private String name;
            private List<Float> data;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<Float> getData() {
                return data;
            }

            public void setData(List<Float> data) {
                this.data = data;
            }
        }
    }
}
