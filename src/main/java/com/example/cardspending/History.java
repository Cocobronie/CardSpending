package com.example.cardspending;

import java.util.Date;

public class History {
    private String type;
    private Date mDate;
    private String money;
    public History(){
    }

    public String getType() {
        return type;
    }

    public Date getDate() {
        return mDate;
    }

    public String getMoney() {
        return money;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
