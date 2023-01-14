// 导入 express 模块
const express = require('express')
// 导入 cors 中间件
const cors = require('cors')


// 创建 express 的服务器实例
const app = express()
// 将 cors 注册为全局中间件
app.use(cors())

// 导入并注册用户路由模块
const userRouter = require('./router/user')
app.use('/api', userRouter)
// 导入并注册用户路由模块
const historyRouter = require('./router/history')
app.use('/api', historyRouter)


// 调用 app.listen 方法，指定端口号并启动web服务器
app.listen(8090, function () {
console.log('api server running at https://6737k8d627.goho.co/')
})
