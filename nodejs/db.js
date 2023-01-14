// 导入 mysql 模块
const mssql = require('mssql')

// 创建数据库连接对象
var db = {};
var config = {
    user: 'sa',
    password: '123456',
    server: '127.0.0.1',
    database: 'dbsqlconnect',
    port: 1433,
    options: {
        encrypt: false // Use this if you're on Windows Azure
    },
    pool: {
        min: 0,
        max: 10,
        idleTimeoutMillis: 3000
    }
};

//执行sql,返回数据.
db.query = function (sql, callBack) {
    var connection = new mssql.ConnectionPool(config, function (err) {
        if (err) {
            console.log(err);
            return;
        }
        var ps = new mssql.PreparedStatement(connection);
        ps.prepare(sql, function (err) {
            if (err) {
                console.log(err);
                return;
            }
            ps.execute('', function (err, result) {
                if (err) {
                    console.log(err);
                    return;
                }

                ps.unprepare(function (err) {
                    if (err) {
                        console.log(err);
                        callback(err, null);
                        return;
                    }
                    callBack(err, result);
                });
            });
        });
    });
};

// 向外共享 db 数据库连接对象
module.exports = db