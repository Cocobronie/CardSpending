package com.example.cardspending.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cardspending.HistoryActivity;
import com.example.cardspending.MeViewModel;
import com.example.cardspending.Student;
import com.example.cardspending.databinding.FragmentMeBinding;


public class MeFragment extends Fragment {

    private FragmentMeBinding binding;
    private Student mStudent;
    private TextView mSurplusText;
    private TextView mElectricityText;
    private TextView mWaterText;
    private TextView mIsHelpText;
    private ImageButton mHistoryBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MeViewModel meViewModel =
                new ViewModelProvider(getActivity()).get(MeViewModel.class);

        binding = FragmentMeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        meViewModel.getStudent().observe(getViewLifecycleOwner(), student -> {
            mStudent = student;
            setEvent();
            Log.e("observe调用",mStudent.getId());
        });
        mSurplusText = binding.meSurplusText;
        mElectricityText = binding.meElectricityText;
        mWaterText = binding.meWaterText;
        mIsHelpText = binding.meIsHelpText;
        mHistoryBtn = binding.historyBtn;

        mHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到历史记录界面
                Intent intent  = new Intent(getContext(), HistoryActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(), "跳转到历史记录界面", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    private void setEvent() {

        if(mStudent!=null){
            mSurplusText.setText(mStudent.getSurplus()+" 元");
            mElectricityText.setText(mStudent.getElectricity()+" 元");
            mWaterText.setText(mStudent.getWater()+" 元");
            if(mStudent.getIsHelp().equals("0"))
                mIsHelpText.setText("否");
            else
                mIsHelpText.setText("是");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}