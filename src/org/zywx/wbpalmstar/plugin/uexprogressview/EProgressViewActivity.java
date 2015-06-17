package org.zywx.wbpalmstar.plugin.uexprogressview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.plugin.uexprogressview.EUExProgressView.OnProgressComplete;
import org.zywx.wbpalmstar.plugin.uexprogressview.NumberProgressBar.ProgressTextVisibility;
import org.zywx.wbpalmstar.plugin.uexprogressview.VO.ProgressDataVO;

/**
 * 进度条显示
 * 
 * @author user
 * 
 */
public class EProgressViewActivity extends Activity implements
        OnClickListener {

    public static final String TAG = "EProgressViewActivity";
    private NumberProgressBar bnp;
    private RoundCornerProgressBar progressTwo;
    private RoundProgressBar roundProgressBarFill;
    //private EUExProgressView euExProgressView;
    private ProgressDataVO dataVO;
    private OnProgressComplete mListener;
    private LinearLayout mCon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null){
            dataVO = (ProgressDataVO)intent
                    .getSerializableExtra(JsConst.PARAMS_PROGRESS_DATA);
            mListener = (OnProgressComplete) intent
                    .getSerializableExtra(JsConst.PARAMS_CALLBACK_LISTENER);
        }
        if (dataVO == null || mListener == null){
            return;
        }
        setContentView(EUExUtil.getResLayoutID("plugin_uexprogressview_main"));
        mCon = (LinearLayout) findViewById(
                EUExUtil.getResIdID("plugin_uexprogress_bg"));
        initProgressStyle();
        setProgressStr((int) dataVO.getProgress());
    }

    private void showNumberProgressBar() {
        bnp = (NumberProgressBar) findViewById(EUExUtil.getResIdID("numberbar"));
        bnp.setVisibility(View.VISIBLE);
        bnp.setUnreachedBarColor(dataVO.getNormalColor());
        bnp.setReachedBarColor(dataVO.getProgressColor());
        bnp.setProgressTextColor(dataVO.getTextColor());
        if (!dataVO.isShowText()) {
            bnp.setProgressTextVisibility(ProgressTextVisibility.Invisible);
        } else {
            bnp.setProgressTextVisibility(ProgressTextVisibility.Visible);
        }
        bnp.setProgressTextSize(dataVO.getTextSize());
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

    private void initProgressStyle(){
        int type = dataVO.getType();
        mCon.setBackgroundColor(dataVO.getBgColor());
        switch (type){
            case JsConst.PROGRESS_TYPE_NUMBER:
                showNumberProgressBar();
                break;
            case JsConst.PROGRESS_TYPE_PROGRESS:
                updateProgress();
                break;
            case JsConst.PROGRESS_TYPE_FILLCRICELPROGRESS:
                updateFillCricleProgress(RoundProgressBar.FILL);
                break;
            case JsConst.PROGRESS_TYPE_STROKECRICELPROGRESS:
                updateFillCricleProgress(RoundProgressBar.STROKE);
                break;
            default:
                showNumberProgressBar();
                break;
        }
    }

    public void setProgressStr(int progress){
        try{
            if(bnp != null){
                bnp.incrementProgressBy(1);
                bnp.setProgress(progress);
            }
            if(progressTwo != null){
                progressTwo.setProgress(progress);
                progressTwo.setProgressStr();
            }
            if(roundProgressBarFill != null){
                roundProgressBarFill.setProgress(progress);
            }
            if(progress == 100) {
                mListener.onComplete(dataVO.getId());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void updateFillCricleProgress(int roundBarStyle) {
        roundProgressBarFill = (RoundProgressBar) findViewById(
                EUExUtil.getResIdID("roundProgressBarFill"));
        roundProgressBarFill.setVisibility(View.VISIBLE);
        roundProgressBarFill.setStyle(roundBarStyle);
        roundProgressBarFill.setCricleColor(dataVO.getBorderColor());
        roundProgressBarFill.setCireBackroundColor(dataVO.getNormalColor());
        roundProgressBarFill.setCricleProgressColor(dataVO.getProgressColor());
        roundProgressBarFill.setTextIsDisplayable(dataVO.isShowText());
        float textSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, dataVO.getTextSize(), this.getResources().getDisplayMetrics());
        roundProgressBarFill.setTextSize(textSize);
        roundProgressBarFill.setTextColor(dataVO.getTextColor());
    }

    private void updateProgress() {
        progressTwo = (RoundCornerProgressBar) findViewById(EUExUtil.getResIdID("progress_two"));
        progressTwo.setVisibility(View.VISIBLE);
        progressTwo.setBackgroundColor(dataVO.getBorderColor());
        progressTwo.setRoundLinearlayoutColor(dataVO.getNormalColor());
        progressTwo.setProgressColor(dataVO.getProgressColor());
        progressTwo.setPercentTextStyle(dataVO);
    }

}
