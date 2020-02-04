from aiohttp import web

import spider

fund_routes = web.RouteTableDef()


@fund_routes.view('/api/fund')
class FundList(web.View):

    async def get(self):
        fund_list = await spider.get_fund_list(self.request.query.get('page', 1), self.request.query.get('pagesize', 200), )
        return web.json_response(fund_list)


@fund_routes.view('/api/fund/{fund_code}')
class Fund(web.View):

    async def get(self):
        fund_code = self.request.match_info.get('fund_code', None)
        if not fund_code:
            return web.json_response({'msg': 'no fund code'}, status=400)
        data = await spider.get_fund_data(fund_code)
        if data:
            return web.json_response(data)
        else:
            return web.json_response({'msg': 'not fund'}, status=404)
