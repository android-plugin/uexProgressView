package org.zywx.wbpalmstar.plugin.uexprogressview;

import org.zywx.wbpalmstar.engine.universalex.EUExUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RoundCornerProgressBar extends LinearLayout {
	private final static int DEFAULT_PROGRESS_BAR_HEIGHT = 30;

	private LinearLayout layoutBackground;
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
	private int progressColor = Color.rgb(66,145,241);
	private int backgroundColor = Color.rgb(66,145,241);

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
			setBackgroundColor(Color.rgb(66,145,241));
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
		layoutBackground = (LinearLayout) findViewById(EUExUtil.getResIdID("round_corner_progress_background"));
		linearlayoutparent = (LinearLayout) findViewById(EUExUtil.getResIdID("linearlayoutparent"));
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
		progressColor = color;
		int radius = this.radius - padding / 2;
		GradientDrawable gradient = new GradientDrawable();
		gradient.setShape(GradientDrawable.RECTANGLE);
		gradient.setColor(progressColor);
		gradient.setCornerRadii(new float[] { radius, radius, radius, radius, radius, radius, radius, radius });
		layoutProgress.setBackgroundDrawable(gradient);
		if (!isProgressBarCreated) {
			isProgressColorSetBeforeDraw = true;
		}
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public void setBackgroundColor(int color) {
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
}