package com.example.cardspending;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.cardspending.ui.HitoryFragment;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //获取FragmentManager
        FragmentManager fm = getSupportFragmentManager();
        if(fm.findFragmentById(R.id.history_fragment_container)==null){
            fm.beginTransaction()   //创建一个Fragment事务
                    .add(R.id.history_fragment_container,new HitoryFragment())   //执行一个Fragment添加操作// 此处的R.id.fragment_container是要盛放fragment的父容器
                    .commit();//提交该事务
        }
    }
}