package org.zywx.wbpalmstar.plugin.uexprogressview;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

@SuppressWarnings({ "deprecation", "serial" })
public class EUExProgressView extends EUExBase implements Serializable {
	
	public static final String SCRIPT_HEADER = "javascript:";
	public static final String CALLBACK_ONCOMPLETE = "uexProgressView.onComplete";
	private LocalActivityManager mgr;
	private String activityId;
	
	public EUExProgressView(Context context, EBrowserView inParent) {
		super(context, inParent);
		mgr = ((ActivityGroup)mContext).getLocalActivityManager();
		activityId = EProgressViewUtils.PROGRESS_PARAMS_KEY_ACTIVITYID + this.hashCode();
	}
	
	public void open(String[] params) {
		sendMessageInProgress(EProgressViewUtils.PROGRESS_MSG_CODE_OPEN, params);
	}

	public void setProgress(final String[] params) {
		sendMessageInProgress(EProgressViewUtils.PROGRESS_MSG_CODE_SETPROGRESS, params);
	}
	
	public void setViewStyle(final String[] params) {
		sendMessageInProgress(EProgressViewUtils.PROGRESS_MSG_CODE_SETVIEWSTYLE, params);
	}
	
	public void close(String[] params) {
		sendMessageInProgress(EProgressViewUtils.PROGRESS_MSG_CODE_CLOSE, params);
	}
	
	private void sendMessageInProgress(int msgType, String[] params) {
		if(mHandler == null) {
			return;
		}
		Message msg = Message.obtain();
		msg.what = msgType;
		msg.obj = this;
		Bundle b = new Bundle();
		b.putStringArray(EProgressViewUtils.PROGRESS_PARAMS_KEY_FUNCTION, params);
		msg.setData(b);
		mHandler.sendMessage(msg);
	}
	
	@Override
	public void onHandleMessage(Message msg) {
		if(msg.what == EProgressViewUtils.PROGRESS_MSG_CODE_OPEN) {
			handleOpen(msg);
		}else {
			handleInProgress(msg);
		}
	}

	private void handleInProgress(Message msg) {
		Activity activity = mgr.getActivity(activityId);
		if (activity != null && activity instanceof EProgressViewActivity) {
			EProgressViewActivity eProgressViewActivity = ((EProgressViewActivity) activity);
			String[] params = msg.getData().getStringArray(EProgressViewUtils.PROGRESS_PARAMS_KEY_FUNCTION);
			switch (msg.what) {
			case EProgressViewUtils.PROGRESS_MSG_CODE_SETPROGRESS:
				handleSetProgress(params, eProgressViewActivity);
				break;
			case EProgressViewUtils.PROGRESS_MSG_CODE_SETVIEWSTYLE:
				handleSetViewStyle(params, eProgressViewActivity);
				break;
			case EProgressViewUtils.PROGRESS_MSG_CODE_CLOSE:
				handleClose(params, eProgressViewActivity);
				break;
			}
		}
	}

	private void handleClose(String[] params, EProgressViewActivity eProgressViewActivity) {
		View decorView = eProgressViewActivity.getWindow().getDecorView();
		removeViewFromCurrentWindow(decorView);
		mgr.destroyActivity(activityId, true);
	}

	private void handleSetViewStyle(String[] params, EProgressViewActivity eProgressViewActivity) {
		Object[] objectArray = ProgressStyle.getProgressStyle(params[0]);
		eProgressViewActivity.setViewStyle(objectArray);
	}

	private void handleSetProgress(String[] params, EProgressViewActivity eProgressViewActivity) {
		String jsonStr = params[0];
		try {
			JSONObject jsonObj = new JSONObject(jsonStr);
			String progress = jsonObj.optString("progress");
			eProgressViewActivity.setProgressStr(progress);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void handleOpen(Message msg) {
		String[] params = msg.getData().getStringArray(EProgressViewUtils.PROGRESS_PARAMS_KEY_FUNCTION);
		if(params == null || params.length != 1) {
			return;
		}
		try {
			JSONObject json = new JSONObject(params[0]);
			float x = Float.parseFloat(json.getString(EProgressViewUtils.PROGRESS_PARAMS_KEY_X));
			float y = Float.parseFloat(json.getString(EProgressViewUtils.PROGRESS_PARAMS_KEY_Y));
			float w = Float.parseFloat(json.getString(EProgressViewUtils.PROGRESS_PARAMS_KEY_W));
			float h = Float.parseFloat(json.getString(EProgressViewUtils.PROGRESS_PARAMS_KEY_H));
			Intent intent = new Intent(mContext, EProgressViewActivity.class);
			EProgressViewActivity eProgressViewActivity = (EProgressViewActivity) mgr.getActivity(activityId);
			if (eProgressViewActivity != null) {
				View view = eProgressViewActivity.getWindow().getDecorView();
				removeViewFromCurrentWindow(view);
				mgr.destroyActivity(activityId, true);
				view = null;
			}
			intent.putExtra(EProgressViewUtils.PROGRESS_PARAMS_KEY_OBJ, this);
			Window window = mgr.startActivity(activityId, intent);
			View decorView = window.getDecorView();
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int) w, (int) h);
			lp.topMargin = (int) y;
			lp.leftMargin = (int) x;
			addView2CurrentWindow(decorView, lp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addView2CurrentWindow(View child,
			RelativeLayout.LayoutParams parms) {
		int l = (int) (parms.leftMargin);
		int t = (int) (parms.topMargin);
		int w = parms.width;
		int h = parms.height;
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(w, h);
		lp.gravity = Gravity.NO_GRAVITY;
		lp.leftMargin = l;
		lp.topMargin = t;
		adptLayoutParams(parms, lp);
		mBrwView.addViewToCurrentWindow(child, lp);
	}
	
	public void callBack() {
		String js = SCRIPT_HEADER + "if(" + CALLBACK_ONCOMPLETE + "){" +CALLBACK_ONCOMPLETE + "()}";
		onCallback(js);
	}
	
	@Override
	protected boolean clean() {
		close(null);
		return false;
	}

}
