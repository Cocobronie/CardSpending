package com.example.cardspending.ui;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cardspending.HistoryFetcher;
import com.example.cardspending.HistoryLab;
import com.example.cardspending.MainActivity;
import com.example.cardspending.Student;
import com.example.cardspending.WriteDialog;
import com.example.cardspending.databinding.FragmentRechargeBinding;
import com.example.cardspending.MeViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RechargeFragment extends Fragment {

    private FragmentRechargeBinding binding;
    private TextView mSpendIdText ;
    private TextView mSpendSurplusText;
    private EditText mSpendMoneyEdit;
    private Button mSpendMoney20;
    private Button mSpendMoney30 ;
    private Button mSpendMoney50 ;
    private Button mSpendMoney100;
    private Button mSpendMoney200 ;
    private Button mSpendMoney300;
    private Button mSpendConfirmButton;
    private String mMoney;
    private Student mStudent;
    private WriteDialog mWriteDialog;
    private MeViewModel meViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //初始化ViewModel
        meViewModel =
                new ViewModelProvider(getActivity()).get(MeViewModel.class);
        //初始化binding
        binding = FragmentRechargeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //动态监听mStudent变化
        meViewModel.getStudent().observe(getViewLifecycleOwner(), student -> {
            mStudent = student;
            setView();
            Log.e("observe调用,SpendFragment",mStudent.getId());
        });

        /** 初始化组件 */
        mSpendIdText = binding.rechargeIdText;
        mSpendSurplusText = binding.rechargeSurplusText;
        mSpendMoneyEdit = binding.rechargeMoneyEdit;
        mSpendMoney20 = binding.rechargeMoney20;
        mSpendMoney30 = binding.rechargeMoney30;
        mSpendMoney50 = binding.rechargeMoney50;
        mSpendMoney100 = binding.rechargeMoney100;
        mSpendMoney200 = binding.rechargeMoney200;
        mSpendMoney300 = binding.rechargeMoney300;
        mSpendConfirmButton = binding.rechargeConfirmButton;
        setEvent();
        return root;
    }

    /** 设置界面值 */
    private void setView() {
        mSpendIdText.setText(mStudent.getId());
        mSpendSurplusText.setText(mStudent.getSurplus());
    }

    /** 设置事件监听 */
    private void setEvent() {
        /** 用户输入框 */
        mSpendMoneyEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //设置确认按钮可见
                mSpendConfirmButton.setEnabled(true);
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        /** 钱按钮 */
        mSpendMoney20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpendMoneyEdit.setText("20");
            }
        });
        mSpendMoney30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpendMoneyEdit.setText("30");
            }
        });
        mSpendMoney50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpendMoneyEdit.setText("50");
            }
        });
        mSpendMoney100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpendMoneyEdit.setText("100");
            }
        });
        mSpendMoney200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpendMoneyEdit.setText("200");
            }
        });
        mSpendMoney300.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpendMoneyEdit.setText("300");
            }
        });

        /** 确认按钮 */
        mSpendConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获得充值金额
                mMoney = String.valueOf(mSpendMoneyEdit.getText());
                mStudent.addSurplus(mMoney);
                mSpendMoneyEdit.setHint("请输入金额");
                mSpendConfirmButton.setEnabled(false);
                Log.e("充值后的金额",mStudent.getSurplus());
                /** 弹出ReadDialog,向卡片中写入信息同时更新服务器中的信息 */
                mWriteDialog=WriteDialog.getWriteDialog();
                mWriteDialog.showNow(getChildFragmentManager(),"WriteDialog");
                mWriteDialog.getDialog().setCancelable(false);
                MainActivity.isWrite=true;
                //实现数据获取
                new RechargeFragment.FetchItemsTask().execute();
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     *实现AsyncTask工具类
     */
    private class FetchItemsTask extends AsyncTask<Void,Void, HistoryLab> {

        @Override
        protected HistoryLab doInBackground(Void... params) {
            String url;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(new Date());
            url = Uri.parse("https://6737k8d627.goho.co/api/addhistory")
                    .buildUpon()
                    .appendQueryParameter("Id", mStudent.getId())
                    .appendQueryParameter("type", "充值记录")
                    .appendQueryParameter("date", dateString)
                    .appendQueryParameter("money", mMoney)
                    .build().toString();
            return new HistoryFetcher().fetchItems(url);
        }

        @Override
        protected void onPostExecute(HistoryLab items) {
        }
    }
}