package com.example.cardspending;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class WriteDialog extends DialogFragment {
    private static WriteDialog mWriteDialog;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_write_dialog, container, false);
        return view;
    }

    public static WriteDialog getWriteDialog(){     //单例实现
        if(mWriteDialog==null){
            mWriteDialog = new WriteDialog();
        }
        return mWriteDialog;
    }

    //在fragment附加给activity时调用
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mCallBack.listening();
    }


}
