package org.zywx.wbpalmstar.plugin.uexprogressview.VO;

import org.zywx.wbpalmstar.base.BUtility;

import java.io.Serializable;

public class ProgressDataVO implements Serializable{
    private static final long serialVersionUID = 4101604695371771720L;
    private String id = "";
    private double left = 0;
    private double top = 0;
    private double width = -1;
    private double height = -1;
    private int type = 1;
    private double progress = 0f;
    private boolean isScrollWithWeb = false;
    private String normalColor = "#ccc";
    private String progressColor = "#4291f1";
    private boolean isShowText = true;
    private int textSize = 12;
    private String textColor = "#000";
    private String borderColor = "#f00";
    private String bgColor = "#00000000";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLeft() {
        return (int) left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public int getTop() {
        return (int) top;
    }

    public void setTop(double top) {
        this.top = top;
    }

    public int getWidth() {
        return (int) width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public int getHeight() {
        return (int) height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public boolean isScrollWithWeb() {
        return isScrollWithWeb;
    }

    public void setIsScrollWithWeb(boolean isScrollWithWeb) {
        this.isScrollWithWeb = isScrollWithWeb;
    }
    public int getNormalColor() {
        return BUtility.parseColor(normalColor);
    }

    public void setNormalColor(String normalColor) {
        this.normalColor = normalColor;
    }

    public int getProgressColor() {
        return BUtility.parseColor(progressColor);
    }

    public void setProgressColor(String progressColor) {
        this.progressColor = progressColor;
    }

    public boolean isShowText() {
        return isShowText;
    }

    public void setIsShowText(boolean isShowText) {
        this.isShowText = isShowText;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getBorderColor() {
        return BUtility.parseColor(borderColor);
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public int getTextColor() {
        return BUtility.parseColor(textColor);
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public int getBgColor() {
        return BUtility.parseColor(bgColor);
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }
}
