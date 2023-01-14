const express = require('express')
const querystring = require('querystring')
const db = require('../db')
// 创建路由对象
const router = express.Router()

// 登录
router.get('/login', (req, res) => {

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
    const sql = "select * from StuInfo where Id='" + Id + "'"
    let error = 'null'
    db.query(sql, function (err, result) {
        console.log("登录结果："+result)
        if (err) { error = "执行 SQL 语句失败" }                    // 执行 SQL 语句失败
        if (result.rowsAffected != 1) { error = '登录失败！' }   // 执行 SQL 语句成功，但是查询到数据条数不等于 1    
        console.log("用户名存在")         //用户名存在     
        res.setHeader("Content-type", "text/html;charset=utf8");//如果打开页面乱码, 设置

        if (error === 'null') {
            res.send(JSON.stringify({
                status: 0,
                message: '登录成功！',
                Id: result.recordset[0].Id,
                password: result.recordset[0].password,
                surplus: result.recordset[0].surplus,
                electricity: result.recordset[0].electricity,
                water: result.recordset[0].water,
                isHelp: result.recordset[0].isHelp,
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

// 更新surplus
router.get('/update', (req, res) => {
    console.log('method', req.method)
    const url = req.url
    console.log('url', url)
    req.query = querystring.parse(url.split('?')[1])
    //参数
    const Id = req.query.Id
    const password = req.query.password
    const surplus = req.query.surplus
    console.log('Id', req.query.Id)
    console.log('password', req.query.password)
    console.log('surplus', req.query.surplus)

    //1、根据用户名查询用户的数据
    const sql = "update StuInfo set surplus='"+surplus+ "' where id='"+Id+"'"
    let error = 'null'
    db.query(sql, function (err, result) {
        console.log("更新结果："+result)
        if (err) { error = "执行 SQL 语句失败" }                    // 执行 SQL 语句失败
        if (result.rowsAffected != 1) { error = '登录失败！' }   // 执行 SQL 语句成功，但是查询到数据条数不等于 1    

        console.log("用户名存在")         //用户名存在     
        res.setHeader("Content-type", "text/html;charset=utf8");//如果打开页面乱码, 设置
        if (error === 'null') {
            res.send(JSON.stringify({
                status: 0,
                message: '修改成功！',
                Id: Id,
                password: password,
                surplus: surplus,
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
