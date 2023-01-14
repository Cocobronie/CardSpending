package com.example.cardspending;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cardspending.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    //NFC相关参数
    private NfcAdapter mNfcAdapter=null;
    private PendingIntent mPendingIntent = null;

    private ActivityMainBinding binding;
    private Student mStudent;
    private static String TAG = "MainActivity" ;
    public static boolean isWrite=false;

    private MeViewModel meViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        meViewModel = new ViewModelProvider(this).get(MeViewModel.class);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //绑定导航栏视图
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_spend, R.id.navigation_recharge, R.id.navigation_me)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //检测NFC是否打开
        NfcCheck();
        //初始化参数
        mPendingIntent = PendingIntent.getActivity(this,0,new Intent(this,getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);
        mStudent = Student.getStudent();

        //弹出提示框
        //tipDialog(MainActivity.this,"请刷卡登录");
//        WriteDialog writeDialog = new WriteDialog();
//        writeDialog.showNow(getSupportFragmentManager(),"刷卡提示框");
    }


    @Override
    public void onPause() {
        super.onPause();
        if(mNfcAdapter!=null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        meViewModel.setStudent(mStudent);//更新界面
        if(mNfcAdapter!=null)
            mNfcAdapter.enableForegroundDispatch(this,mPendingIntent,null,null);
    }

    /**
    *当NFC靠近时调用
    */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if(!isWrite&&mStudent.getId()==""){
            String[] info = NfcUtil.read(tag); //读取Nfc:获取Id和密码
            mStudent.setId(info[1]);
            mStudent.setPassword(info[2]);
            Log.e("卡片所读Id",mStudent.getId());
            Log.e("卡片所读password",mStudent.getPassword());
            /**开启后台线程调用后端API查询数据库，启动AsyncTask，触发后台线程，并调用doInBackground()*/
            new FetchItemsTask(false).execute();
        }
        else if(isWrite){
            Log.e(TAG,"开始写入数据啦");
            NfcUtil.write(tag,mStudent);
            isWrite=false;
            WriteDialog.getWriteDialog().dismiss();  //关闭弹窗
            /**开启后台线程调用后端API查询数据库，启动AsyncTask，触发后台线程，并调用doInBackground()*/
            new FetchItemsTask(true).execute();
        }
        else{
            tipDialog(MainActivity.this,"请先刷卡登录");
        }

    }

    /**
     *判断NFC功能是否可用
     */
    private void NfcCheck(){
        //初始化mNfcAdapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(mNfcAdapter==null){//不支持NFC
            Toast.makeText(this,"不支持NFC",Toast.LENGTH_SHORT).show();
        }else{//判断NFC是否打开
            if(!mNfcAdapter.isEnabled()){    //如果没有打开，则跳转到设置界面
                Intent setNfc = new Intent(Settings.ACTION_NFC_SETTINGS);
                startActivity(setNfc);
            }else{
                Toast.makeText(this,"NFC已打开",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
    *更新界面
    */
    private void setUI() {
        meViewModel.setStudent(mStudent);       //更新界面
        Toast.makeText(MainActivity.this, "信息载入成功！", Toast.LENGTH_SHORT).show();
    }

    /**
    *实现AsyncTask工具类
    */
    private class FetchItemsTask extends AsyncTask<Void,Void, Student> {
        private String mSurplus;
        private boolean mIsUpdate;
        public FetchItemsTask(boolean isUpdate) {
            mIsUpdate = isUpdate;
        }

        @Override
        protected Student doInBackground(Void... params) {
            return new StudentFetcher().fetchItems(mIsUpdate);
        }

        @Override
        protected void onPostExecute(Student items) {
            Log.e("mStudent",items.getId());
            if(items.getPassword().equals("wrong")){     //登录失败:弹出Dialog提示卡片不正确
                tipDialog(MainActivity.this,"您的卡不在系统中");
        }else{                                           //登录成功:更新界面
                setUI();
            }
        }

    }

    /**
     * 提示对话框
     */
    public static void tipDialog(Context context,String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示：");
        builder.setMessage(msg);
        builder.setIcon(R.drawable.wrong);
        builder.setCancelable(false);            //点击对话框以外的区域是否让对话框消失

        //设置正面按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(MainActivity.this, "你点击了确定", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();      //创建AlertDialog对象
        //对话框显示的监听事件
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Log.e(TAG, "对话框显示了");
            }
        });
        //对话框消失的监听事件
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.e(TAG, "对话框消失了");
            }
        });
        dialog.show();                              //显示对话框
    }
}