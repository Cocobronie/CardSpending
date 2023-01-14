package com.example.cardspending;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class HistoryFetcher {

    private static final String TAG = "StudentFetcher";
    private Student mStudent;

    //从指定URL中获取原始数据，返回一个字节流数组
    public byte[] getUrlBytes(String urlSpec) throws IOException {
        //创建一个URL对象
        URL url = new URL(urlSpec);
        //创建一个指向要访问URL的连接对象
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setInstanceFollowRedirects(false);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        //跟踪重定向:表示永久重定向，Moved Permanently:说明请求的资源已经不存在了，需改用新的 URL 再次访问。
        String redirect = connection.getHeaderField("Location");
        if (redirect != null) {
            connection = (HttpURLConnection) new URL(redirect).openConnection();
        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    //将getUrlBytes获取的字节数据转化为String
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    //构建URL请求并获取内容
    public HistoryLab fetchItems(String url) {
        mStudent = Student.getStudent();
        HistoryLab historyLab = HistoryLab.getHistoryLab();
        try {
            String jsonString = getUrlString(url);
            Log.e(TAG, "Received JSON: " + jsonString);
            //将JSON数据解析成相应的Java对象
            JSONObject jsonBody = new JSONObject(jsonString);
            String status = jsonBody.getString("status");
            String message = jsonBody.getString("message");
            Log.e(TAG, "status : " + status);
            Log.e(TAG, "message : " + message);
            //判断登录状态
            if (status.equals("0")) { //登录成功
                parseItems(historyLab, jsonBody);

            } else {                   //登录失败:弹出Dialog提示卡片不正确
                mStudent.setPassword("wrong");
            }
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return historyLab;
    }

    /**
     *处理JSON数据
     */
    private void parseItems(HistoryLab items, JSONObject jsonBody)
            throws IOException, JSONException, ParseException {

        //JSONObject photosJsonObject = jsonBody.getJSONObject("daily");
        JSONArray HistoryJsonArray = jsonBody.getJSONArray("history");

        for (int i = HistoryJsonArray.length()-1; i >=0 ; i--) {
            JSONObject historyJsonObject = HistoryJsonArray.getJSONObject(i);
            History item = new History();
            //设置每一个item
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            item.setDate(format.parse(historyJsonObject.getString("date")));
            item.setType(historyJsonObject.getString("type"));
            item.setMoney(historyJsonObject.getString("money"));
            items.addHistory(item);
        }
    }
    /*
    "history":[
    {"Id":"8202201417","date":"2022.1.2","type":"消费记录","money":"56.4"},
    {"Id":"8202201417","date":"2022.1.3","type":"消费记录","money":"4.4"},
    {"Id":"8202201417","date":"2022.1.4","type":"消费记录","money":"56.4"},
    {"Id":"8202201417","date":"2022.1.5","type":"充值记录","money":"5.22"}
    ]
     */
}


