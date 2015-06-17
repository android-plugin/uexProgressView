package org.zywx.wbpalmstar.plugin.uexprogressview;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;
import org.zywx.wbpalmstar.engine.DataHelper;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.plugin.uexprogressview.VO.ProgressDataVO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EUExProgressView extends EUExBase {

    private static final String BUNDLE_DATA = "data";
    private static final int MSG_OPEN = 1;
    private static final int MSG_SET_PROGRESS = 2;
    private static final int MSG_SET_VIEW_STYLE = 3;
    private static final int MSG_CLOSE = 4;
    private LocalActivityManager mgr;
    private String activityId;
    private List<ProgressDataVO> mIDs;


    public EUExProgressView(Context context, EBrowserView eBrowserView) {
        super(context, eBrowserView);
        mgr = ((ActivityGroup)mContext).getLocalActivityManager();
        activityId = EProgressViewUtils.PROGRESS_PARAMS_KEY_ACTIVITYID + this.hashCode();
        mIDs = new ArrayList<ProgressDataVO>();
    }

    @Override
    protected boolean clean() {
        return false;
    }


    public void open(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_OPEN;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void openMsg(String[] params) {
        String json = params[0];
        ProgressDataVO dataVO = DataHelper.gson.fromJson(json, ProgressDataVO.class);
        String id = getTagID(dataVO.getId());
        if (isAlreadyAdded(id)){
            return;
        }
        Intent intent = new Intent(mContext, EProgressViewActivity.class);
        intent.putExtra(JsConst.PARAMS_PROGRESS_DATA, dataVO);
        intent.putExtra(JsConst.PARAMS_CALLBACK_LISTENER, listener);
        Window window = mgr.startActivity(id, intent);
        View decorView = window.getDecorView();
        if (dataVO.isScrollWithWeb()){
            android.widget.AbsoluteLayout.LayoutParams lp = new
                    android.widget.AbsoluteLayout.LayoutParams(
                    dataVO.getWidth(),
                    dataVO.getHeight(),
                    dataVO.getLeft(),
                    dataVO.getTop());
            addViewToWebView(decorView, lp, id);
        }else{
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    dataVO.getWidth(), dataVO.getHeight());
            lp.leftMargin = dataVO.getLeft();
            lp.topMargin = dataVO.getTop();
            addView2CurrentWindow(decorView, lp);
        }
        mIDs.add(dataVO);
    }

    private void addView2CurrentWindow(View child,
                                       RelativeLayout.LayoutParams parms) {
        int l = parms.leftMargin;
        int t = parms.topMargin;
        int w = parms.width;
        int h = parms.height;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(w, h);
        lp.gravity = Gravity.NO_GRAVITY;
        lp.leftMargin = l;
        lp.topMargin = t;
        adptLayoutParams(parms, lp);
        mBrwView.addViewToCurrentWindow(child, lp);
    }

    public void setProgress(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_SET_PROGRESS;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void setProgressMsg(String[] params) {
        String json = params[0];
        ProgressDataVO dataVO = DataHelper.gson.fromJson(json, ProgressDataVO.class);
        String tag = getTagID(dataVO.getId());
        if (isAlreadyAdded(tag)){
            EProgressViewActivity activity = (EProgressViewActivity) mgr.getActivity(tag);
            activity.setProgressStr((int) dataVO.getProgress());
        }
    }

    public void setViewStyle(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_SET_VIEW_STYLE;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void setViewStyleMsg(String[] params) {
        String json = params[0];
    }

    public void close(String[] params) {
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_CLOSE;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void closeMsg(String[] params) {
        List<String> list = new ArrayList<String>();
        List<String> tempList = new ArrayList<String>();
        if (params != null && params.length > 0){
            String json = params[0];
            tempList = DataHelper.gson.fromJson(json, new TypeToken<List<String>>(){}.getType());
        }
        if (tempList == null || tempList.size() < 1){
            for (int i = 0; i < mIDs.size(); i++){
                list.add(mIDs.get(i).getId());
            }
        }else{
            for (int j = 0; j < tempList.size(); j++){
                final String id = tempList.get(j);
                list.add(id);
            }
        }
        for (int i = 0; i < list.size(); i++){
            final String item = list.get(i);
            ProgressDataVO chartVO = getProgressFromIDs(item);
            if (chartVO != null){
                String id = getTagID(chartVO.getId());
                if (isAlreadyAdded(id)){
                    if (chartVO.isScrollWithWeb()){
                        removeViewFromWebView(id);
                    }else{
                        removeProgressView(id);
                    }
                    mgr.destroyActivity(id, true);
                    removeIdFormIDs(item);
                }
            }
        }
    }

    private void removeIdFormIDs(String id){
        Iterator<ProgressDataVO> iterator = mIDs.iterator();
        while (iterator.hasNext()){
            ProgressDataVO item = iterator.next();
            if (id.equals(item.getId())){
                iterator.remove();
            }
        }
    }

    private void removeProgressView(String id){
        View view = mgr.getActivity(id).getWindow().getDecorView();
        if (view.getParent() != null) {
            ((ViewGroup)view.getParent()).removeView(view);
        }
    }

    private ProgressDataVO getProgressFromIDs(String id){
        ProgressDataVO progressVO = null;
        Iterator<ProgressDataVO> iterator = mIDs.iterator();
        while (iterator.hasNext()){
            ProgressDataVO item = iterator.next();
            if (id.equals(item.getId())){
                progressVO = item;
            }
        }
        return progressVO;
    }

    @Override
    public void onHandleMessage(Message message) {
        if(message == null){
            return;
        }
        Bundle bundle=message.getData();
        switch (message.what) {

            case MSG_OPEN:
                openMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            case MSG_SET_PROGRESS:
                setProgressMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            case MSG_SET_VIEW_STYLE:
                setViewStyleMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            case MSG_CLOSE:
                closeMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            default:
                super.onHandleMessage(message);
        }
    }

    private void callBackPluginJs(String methodName, String jsonData){
        String js = SCRIPT_HEADER + "if(" + methodName + "){"
                + methodName + "('" + jsonData + "');}";
        onCallback(js);
    }

    private String getTagID(String id){
        return EProgressViewActivity.TAG + id;
    }

    private boolean isAlreadyAdded(String tagID) {
        return mgr.getActivity(tagID) != null;
    }

    OnProgressComplete listener = new OnProgressComplete() {
        @Override
        public void onComplete(String id) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(JsConst.RESULT_ID, id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            callBackPluginJs(JsConst.ON_COMPLETE, jsonObject.toString());
        }
    };

    public interface OnProgressComplete extends Serializable{
        public void onComplete(String id);
    }
}
