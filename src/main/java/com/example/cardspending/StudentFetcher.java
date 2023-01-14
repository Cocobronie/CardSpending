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
import java.util.ArrayList;
import java.util.List;

public class StudentFetcher {
    private static final String TAG = "StudentFetcher";
    private Student mStudent;

    //从指定URL中获取原始数据，返回一个字节流数组
    public byte[] getUrlBytes(String urlSpec) throws IOException {
        //创建一个URL对象
        URL url = new URL(urlSpec);
        //创建一个指向要访问URL的连接对象
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setInstanceFollowRedirects(false);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        //跟踪重定向:表示永久重定向，Moved Permanently:说明请求的资源已经不存在了，需改用新的 URL 再次访问。
        String redirect = connection.getHeaderField("Location");
        if(redirect!=null){
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
    public Student fetchItems(boolean isUpdate) {
        mStudent=Student.getStudent();
        try {
            String url;
            if(isUpdate){
                url = Uri.parse("https://6737k8d627.goho.co/api/update")
                        .buildUpon()
                        .appendQueryParameter("Id", mStudent.getId())
                        .appendQueryParameter("password", mStudent.getPassword())
                        .appendQueryParameter("surplus", mStudent.getSurplus())
                        .build().toString();
            }
            else{
                url = Uri.parse("https://6737k8d627.goho.co/api/login")
                        .buildUpon()
                        .appendQueryParameter("Id", mStudent.getId())
                        .appendQueryParameter("password", mStudent.getPassword())
                        .build().toString();
            }
            String jsonString = getUrlString(url);
            Log.e(TAG, "Received JSON: " + jsonString);
            //将JSON数据解析成相应的Java对象
            JSONObject jsonBody = new JSONObject(jsonString);
            String status = jsonBody.getString("status");
            String message = jsonBody.getString("message");
            Log.e(TAG,"status : "+status);
            Log.e(TAG,"message : "+message);
            //判断登录状态
            if(status.equals("0")){ //登录成功
                mStudent.setId(jsonBody.getString("Id"));
                mStudent.setPassword(jsonBody.getString("password"));
                mStudent.setSurplus(jsonBody.getString("surplus"));
                mStudent.setElectricity(jsonBody.getString("electricity"));
                mStudent.setWater(jsonBody.getString("water"));
                mStudent.setIsHelp(jsonBody.getString("isHelp"));
            }
            else{                   //登录失败:弹出Dialog提示卡片不正确
                mStudent.setPassword("wrong");
            }
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }

        return Student.getStudent();
    }


}
