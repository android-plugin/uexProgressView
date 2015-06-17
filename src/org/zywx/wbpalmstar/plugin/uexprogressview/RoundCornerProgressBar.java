package org.zywx.wbpalmstar.plugin.uexprogressview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.plugin.uexprogressview.VO.ProgressDataVO;

public class RoundCornerProgressBar extends LinearLayout {
	private final static int DEFAULT_PROGRESS_BAR_HEIGHT = 30;

	private FrameLayout layoutBackground;
	private LinearLayout layoutProgress;
	private LinearLayout linearlayoutparent;
	private int backgroundWidth = 0;
	private int backgroundHeight = 0;

	private boolean isProgressBarCreated = false;
	private boolean isProgressSetBeforeDraw = false;
	private boolean isMaxProgressSetBeforeDraw = false;
	private boolean isBackgroundColorSetBeforeDraw = false;
	private boolean isProgressColorSetBeforeDraw = false;

	private float max = 100;
	private float progress = 0;
	private int radius = 10;
	private int padding = 1;
	private int progressColor = Color.RED;
	private int backgroundColor = Color.GREEN;
    private TextView mPercent;

	@SuppressLint("NewApi")
	public RoundCornerProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!isInEditMode()) {
			isProgressBarCreated = false;
			isProgressSetBeforeDraw = false;
			isMaxProgressSetBeforeDraw = false;
			isBackgroundColorSetBeforeDraw = false;
			isProgressColorSetBeforeDraw = false;
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(EUExUtil.getResLayoutID("plugin_uexprogressview_round_corner_layout"), this);
			setup(context, attrs);
			isProgressBarCreated = true;
		} else {
			setBackgroundColor(Color.TRANSPARENT);
			setGravity(Gravity.CENTER);

			TextView tv = new TextView(context);
			tv.setText("RoundCornerProgressBar");
			addView(tv);
		}
	}

	@SuppressLint("NewApi")
	private void setup(Context context, AttributeSet attrs) {
		int color;
		DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
		radius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius, metrics);
		layoutBackground = (FrameLayout) findViewById(EUExUtil.getResIdID("round_corner_progress_background"));
		linearlayoutparent = (LinearLayout) findViewById(EUExUtil.getResIdID("linearlayoutparent"));
        mPercent = (TextView) findViewById(EUExUtil.getResIdID("plugin_uexprogress_percent"));
		padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, padding, metrics);
		layoutBackground.setPadding(padding, padding, padding, padding);
		linearlayoutparent.setPadding(padding, padding, padding, padding);
		if (!isBackgroundColorSetBeforeDraw) {
			color = backgroundColor;
			setBackgroundColor(color);
		}
		ViewTreeObserver observer = layoutBackground.getViewTreeObserver();
		observer.addOnPreDrawListener(new OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                layoutBackground.getViewTreeObserver().removeOnPreDrawListener(this);
                int height = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    backgroundWidth = layoutBackground.getMeasuredWidth();
                    height = layoutBackground.getMeasuredHeight();
                } else {
                    backgroundWidth = layoutBackground.getWidth();
                    height = layoutBackground.getHeight();
                }
                backgroundHeight = (height == 0) ? (int) dp2px(DEFAULT_PROGRESS_BAR_HEIGHT) : height;
                LayoutParams params = (LayoutParams) layoutBackground.getLayoutParams();
                params.height = backgroundHeight;
                layoutBackground.setLayoutParams(params);
                setProgress(progress);
                return false;
            }
        });
		layoutProgress = (LinearLayout) findViewById(EUExUtil.getResIdID("round_corner_progress_progress"));
		if (!isProgressColorSetBeforeDraw) {
			color = progressColor;
			setProgressColor(color);
		}

		if (!isMaxProgressSetBeforeDraw) {
		}
		if (!isProgressSetBeforeDraw) {
		}
	}
	
	@SuppressWarnings("deprecation")
	public void setRoundLinearlayoutColor(int color) {
        Log.i("djf", "setRoundLinearlayoutColor:color=" + color);
        int radius = this.radius - padding / 2;
		GradientDrawable gradient = new GradientDrawable();
		gradient.setShape(GradientDrawable.RECTANGLE);
		gradient.setColor(color);
		gradient.setCornerRadii(new float[] { radius, radius, radius, radius, radius, radius, radius, radius });
		linearlayoutparent.setBackgroundDrawable(gradient);
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public void setProgressColor(int color) {
        Log.i("djf", "setProgressColor:color=" + color);
		progressColor = color;
		int radius = this.radius - padding / 2;
		GradientDrawable gradient = new GradientDrawable();
		gradient.setShape(GradientDrawable.RECTANGLE);
		gradient.setColor(progressColor);
		gradient.setCornerRadii(new float[]{radius, radius, radius, radius, radius, radius, radius, radius});
		layoutProgress.setBackgroundDrawable(gradient);
		if (!isProgressBarCreated) {
			isProgressColorSetBeforeDraw = true;
		}
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public void setBackgroundColor(int color) {
        Log.i("djf", "setBackgroundColor:color=" + color);
		backgroundColor = color;
		GradientDrawable gradient = new GradientDrawable();
		gradient.setShape(GradientDrawable.RECTANGLE);
		gradient.setColor(backgroundColor);
		gradient.setCornerRadius(radius);
		layoutBackground.setBackgroundDrawable(gradient);
		if (!isProgressBarCreated) {
			isBackgroundColorSetBeforeDraw = true;
		}
	}

	public int getBackgroundColor() {
		return backgroundColor;
	}

	public int getProgressColor() {
		return progressColor;
	}

	public void setProgress(float progress) {
		progress = (progress > max) ? max : progress;
		progress = (progress < 0) ? 0 : progress;
		this.progress = progress;
		float ratio = max / progress;
		
		LayoutParams params = (LayoutParams) layoutProgress.getLayoutParams();
		params.width = (int) ((backgroundWidth - (padding * 2)) / ratio);
		setProgressColor(progressColor);
		layoutProgress.setLayoutParams(params);
		if (!isProgressBarCreated) {
			isProgressSetBeforeDraw = true;
		}
	}

    public void setProgressStr(){
        if (mPercent != null){
            mPercent.setText((int) progress + "%");
        }
    }

	public float getMax() {
		return max;
	}

	public void setMax(float max) {
		if (!isProgressBarCreated) {
			isMaxProgressSetBeforeDraw = true;
		}
		this.max = max;
	}

	public float getProgress() {
		return progress;
	}

	@SuppressLint("NewApi")
	private float dp2px(float dp) {
		DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
		return Math.round(dp * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
	}

    public void setPercentTextStyle(ProgressDataVO data){
        if (data.isShowText()){
            mPercent.setVisibility(View.VISIBLE);
            mPercent.setTextSize(TypedValue.COMPLEX_UNIT_SP, data.getTextSize());
            mPercent.setTextColor(data.getTextColor());
        }else{
            mPercent.setVisibility(View.GONE);
        }
    }
}