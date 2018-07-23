package com.pzm.netroiddemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.pzm.netroiddemo.bean.EventMsg;
import com.pzm.netroiddemo.bean.ProgramInfoData;
import com.pzm.netroiddemo.utils.Constants;
import com.pzm.netroiddemo.utils.DownloadUtils;
import com.pzm.netroiddemo.utils.FileUtils;
import com.pzm.netroiddemo.widget.RoundProgressBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;

import static com.pzm.netroiddemo.widget.RoundProgressBar.PROGRESS_DOWNLOAD;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, DownloadAdapter.OnProgressBarClickListener, Constants {

    private static final String	TAG = "MainActivity";

    private Context mContext = null;
    private ListView mListView = null;
    private DownloadAdapter mAdapter = null;
    private LinkedList<ProgramInfoData> mProgramInfoDataList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        EventBus.getDefault().register(this);
        initData();
        initView();
    }

    private void initView() {
        mListView = findViewById(R.id.gv_download_list);
        mAdapter = new DownloadAdapter(this, mListView, mProgramInfoDataList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mAdapter.setOnProgressBarClickListener(this);
    }

    private void initData(){
        mProgramInfoDataList = new LinkedList<>();
        mProgramInfoDataList.add(new ProgramInfoData(1, "TIM", R.mipmap.qq, "http://sqdd.myapp.com/myapp/qqteam/tim/down/tim.apk"));
        mProgramInfoDataList.add(new ProgramInfoData(2, "QQ", R.mipmap.qq, "https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk"));
        mProgramInfoDataList.add(new ProgramInfoData(3, "微信", R.mipmap.qq, "http://dldir1.qq.com/weixin/android/weixin667android1320.apk"));
//        mProgramInfoDataList.add(new ProgramInfoData(4, "京东", R.mipmap.qq, "https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk"));
//        mProgramInfoDataList.add(new ProgramInfoData(5, "淘宝", R.mipmap.qq, "https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk"));
        mProgramInfoDataList.add(new ProgramInfoData(6, "腾讯视频", R.mipmap.qq, "https://sm.myapp.com/original/Video/QQliveSetup_20_523-10.9.2173.0.exe"));
        mProgramInfoDataList.add(new ProgramInfoData(7, "爱奇艺", R.mipmap.qq, "http://down10.zol.com.cn/ceshi/IQIYIsetup_zol@kb001.exe"));
        mProgramInfoDataList.add(new ProgramInfoData(8, "谷歌浏览器", R.mipmap.qq, "https://sm.myapp.com/original/Browser/67.0.3396.99_chrome_installer.exe"));
        mProgramInfoDataList.add(new ProgramInfoData(9, "支付宝", R.mipmap.qq, "https://t.alipayobjects.com/L1/71/100/and/alipay_wap_main.apk"));
//        mProgramInfoDataList.add(new ProgramInfoData(10, "天猫", R.mipmap.qq, "https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk"));
//        mProgramInfoDataList.add(new ProgramInfoData(11, "美团", R.mipmap.qq, "http://dl.sj.91.com/msoft/91assistant_3.9_295.apk"));
//        mProgramInfoDataList.add(new ProgramInfoData(12, "网易", R.mipmap.qq, "https://github.com/umeng/UMAndroidSdkDemo/archive/master.zip"));
//        mProgramInfoDataList.add(new ProgramInfoData(13, "高德地图", R.mipmap.qq, "https://github.com/greenrobot/EventBus/archive/master.zip"));
//        mProgramInfoDataList.add(new ProgramInfoData(14, "知乎", R.mipmap.qq, "https://github.com/vince-styling/Netroid/archive/dev.zip"));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusListenr(EventMsg event){

        switch (event.getMsgTypeMode()) {
            case STATUS_PRE_EXECUTE :
                Log.e("debug", "onEvent :STATUS_PRE_EXECUTE ");
                mAdapter.setProgress(event.getPercent(), event.getProgramInfoData());
                break;
            case STATUS_PROGRESS_CHANGE :
                mAdapter.setProgress(event.getPercent(), event.getProgramInfoData());
//                Log.e("debug", "onEvent :STATUS_PROGRESS_CHANGE "+ event.getPercent());

                break;
            case STATUS_CANCEL :
                mAdapter.setPause(event.getProgramInfoData());
                break;
            case STATUS_SUCCESS :
                mAdapter.setSuccess(event.getProgramInfoData());
                break;
            case STATUS_ERROR :
                mAdapter.setError(event.getProgramInfoData());
                break;
            case STATUS_FINISH :

                break;
            default:
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(this, position+"", Toast.LENGTH_SHORT);
    }

    @Override
    public void OnProgressBarClick(RoundProgressBar v, int position, ProgramInfoData programInfoData) {
        int status = v.getStatus();
        if(status == RoundProgressBar.PROGRESS_PRE) {
            programInfoData.setFilePath(FileUtils.getRootFilePath() + programInfoData.getName());
            DownloadUtils.startDownload(mContext, programInfoData);
            v.setStatus(PROGRESS_DOWNLOAD);
        }else if(status == PROGRESS_DOWNLOAD) {
            DownloadUtils.pauseDownload(mContext, programInfoData);
        }else if(status == RoundProgressBar.PROGRESS_PAUSE) {
            DownloadUtils.resumeDownload(mContext, programInfoData);
        }
    }

}
