import json

import asyncio
import aiohttp

FUND_LIST_GET_URL = "http://fund.eastmoney.com/Data/Fund_JJJZ_Data.aspx"


def add_double_quotes(data: str) -> str:
    data = data[data.find('{'):]
    i: int = 0
    while i < len(data):
        if i > 0 and data[i].isalpha() and data[i - 1] in ('{', ','):
            data = data[:i] + '"' + data[i:]
            i += 1
        elif i < len(data) - 1 and data[i].isalpha() and data[i + 1] in ':':
            data = data[:i + 1] + '"' + data[i + 1:]
            i += 1
        elif i < len(data) - 1 and data[i] == ',' and data[i + 1] in ']':
            data = data[:i] + data[i + 1:]
            i -= 1
        i += 1
    return data


async def get_fund_list(page: int, page_size: int = 200, sort=('zdf', 'desc')):
    """
    :param sort:
    :param page:
    :param page_size:
    :return:
    http://fund.eastmoney.com/Data/Fund_JJJZ_Data.aspx?
    t=1&lx=1&letter=&gsid=&text=&sort=zdf,desc&page=2,200&dt=1580448371575&atfc=&onlySale=0
    """
    response_dict = None
    url = FUND_LIST_GET_URL + '?page={},{}&sort={}'.format(page, page_size, ','.join(sort))
    async with aiohttp.ClientSession() as session:
        async with session.get(url) as response:
            res = await response.text()
    if res:
        response_dict = json.loads(add_double_quotes(res))
    return response_dict


if __name__ == '__main__':
    loop = asyncio.get_event_loop()
    result = loop.run_until_complete(get_fund_list(1))
    loop.close()
