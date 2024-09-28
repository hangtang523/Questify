const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
    transpileDependencies: true,
    devServer: {
        port: 8080,
        open: true,
        client: {
            progress: true,
            overlay: false
        },
        //before: require('./mock/mock-server.js')
        proxy: {
            '/dev-api': { // 匹配所有以 '/dev-api'开头的请求路径
                target: 'http://localhost:9090',
                changeOrigin: true, // 支持跨域
                pathRewrite: { // 重写路径: 去掉路径中开头的'/dev-api'
                    '^/dev-api': ''
                }
            }
        }
    },
    chainWebpack: config => {
        config.plugin('html')
            .tap(args => {
                args[0].title = "Questify";
                return args;
            })
    }
})
