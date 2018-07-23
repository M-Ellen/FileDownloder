package com.pzm.netroiddemo;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pzm.netroiddemo.bean.ProgramInfoData;
import com.pzm.netroiddemo.utils.FileUtils;
import com.pzm.netroiddemo.widget.RoundProgressBar;

import java.util.List;

/**
 * @author pzm on 2018/7/18
 */

public class DownloadAdapter extends BaseAdapter {


    private Context mContext = null;
    private ListView mListView = null;
    private List<ProgramInfoData> mProgramInfoDataList = null;

    public DownloadAdapter(Context context, ListView listView, List<ProgramInfoData> dataList) {
        mListView = listView;
        mContext = context;
        mProgramInfoDataList = dataList;
    }

    @Override
    public int getCount() {
        return mProgramInfoDataList.size();
    }

    @Override
    public ProgramInfoData getItem(int position) {
        return mProgramInfoDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.download_grid_item, null, false);
            viewHolder.mDownloadIv = convertView.findViewById(R.id.iv_download_icon);
            viewHolder.mDownloadNameTv = convertView.findViewById(R.id.tv_download_name);
            viewHolder.mRoundProgressBar = convertView.findViewById(R.id.roundProgressBar);

            convertView.setTag(viewHolder);
        }else {

            viewHolder = (ViewHolder) convertView.getTag();
        }



        viewHolder.mDownloadIv.setImageResource(mProgramInfoDataList.get(position).getIconId());
        viewHolder.mDownloadNameTv.setText(mProgramInfoDataList.get(position).getName());
        if(!TextUtils.isEmpty(mProgramInfoDataList.get(position).getFilePath())) {
            long downSize = FileUtils.judgeIsExistAndGetSize(mProgramInfoDataList.get(position).getFilePath());
            long fileSize = mProgramInfoDataList.get(position).getFileSize();
            int progress = (int) Math.floor((downSize *1.0f / fileSize) * 100);
            if(fileSize != 0) {
                viewHolder.mRoundProgressBar.setVisibility(View.VISIBLE);
                viewHolder.mRoundProgressBar.setProgress(progress);
                if(fileSize == downSize) {
                    viewHolder.mRoundProgressBar.setVisibility(View.GONE);
                }
            }
        }


        viewHolder.mRoundProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnProgressBarClickListener.OnProgressBarClick(((RoundProgressBar)v), position, mProgramInfoDataList.get(position));
            }
        });
        return convertView;
    }

    public void refleshData(List<ProgramInfoData> programInfoDataList){
        mProgramInfoDataList = programInfoDataList;
    }

    public void setProgress(int progress, ProgramInfoData programInfoData) {
        getCurProgressBar(programInfoData).setStatus(RoundProgressBar.PROGRESS_DOWNLOAD);
        getCurProgressBar(programInfoData).setProgress(progress);
    }

    private int findProgramInfoDataById(int id) {
        for (int i = 0; i < mProgramInfoDataList.size(); i++) {
            if (mProgramInfoDataList.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    public RoundProgressBar getCurProgressBar(ProgramInfoData ProgramInfoData){
        RoundProgressBar progressBar = null;
        int pos = findProgramInfoDataById(ProgramInfoData.getId());
        if(pos >= mListView.getFirstVisiblePosition() && pos <= mListView.getLastVisiblePosition()) {
            progressBar = (mListView.getChildAt(pos - mListView.getFirstVisiblePosition()).findViewById(R.id.roundProgressBar));
        }
        return progressBar;
    }


    public void setSuccess(ProgramInfoData programInfoData) {
        getCurProgressBar(programInfoData).setVisibility(View.GONE);
    }

    public void setError(ProgramInfoData programInfoData) {
        getCurProgressBar(programInfoData).setStatus(RoundProgressBar.PROGRESS_ERROR);
    }

    public void setPause(ProgramInfoData programInfoData) {
        getCurProgressBar(programInfoData).setStatus(RoundProgressBar.PROGRESS_PAUSE);
    }


    private class ViewHolder{

        ImageView mDownloadIv;
        ImageView mEditIv;
        TextView mDownloadNameTv;
        RoundProgressBar mRoundProgressBar;
    }



    private OnProgressBarClickListener mOnProgressBarClickListener;

    public void setOnProgressBarClickListener(OnProgressBarClickListener onProgressBarClickListener){
        mOnProgressBarClickListener = onProgressBarClickListener;
    }

    public interface OnProgressBarClickListener{
        void OnProgressBarClick(RoundProgressBar v, int position, ProgramInfoData programInfoData);
    }

}
