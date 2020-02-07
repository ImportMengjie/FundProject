import time
import json

import asyncio
import aiohttp

"""
http://fundsuggest.eastmoney.com/FundSearch/api/FundSearchAPI.ashx?
callback=jQuery18306265164857606453_1581050712372&m=1&key=changcheng&_=1581050727344

http://fundsuggest.eastmoney.com/FundSearch/api/FundSearchPageAPI.ashx
?callback=jQuery18309076342312481094_1581051165615&m=0&key=%E9%95%BF%E5%9F%8E&_=1581051175268
"""

FUND_SEARCH_URL = "http://fundsuggest.eastmoney.com/FundSearch/api/FundSearchPageAPI.ashx?m={}&_={}&key={}"


async def get_fund_search_list(keyword: str):
    url = FUND_SEARCH_URL.format(0, int(time.time()*1000), keyword)
    response_dict = None
    async with aiohttp.ClientSession() as session:
        async with session.get(url) as response:
            res = await response.text()
    if res:
        response_dict = json.loads(res)
    return response_dict

if __name__ == '__main__':
    loop = asyncio.get_event_loop()
    result = loop.run_until_complete(get_fund_search_list('长城'))
    loop.close()
