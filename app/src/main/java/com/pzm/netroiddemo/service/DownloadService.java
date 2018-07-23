package com.pzm.netroiddemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.NetroidError;
import com.duowan.mobile.netroid.toolbox.FileDownloader;
import com.pzm.netroiddemo.Netroid;
import com.pzm.netroiddemo.bean.EventMsg;
import com.pzm.netroiddemo.bean.ProgramInfoData;
import com.pzm.netroiddemo.utils.Constants;

import org.greenrobot.eventbus.EventBus;

/**
 * @author pzm
 */
public class DownloadService extends Service implements Constants {
    
    
    public static final String TAG = "debug";
    private FileDownloader mFileDownloader = null;


    public DownloadService() {
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        super.onCreate();
        Netroid.initFileDownload(null, 3);
        mFileDownloader = Netroid.getFileDownloader();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        if(intent != null) {
            ProgramInfoData programInfoData = (ProgramInfoData) intent.getSerializableExtra(DOWNLOAD_DATA);
            switch (intent.getFlags()) {
                case DOWNLOAD_START :
                    startDownload(programInfoData);
                    break;
                case DOWNLOAD_PAUSE :
                    pauseDownload(programInfoData);
                    break;
                case DOWNLOAD_RESUME :
                    resumeDownload(programInfoData);
                    break;
                case DOWNLOAD_DELETE :
                    deleteDownload(programInfoData);
                    break;
                default:
                    break;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void startDownload(ProgramInfoData data) {
        String path = data.getFilePath();
        FileDownloader.DownloadController controller = mFileDownloader.get(data.getFilePath(), data.getUrl());
        if(controller != null) {
            controller.resume();
        }else {
            mFileDownloader.add(data.getFilePath(), data.getUrl(), getListener(data));
        }
    }

    private void pauseDownload(ProgramInfoData data) {
        Log.e(TAG, "pauseDownload");
        FileDownloader.DownloadController controller = mFileDownloader.get(data.getFilePath(), data.getUrl());
        Log.e(TAG, "data.getFilePath()" + data.getFilePath());
        if(controller != null) {
            controller.pause();
        }
    }

    private void resumeDownload(ProgramInfoData data) {
        FileDownloader.DownloadController controller = mFileDownloader.get(data.getFilePath(), data.getUrl());
        if(controller != null) {
            controller.resume();
        }
    }

    private void deleteDownload(ProgramInfoData data) {
        FileDownloader.DownloadController controller = mFileDownloader.get(data.getFilePath(), data.getUrl());
        if(controller != null) {
            controller.discard();

        }
    }


    private Listener<Void> getListener(final ProgramInfoData data){

        return new Listener<Void>() {

            @Override
            public void onPreExecute() {
                super.onPreExecute();
                Log.e(TAG, "onPreExecute");
                EventBus.getDefault().post(new EventMsg(STATUS_PRE_EXECUTE, data));
            }

            @Override
            public void onProgressChange(long fileSize, long downloadedSize) {
                super.onProgressChange(fileSize, downloadedSize);
//                Log.e(TAG, "onProgressChange");
                data.setFileSize(downloadedSize);
                int pesenrt = (int) Math.floor((downloadedSize * 1.0f / fileSize) * 100);
                EventBus.getDefault().post(new EventMsg(STATUS_PROGRESS_CHANGE, data, pesenrt));
            }


            @Override
            public void onCancel() {
                super.onCancel();
                Log.e(TAG, "onCancel");
                EventBus.getDefault().post(new EventMsg(STATUS_CANCEL, data));
            }

            @Override
            public void onSuccess(Void aVoid) {
                Log.e(TAG, "onSuccess");
                EventBus.getDefault().post(new EventMsg(STATUS_SUCCESS, data));

            }

            @Override
            public void onError(NetroidError error) {
                super.onError(error);
                Log.e(TAG, "onError");
                EventBus.getDefault().post(new EventMsg(STATUS_ERROR, data));
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Log.e(TAG, "onFinish");
                EventBus.getDefault().post(new EventMsg(STATUS_FINISH, data));
            }

            @Override
            public void onNetworking() {
                super.onNetworking();
                Log.e(TAG, "onNetworking");
//                EventBus.getDefault().post(new EventMsg(STATUS_FINISH, data));
            }

        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
