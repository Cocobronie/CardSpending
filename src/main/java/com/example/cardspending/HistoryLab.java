package com.example.cardspending;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryLab {
    private static HistoryLab sHistoryLab;
    private List<History> mHistories;

    public static HistoryLab getHistoryLab(){
        if(sHistoryLab==null){
            sHistoryLab=new HistoryLab();
        }
        return sHistoryLab;
    }

    public void addHistory(History history){
        mHistories.add(history);
    }

    public void setHistories(List<History> histories) {
        mHistories = histories;
    }

    public void delHistories() {
        int len = mHistories.size();
        for(int i=len-1;i>=0;i--){
            mHistories.remove(i);
        }
    }

    private HistoryLab(){
        mHistories = new ArrayList<>();
//        //先创建几个调试一下
//        for(int i=0;i<11;i++){
//            History history = new History();
//            history.setMoney("21.0");
//            history.setType("消费记录");
//            history.setDate(new Date());
//            mHistories.add(history);
//        }
    }

    public List<History> getHistories() {
        return mHistories;
    }

    //    public Weather getWeather(UUID id){
//        for(Weather weather:mWeathers){
//            if(weather.getId().equals(id)){
//                return weather;
//            }
//        }
//        return null;
//    }
}
