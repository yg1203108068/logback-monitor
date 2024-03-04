const {createProxyMiddleware} = require('http-proxy-middleware')

// api 代理
const apiServerProxy = createProxyMiddleware("/api", {
    target: 'http://localhost:8080',
    // pathRewrite: {
    //     '^/api': '/',
    // },
})


// 注册代理中间件
module.exports = function (app) {
    app.use(apiServerProxy);
}