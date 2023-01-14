package com.example.cardspending;

public class Student {
    private String id;
    private String password;
    private String surplus;
    private String electricity;
    private String water;
    private String isHelp;
    private static Student mStudent; /** 1、单例实现 */
    private Student(){
        id = "";
        password="";
        surplus = "";
        electricity = "";
        water = "";
        isHelp = "";
    }

    public static Student getStudent(){     /** 2、单例实现 */
        if(mStudent==null){
            mStudent = new Student();
        }
        return mStudent;
    }

    /** 余额充值 */
    public void addSurplus(String money) {
        float now = Float.parseFloat(surplus);
        float add = Float.parseFloat(money);
        surplus = String.valueOf(now+add);
    }

    /** 余额消费 */
    public void delSurplus(String money) {
        float now = Float.parseFloat(surplus);
        float add = Float.parseFloat(money);
        surplus = String.valueOf(now-add);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setId(String id) {
        this.id = id.trim();
    }

    public void setSurplus(String surplus) {
        this.surplus = surplus.trim();
    }

    public void setElectricity(String electricity) {
        this.electricity = electricity.trim();
    }

    public void setWater(String water) {
        this.water = water.trim();
    }

    public void setIsHelp(String isHelp) {
        this.isHelp = isHelp.trim();
    }

    public String getId() {
        return id;
    }

    public String getSurplus() {
        return surplus;
    }

    public String getElectricity() {
        return electricity;
    }

    public String getWater() {
        return water;
    }

    public String getIsHelp() {
        return isHelp;
    }
}
