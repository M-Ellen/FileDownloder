package com.pzm.netroiddemo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.pzm.netroiddemo.bean.ProgramInfoData;
import com.pzm.netroiddemo.service.DownloadService;


/**
 * Created by MK on 2017/2/25.
 */

public class DownloadUtils implements Constants {
	private static final String	TAG					= DownloadUtils.class.getSimpleName();




	@SuppressLint("WrongConstant")
	public static void startDownload(Context context, ProgramInfoData data) {
		Intent intent = new Intent(context, DownloadService.class);
		intent.putExtra(DOWNLOAD_DATA, data);
		intent.setFlags(DOWNLOAD_START);
		context.startService(intent);
	}
	@SuppressLint("WrongConstant")
	public static void pauseDownload(Context context, ProgramInfoData data) {
		Intent intent = new Intent(context, DownloadService.class);
		intent.putExtra(DOWNLOAD_DATA, data);
		intent.setFlags(DOWNLOAD_PAUSE);
		context.startService(intent);
	}
	@SuppressLint("WrongConstant")
	public static void resumeDownload(Context context, ProgramInfoData data) {
		Intent intent = new Intent(context, DownloadService.class);
		intent.putExtra(DOWNLOAD_DATA, data);
		intent.setFlags(DOWNLOAD_RESUME);
		context.startService(intent);
	}
	@SuppressLint("WrongConstant")
	public static void deleteDownload(Context context, ProgramInfoData data) {
		Intent intent = new Intent(context, DownloadService.class);
		intent.putExtra(DOWNLOAD_DATA, data);
		intent.setFlags(DOWNLOAD_DELETE);
		context.startService(intent);
	}
}
