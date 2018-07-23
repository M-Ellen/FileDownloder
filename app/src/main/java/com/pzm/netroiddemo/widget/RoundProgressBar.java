package com.pzm.netroiddemo.widget;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.pzm.netroiddemo.R;

/**
 *
 * @author pzm
 * @date 2017/3/8
 */

public class RoundProgressBar extends View {

	public static final int PROGRESS_PRE = 0;
	public static final int PROGRESS_PAUSE = 1;
	public static final int PROGRESS_DOWNLOAD = 2;
	public static final int PROGRESS_ERROR = 3;
	private Paint			paint;
	private int				roundColor;
	private int				roundProgressColor;
	private int				textColor;
	private float			textSize;
	private float			roundWidth;
	private int				max;
	private int				progress;
	private boolean			textIsDisplayable;
	private int				style;
	public static final int	STROKE	= 0;
	public static final int	FILL	= 1;
//	private boolean			isPause	= false;
//	private boolean			isError	= false;
	private int				mStatus	= PROGRESS_PRE; // 0 pre; 1 downloaing; 2 pause; 3 success ; 4 error;
	public RoundProgressBar(Context context) {
		this(context, null);
	}
	
	public RoundProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		paint = new Paint();
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);
		roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
		roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
		textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.GREEN);
		textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, 18);
		roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 5);
		max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
		textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);
		style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);
		
		mTypedArray.recycle();
	}

//	public void setPause(boolean pause) {
//		isPause = pause;
//
//	}

	public void setStatus(int status) {
		mStatus = status;
		postInvalidate();

	}

	public int getStatus() {
		return mStatus;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		/**
		 * 画最外层的大圆环
		 */
		int centre = getWidth() / 2;
		int radius = (int) (centre - roundWidth / 2);
		paint.setColor(roundColor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(roundWidth);
		paint.setAntiAlias(true);
		canvas.drawCircle(centre, centre, radius, paint);
		
		/**
		 * 画进度百分比
		 */
		paint.setStrokeWidth(0);
		paint.setColor(textColor);
		paint.setTextSize(textSize);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		
		int percent = (int) (((float) progress / (float) max) * 100);
		String text;
		switch (mStatus) {
		    case  PROGRESS_PRE:
				text = getResources().getString(R.string.download);
		        break;
		    case  PROGRESS_PAUSE:
				text = getResources().getString(R.string.continued);
		        break;
		    case PROGRESS_DOWNLOAD:
				text = percent + "%";
		        break;
		    case PROGRESS_ERROR:
				text = getResources().getString(R.string.error);
		        break;
		    default:
				text = getResources().getString(R.string.download);
		        break;
		}

		float textWidth = paint.measureText(text);
		float ascent = Math.abs(paint.ascent());
		float descent = paint.descent();
		if (mStatus != PROGRESS_DOWNLOAD) {
			canvas.drawText(text, centre - textWidth / 2, centre + (ascent - descent) / 2, paint);
		} else {
			if (textIsDisplayable && style == STROKE) {
				canvas.drawText(text, centre - textWidth / 2, centre + (ascent - descent) / 2, paint);
			}
		}
		
		/**
		 * 画圆弧 ，画圆环的进度
		 */
		paint.setStrokeWidth(roundWidth);
		paint.setColor(roundProgressColor);
		RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius); // 用于定义的圆弧的形状和大小的界限
		switch (style) {
			case STROKE :
				paint.setStyle(Paint.Style.STROKE);
				canvas.drawArc(oval, 0, 360 * progress / max, false, paint); // 根据进度画圆弧
				break;

			case FILL :
				paint.setStyle(Paint.Style.FILL_AND_STROKE);
				if (progress != 0) {
                    canvas.drawArc(oval, 0, 360 * progress / max, true, paint); // 根据进度画圆弧
                }
				break;

            default:
                break;
		}
		
	}
	
	public synchronized int getMax() {
		return max;
	}
	
	/**
	 * 设置进度的最大值
	 * 
	 * @param max
	 */
	public synchronized void setMax(int max) {
		if (max < 0) {
			throw new IllegalArgumentException("max not less than 0");
		}
		this.max = max;
	}
	
	/**
	 * 获取进度.需要同步
	 * 
	 * @return
	 */
	public synchronized int getProgress() {
		return progress;
	}
	
	/**
	 * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步 刷新界面调用postInvalidate()能在非UI线程刷新
	 * 
	 * @param progress
	 */
	public synchronized void setProgress(int progress) {
		if (progress < 0) {
			throw new IllegalArgumentException("progress not less than 0");
		}
		if (progress > max) {
			progress = max;
		}
		if (progress <= max) {
			this.progress = progress;
			postInvalidate();
		}
		
	}
	
	public int getCricleColor() {
		return roundColor;
	}
	
	public void setCricleColor(int cricleColor) {
		this.roundColor = cricleColor;
	}
	
	public int getCricleProgressColor() {
		return roundProgressColor;
	}
	
	public void setCricleProgressColor(int cricleProgressColor) {
		this.roundProgressColor = cricleProgressColor;
	}
	
	public int getTextColor() {
		return textColor;
	}
	
	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}
	
	public float getTextSize() {
		return textSize;
	}
	
	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}
	
	public float getRoundWidth() {
		return roundWidth;
	}
	
	public void setRoundWidth(float roundWidth) {
		this.roundWidth = roundWidth;
	}
}
