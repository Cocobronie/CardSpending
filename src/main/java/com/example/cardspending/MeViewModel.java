package com.example.cardspending;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cardspending.Student;

public class MeViewModel extends ViewModel {

    private final MutableLiveData<Student> mStudent;
    private final MutableLiveData<String> mMoney;

    public MeViewModel() {
        mStudent = new MutableLiveData<>();
        mMoney= new MutableLiveData<String>();
    }

    public void setMoney(String money) {
        mMoney.setValue(money);
    }

    public LiveData<String> getMoney() {
        return mMoney;
    }

    public void setStudent(Student student) {
        mStudent.setValue(student);
    }

    public LiveData<Student> getStudent() {
        return mStudent;
    }
}