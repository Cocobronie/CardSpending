const express = require('express')
const querystring = require('querystring')
const db = require('../db')
// 创建路由对象
const router = express.Router()

// 查询历史记录
router.get('/history', (req, res) => {

    console.log('method', req.method)
    const url = req.url
    console.log('url', url)
    req.query = querystring.parse(url.split('?')[1])
    //参数
    const Id = req.query.Id
    const password = req.query.password
    console.log('Id', req.query.Id)
    console.log('password', req.query.password)

    //1、根据用户名查询用户的数据
    const sql = "select * from History where Id='"+Id+"'"
    let error = 'null'
    db.query(sql, function (err, result) {
        console.log("历史记录查询结果："+result)
        if (err) { error = "执行 SQL 语句失败" }                    // 执行 SQL 语句失败
        if (result.rowsAffected < 1) { error = '登录失败！' }   // 执行 SQL 语句成功，但是查询到数据条数不等于 1    
        console.log("用户名存在")         //用户名存在     
        res.setHeader("Content-type", "text/html;charset=utf8");//如果打开页面乱码, 设置

        if (error === 'null') {
            res.send(JSON.stringify({
                status: 0,
                message: '查询成功！',
                Id: Id,
                password: password,
                history:result.recordsets[0]
            }))
        }
        else {
            res.send(JSON.stringify({
                status: 1,
                message: error
            }))
        }

    })
})

router.get('/addhistory', (req, res) => {

    console.log('method', req.method)
    const url = req.url
    console.log('url', url)
    req.query = querystring.parse(url.split('?')[1])
    //参数
    const Id = req.query.Id
    const date = req.query.date
    const money = req.query.money
    const type = req.query.type
    console.log('Id', req.query.Id)
    console.log('date',date)
    console.log('money',money)
    console.log('type',type)

    //1、根据用户名查询用户的数据
    //insert into History values('8202201417','2022-1-3 16:18:23','充值记录','5.22')
    const sql = "insert into History values('"+Id+"','"+date+"','"+type+"','"+money+"')"
    let error = 'null'
    db.query(sql, function (err, result) {
        console.log("历史记录添加结果："+result)
        if (err) { error = "执行 SQL 语句失败" }                    // 执行 SQL 语句失败
        if (result.rowsAffected < 1) { error = '登录失败！' }   // 执行 SQL 语句成功，但是查询到数据条数不等于 1    
        console.log("用户名存在")         //用户名存在     
        res.setHeader("Content-type", "text/html;charset=utf8");//如果打开页面乱码, 设置

        if (error === 'null') {
            res.send(JSON.stringify({
                status: 0,
                message: '添加成功！',
                Id: Id,
                type: type,
                date:date,
                money:money
            }))
        }
        else {
            res.send(JSON.stringify({
                status: 1,
                message: error
            }))
        }

    })
})

// 将路由对象共享出去
module.exports = router