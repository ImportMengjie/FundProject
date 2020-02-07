
def format_fund_search_data(data: dict) -> dict:
    new_data = {'datas': [[d['CODE'], d['NAME']] for d in data['Datas']['FundList']], 'record': len(
        data['Datas']['FundList']), 'pages': 1, 'curpage': 1}

    return new_data
