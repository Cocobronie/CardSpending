package com.example.cardspending.ui;

import static com.example.cardspending.MainActivity.tipDialog;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cardspending.History;
import com.example.cardspending.HistoryFetcher;
import com.example.cardspending.HistoryLab;
import com.example.cardspending.MainActivity;
import com.example.cardspending.R;
import com.example.cardspending.Student;
import com.example.cardspending.StudentFetcher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class HitoryFragment extends Fragment {

    private static final String TAG = "HitoryFragment";
    private RecyclerView mRecyclerView;
    private HistoryLab mHistoryLab;
    private Student mStudent;
    public HitoryFragment() {}

    public static HitoryFragment newInstance(String param1, String param2) {
        HitoryFragment fragment = new HitoryFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化mStudent
        mStudent = Student.getStudent();
        mHistoryLab = HistoryLab.getHistoryLab();
        if(mStudent.getId()==""){     //如果用户还未录入卡片信息
            tipDialog(getActivity(),"您的卡不在系统中");
        }else{
            if(mHistoryLab!=null)     //清空mHistoryLab，防止数据重复
                mHistoryLab.delHistories();
            //实现数据获取
            new FetchItemsTask().execute();
        }

        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_hitory, container, false);
        //实例化RecycleView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //setupAdapter();
        return view;
    }

    private void setupAdapter() {
        //如果有数据或者recycle view有item view就删除;否则程序崩溃，找不到item
        if (mRecyclerView.getChildCount() > 0 ) {
            Log.e("ChildCount", String.valueOf(mRecyclerView.getChildCount()));
            mRecyclerView.removeAllViews();
        }
        if (isAdded()) {
            mRecyclerView.setAdapter(new HistoryAdapter(mHistoryLab.getHistories()));
        }
    }
    /**
    ViewHolder类
    1、绑定list_item_weather布局
    2、实例化list_item_weather中的组件
    3、监听并响应点击事件
     */
    private class StudentHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mMoneyTextView ;
        private TextView mDateTextView ;
        private TextView mTypeTextView ;
        private History mHistory;
        /**
        1、实例化组件
        2、设置监听
         */
        public StudentHolder(LayoutInflater inflater,ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_student,parent,false));
            //监听实现的关键
            itemView.setOnClickListener(this);
            mMoneyTextView = (TextView) itemView.findViewById(R.id.money_text);
            mDateTextView = (TextView) itemView.findViewById(R.id.date_text);
            mTypeTextView = (TextView) itemView.findViewById(R.id.record_type);
        }

        @Override
        public void onClick(View v) {
            //跳转到细节视图
//            Intent intent = PagerActivity.newIntent(getActivity(), mWeather.getId());
//            startActivity(intent);
        }

        //Adapter中传入数据
        public void bind(History history) {
            mHistory = history;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd \n HH:mm:ss");
            String dateString = formatter.format(mHistory.getDate());
            //设置组件数据
            mMoneyTextView.setText(mHistory.getMoney());
            mDateTextView.setText(dateString);
            mTypeTextView.setText(mHistory.getType());
        }
    }

    /**
    Adapter
    1、实例化单例WeatherLab
     */
    private class HistoryAdapter extends RecyclerView.Adapter<StudentHolder> {

        private List<History> mHistories = new ArrayList<>();

        //构造函数中的weathers由Fragment在onCreateView中实例化后传入
        public HistoryAdapter(List<History> histories){
            mHistories = histories;
        }

        //当RecycleView需要新的ViewHolder显示列表项时调用。
        @Override
        public StudentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //创建WeatherHolder
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new StudentHolder(layoutInflater,parent);
        }

        //用于在指定位置显示数据。此方法用于更新itemView的内容，以反映给定位置的项。
        @Override
        public void onBindViewHolder(StudentHolder holder, int position) {
            History history = mHistories.get(position);
            holder.bind(history);
        }

        @Override
        public int getItemCount() {
            return mHistories.size();
        }

    }

    /**
     *实现AsyncTask工具类
     */
    private class FetchItemsTask extends AsyncTask<Void,Void, HistoryLab> {

        @Override
        protected HistoryLab doInBackground(Void... params) {
            String url;
            url = Uri.parse("https://6737k8d627.goho.co/api/history")
                    .buildUpon()
                    .appendQueryParameter("Id", mStudent.getId())
                    .appendQueryParameter("password", mStudent.getPassword())
                    .build().toString();
            return new HistoryFetcher().fetchItems(url);
        }

        @Override
        protected void onPostExecute(HistoryLab items) {
            mHistoryLab = items;
            setupAdapter();
            //updateView();
        }
    }
}