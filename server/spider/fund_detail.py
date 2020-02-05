import time

import asyncio
import aiohttp
import execjs

FUND_LIST_GET_URL = "http://fund.eastmoney.com/Data/Fund_JJJZ_Data.aspx"
FUND_NET_VALUE_URL = "http://api.fund.eastmoney.com/f10/lsjz"
FUND_DETAIL_URL = "http://fund.eastmoney.com/pingzhongdata/{}.js"

"""
/*基金或股票信息*/
var fS_name = "泓德裕泰债券A";
var fS_code = "002138";
/*原费率*/
var fund_sourceRate = "0.80";
/*现费率*/
var fund_Rate = "0.08";
/*最小申购金额*/
var fund_minsg = "100";
/*基金持仓股票代码*/
var stockCodes = [];
/*基金持仓债券代码*/
var zqCodes = "1361391,1086022,0180071";
/*基金持仓股票代码(新市场号)*/
var stockCodesNew = [];
/*基金持仓债券代码（新市场号）*/
var zqCodesNew = "1.136139,0.108602,1.018007";
/*收益率*/
/*近一年收益率*/
var syl_1n = "12.2544";
/*近6月收益率*/
var syl_6y = "12.0448";
/*近三月收益率*/
var syl_3y = "4.4387";
/*近一月收益率*/
var syl_1y = "2.1276";
/*股票仓位测算图*/
var Data_fundSharesPositions = [];
/*单位净值走势 equityReturn-净值回报 unitMoney-每份派送金*/
var Data_netWorthTrend = []
/*累计净值走势*/
var Data_ACWorthTrend = [];
/*累计收益率走势*/
var Data_grandTotal = [];
/*同类排名走势*/
var Data_rateInSimilarType = [{
    "x": 1458144000000,
    "y": 237,
    "sc": "638"
}];

var Data_rateInSimilarPersent = [[1458144000000, 62.8500],];
/*规模变动 mom-较上期环比*/
var Data_fluctuationScale = {
    "categories": ["2018-12-31", "2019-03-31", "2019-06-30", "2019-09-30", "2019-12-31"],
    "series": [{
        "y": 29.81,
        "mom": "-10.27%"
    }, {
        "y": 28.89,
        "mom": "-3.08%"
    }, {
        "y": 27.76,
        "mom": "-3.91%"
    }, {
        "y": 28.11,
        "mom": "1.26%"
    }, {
        "y": 33.26,
        "mom": "18.32%"
    }]
};
/*持有人结构*/
var Data_holderStructure = {
    "series": [{
        "name": "机构持有比例",
        "data": [99.95, 99.87, 99.91, 99.88]
    }, {
        "name": "个人持有比例",
        "data": [0.05, 0.13, 0.09, 0.12]
    }, {
        "name": "内部持有比例",
        "data": [0.0001, 0.0, 0.0021, 0.0002]
    }],
    "categories": ["2017-12-31", "2018-06-30", "2018-12-31", "2019-06-30"]
};
/*资产配置*/
var Data_assetAllocation = {
    "series": [{
        "name": "股票占净比",
        "type": null,
        "data": [0, 0, 0, 0],
        "yAxis": 0
    }, {
        "name": "债券占净比",
        "type": null,
        "data": [96.3, 100.17, 108.9, 107.85],
        "yAxis": 0
    }, {
        "name": "现金占净比",
        "type": null,
        "data": [0.03, 0.04, 2.1, 1.08],
        "yAxis": 0
    }, {
        "name": "净资产",
        "type": "line",
        "data": [32.1563119583, 30.6990515721, 30.4606539672, 34.5196830457],
        "yAxis": 1
    }],
    "categories": ["2019-03-31", "2019-06-30", "2019-09-30", "2019-12-31"]
};
/*业绩评价 ['选股能力', '收益率', '抗风险', '稳定性','择时能力']*/
var Data_performanceEvaluation = {
    "avr": "64.00",
    "categories": ["选证能力", "收益率", "抗风险", "稳定性", "管理规模"],
    "dsc": ["反映基金挑选证券而实现风险调整\u003cbr\u003e后获得超额收益的能力", "根据阶段收益评分，反映基金的盈利能力", "反映基金投资收益的回撤情况", "反映基金投资收益的波动性", "根据基金的资产规模评分"],
    "data": [60.0, 80.0, 50.0, 40.0, 80.0]
};
/*现任基金经理*/
var Data_currentFundManager = [{
    "id": "30410909",
    "pic": "https://pdf.dfcfw.com/pdf/H8_JPG30410909_1.jpg",
    "name": "李倩",
    "star": 4,
    "workTime": "4年又42天",
    "fundSize": "181.51亿(23只基金)",
    "power": {
        "avr": "61.34",
        "categories": ["经验值", "收益率", "抗风险", "稳定性", "管理规模"],
        "dsc": ["反映基金经理从业年限和管理基金的经验", "根据基金经理投资的阶段收益评分，反映\u003cbr\u003e基金经理投资的盈利能力", "反映基金经理投资的回撤控制能力", "反映基金经理投资收益的波动", "根据基金经理现任管理基金资产规模评分"],
        "data": [63.0, 89.40, 27.70, 24.80, 79.50],
        "jzrq": "2020-01-23"
    },
    "profit": {
        "categories": ["任期收益", "同类平均"],
        "series": [{
            "data": [{
                "name": null,
                "color": "#7cb5ec",
                "y": 30.4843
            }, {
                "name": null,
                "color": "#414c7b",
                "y": 12.12
            }]
        }],
        "jzrq": "2020-01-23"
    }
}];
/*申购赎回*/
var Data_buySedemption = {
    "series": [{
        "name": "期间申购",
        "data": [0.78, 0.02, 5.76, 9.41]
    }, {
        "name": "期间赎回",
        "data": [1.67, 1.20, 7.18, 6.02]
    }, {
        "name": "总份额",
        "data": [27.21, 26.04, 24.61, 28.00]
    }],
    "categories": ["2019-03-31", "2019-06-30", "2019-09-30", "2019-12-31"]
};
/*同类型基金涨幅榜（页面底部通栏）*/
var swithSameType = [['004222_金信民旺债券A_11.34', '004402_金信民旺债券C_11.30', '675011_西部利得稳健双利债券_7.86', '675013_西部利得稳健双利债券_7.83', '000297_鹏华可转债债券_7.34'], ['007372_国联安增瑞政策性金融_13.61', '005461_南方希元转债_13.41', '001045_华夏可转债增强债券A_13.10', '000297_鹏华可转债债券_12.43', '710301_富安达增强收益债券A_12.23'], ['005461_南方希元转债_25.47', '005480_诺安联创顺鑫C_20.56', '005448_诺安联创顺鑫A_19.97', '000297_鹏华可转债债券_19.75', '240018_华宝可转债债券A_19.16'], ['005461_南方希元转债_38.19', '003510_长盛可转债债券A_34.75', '003511_长盛可转债债券C_34.55', '240018_华宝可转债债券A_34.00', '050019_博时转债增强债券A_31.79'], ['320021_诺安双利债券发起_53.71', '470058_汇添富可转换债券A_33.53', '470059_汇添富可转换债券C_31.96', '002459_华夏鼎利债券发起式A_30.95', '002460_华夏鼎利债券发起式C_30.52']];

"""


def js_to_dict(js_code):
    """
    """

    detail_keyword = ["fS_name", "fS_code", "fund_sourceRate", "fund_Rate", "fund_minsg", "stockCodes", "zqCodes",
                      "stockCodesNew", "zqCodesNew", "syl_1n", "syl_6y", "syl_3y", "syl_1y "]
    need_keyword = ['Data_fundSharesPositions', 'Data_ACWorthTrend', 'Data_grandTotal', 'Data_rateInSimilarType',
                    'Data_rateInSimilarPersent', 'Data_fluctuationScale', 'Data_holderStructure',
                    'Data_assetAllocation', 'Data_performanceEvaluation', 'Data_currentFundManager',
                    'Data_buySedemption', 'Data_netWorthTrend']
    ctx = execjs.compile(js_code)
    detail = {i: ctx.eval(i) for i in detail_keyword}
    data = {i: ctx.eval(i) for i in need_keyword}
    data['detail'] = detail
    return data


async def get_fund_data(fund_code: str):
    async with aiohttp.ClientSession() as session:
        async with session.get(FUND_DETAIL_URL.format(fund_code)) as response:
            if response.status == 200:
                res = await response.text()
            else:
                res = None
    if res:
        data = await asyncio.get_event_loop().run_in_executor(None, js_to_dict, res)
    else:
        data = None
    return data


async def get_fund_net_values(fund_code: str, page: int, page_size: int = 20):
    """
    :param fund_code:
    :param page:
    :param page_size:
    :return:
    http://api.fund.eastmoney.com/f10/lsjz
    ?callback=jQuery18301983556540064817_1580452922854&fundCode=002138&
    pageIndex=49&pageSize=20&startDate=&endDate=&_=1580453700917
    """
    params = {"fundCode": fund_code, "pageIndex": page, "pageSize": 20,
              "_": int(time.time() * 1000), "callback": "jQuery18301983556540064817_1580452922854"}
    async with aiohttp.ClientSession() as session:
        async with session.get(FUND_NET_VALUE_URL, params=params) as response:
            res = await response.text()
            print(response.url)
    print(res)


"""
http://fund.eastmoney.com/pingzhongdata/002138.js?v=20200131145914
"""

if __name__ == '__main__':
    loop = asyncio.get_event_loop()
    result = loop.run_until_complete(get_fund_data('002138'))
    loop.close()
