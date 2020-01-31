from aiohttp import web

import resources


def make_app():
    app = web.Application()
    app.router.add_routes(resources.fund_routes)
    web.run_app(app)


if __name__ == '__main__':
    make_app()
