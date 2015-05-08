package org.zywx.wbpalmstar.plugin.uexprogressview;

import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.plugin.uexprogressview.NumberProgressBar.ProgressTextVisibility;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 进度条显示
 * 
 * @author user
 * 
 */
public class EProgressViewActivity extends Activity implements
		OnClickListener {

	private NumberProgressBar bnp;
	private RoundCornerProgressBar progressTwo;
	private RoundProgressBar roundProgressBarFill;
	private EUExProgressView euExProgressView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		euExProgressView = (EUExProgressView) getIntent().getSerializableExtra(EProgressViewUtils.PROGRESS_PARAMS_KEY_OBJ);
		setContentView(EUExUtil.getResLayoutID("plugin_uexprogressview_main"));
	}

	private void showNumberProgressBar(Style style) {
		bnp = (NumberProgressBar) findViewById(EUExUtil.getResIdID("numberbar"));
		bnp.setVisibility(View.VISIBLE);
		if (style != null) {
			if (!TextUtils.isEmpty(style.getNormalColor())) {
				bnp.setUnreachedBarColor(ImageColorUtils.parseColor(style
						.getNormalColor()));
			}
			if (!TextUtils.isEmpty(style.getProgressColor())) {
				bnp.setReachedBarColor(ImageColorUtils.parseColor(style
						.getProgressColor()));
			}
			if (!TextUtils.isEmpty(style.getTextColor())) {
				bnp.setProgressTextColor(ImageColorUtils.parseColor(style
						.getTextColor()));
			}
			if ("1".equals(style.getIsShowText())) {
				bnp.setProgressTextVisibility(ProgressTextVisibility.Invisible);
			} else {
				bnp.setProgressTextVisibility(ProgressTextVisibility.Visible);
			}
			try {
				bnp.setProgressTextSize(Integer.parseInt(style.getTextSize()));
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
	}
	
	public void setProgressStr(String progressBarStr){
		try{
			int progressBar = Integer.parseInt(progressBarStr);
			if(bnp != null){
				bnp.incrementProgressBy(1);
				bnp.setProgress(progressBar);
			}
			if(progressTwo != null){
				progressTwo.setProgress(progressBar);
			}
			if(roundProgressBarFill != null){
				roundProgressBarFill.setProgress(progressBar);
			}
			if(progressBar == 100) {
				euExProgressView.callBack();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void setViewStyle(Object[] objectArray) {
		if (objectArray != null && objectArray.length > 0) {
			String type = (String) objectArray[0];
			Style style = null;
			if (objectArray.length == 2) {
				style = (Style) objectArray[1];
			} else {
				style = null;
			}
			if ("1".equals(type)) {
				showNumberProgressBar(style);
			} else if ("2".equals(type)) {
				updateProgress(style);
			} else if ("3".equals(type)) {
				updateFillCricelProgress(style, RoundProgressBar.FILL);
			} else if ("4".equals(type)) {
				updateFillCricelProgress(style, RoundProgressBar.STROKE);
			}
		}
	}

	int progress = 0;

	private void updateFillCricelProgress(Style style, int roundBarStyle) {
		roundProgressBarFill = (RoundProgressBar) findViewById(EUExUtil.getResIdID("roundProgressBarFill"));
		roundProgressBarFill.setVisibility(View.VISIBLE);
		roundProgressBarFill.setStyle(roundBarStyle);
		if (style != null) {
			String borderCorlor = style.getBorderCorlor();
			if (!TextUtils.isEmpty(borderCorlor)) {
				roundProgressBarFill.setCricleColor(ImageColorUtils
						.parseColor(borderCorlor));
			}
			String normalCorlor = style.getNormalColor();
			if (!TextUtils.isEmpty(normalCorlor)) {
				roundProgressBarFill.setCireBackroundColor(ImageColorUtils
						.parseColor(normalCorlor));
			}
			String progressColor = style.getProgressColor();
			if (!TextUtils.isEmpty(progressColor)) {
				roundProgressBarFill.setCricleProgressColor(ImageColorUtils
						.parseColor(progressColor));
			}
			String textColor = style.getTextColor();
			if (!TextUtils.isEmpty(textColor)) {
				roundProgressBarFill.setTextColor(ImageColorUtils
						.parseColor(textColor));
			}
		}
	}

	private void updateProgress(Style style) {
		progressTwo = (RoundCornerProgressBar) findViewById(EUExUtil.getResIdID("progress_two"));
		progressTwo.setVisibility(View.VISIBLE);
		if (style != null) {
			String borderCorlor = style.getBorderCorlor();
			String normalCorlor = style.getNormalColor();
			String progressColor = style.getProgressColor();
			if (!TextUtils.isEmpty(borderCorlor)) {
				progressTwo.setBackgroundColor(ImageColorUtils
						.parseColor(borderCorlor));
			} else {
				progressTwo.setBackgroundColor(Color.rgb(66, 145, 241));
			}
			if (!TextUtils.isEmpty(normalCorlor)) {
				progressTwo.setRoundLinearlayoutColor(ImageColorUtils
						.parseColor(normalCorlor));
			}
			if (!TextUtils.isEmpty(progressColor)) {
				progressTwo.setProgressColor(ImageColorUtils
						.parseColor(progressColor));
			}
		} else {
			progressTwo.setBackgroundColor(Color.rgb(66, 145, 241));
		}
	}

}
