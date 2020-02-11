
import time
import datetime


def format_fund_detail_data(data: dict) -> dict:
    data['Data_netWorthTrend'] = handle_net_worthtrend(
        data['Data_netWorthTrend'])
    data['Data_ACWorthTrend'] = handle_ac_net_worthtrend(
        data['Data_ACWorthTrend'])
    for d in data['Data_grandTotal']:
        d['data'] = handle_ac_net_worthtrend(d['data'])
    return data


def handle_net_worthtrend(array: list) -> list:
    new_array = []
    if len(array):
        start_datetime = datetime.datetime.fromtimestamp(array[0]['x']//1000)
        end_datetime = datetime.datetime.fromtimestamp(array[-1]['x']//1000)
        current_datetime = start_datetime
        idx = 0
        while current_datetime <= end_datetime:
            if array[idx]['x']//1000 == current_datetime.timestamp():
                item = array[idx]
                idx += 1
            else:
                item = {'x': current_datetime.timestamp()*1000, 'y': -1,
                        'equityReturn': 0, 'unitMoney': ''}
            new_array.append(item)
            current_datetime += datetime.timedelta(days=1)

    return new_array


def handle_ac_net_worthtrend(array: list) -> list:
    new_array = []
    if len(array):
        start_datetime = datetime.datetime.fromtimestamp(array[0][0]//1000)
        end_datetime = datetime.datetime.fromtimestamp(array[-1][0]//1000)
        current_datetime = start_datetime
        idx = 0
        while current_datetime <= end_datetime:
            if array[idx][0]//1000 == current_datetime.timestamp():
                item = array[idx]
                idx += 1
            else:
                item = [current_datetime.timestamp()*1000, -999]
            new_array.append(item)
            current_datetime += datetime.timedelta(days=1)

    return new_array
