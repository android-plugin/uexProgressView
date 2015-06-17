package org.zywx.wbpalmstar.plugin.uexprogressview;

public class ProgressStyle {

	public final static String type_key = "type";
	public final static String style_key = "style";
	public final static String normalColor_key  = "normalColor";
	public final static String progressColor_key  = "progressColor";
	public final static String isShowText_key  = "isShowText";
	public final static String textSize_key  = "textSize";
	public final static String borderCorlor_key  = "borderCorlor";
	
	/**
	 * 设置热门城市
	 * @param msg
	 * @return
	 */
	public static Object[] getProgressStyle(String msg){
		if (msg == null || msg.length() == 0) {
			return null;
		}
//		try {
//			JSONObject jsonObj = new JSONObject(msg);
//			if (jsonObj != null) {
//				//定位城市
//				String type = jsonObj.optString(type_key);
//				JSONObject styleObj = jsonObj.optJSONObject(style_key);
//				Style styleBean = null;
//				if(styleObj != null){
//					styleBean = new Style();
//					String normalColor = styleObj.optString(normalColor_key);
//					String progressColor = styleObj.optString(progressColor_key);
//					String isShowText = styleObj.optString(isShowText_key);
//					String textSize = styleObj.optString(textSize_key);
//					String borderCorlor = styleObj.optString(borderCorlor_key);
//					styleBean.setNormalColor(normalColor);
//					styleBean.setProgressColor(progressColor);
//					styleBean.setIsShowText(isShowText);
//					styleBean.setTextSize(textSize);
//					styleBean.setBorderCorlor(borderCorlor);
//				}
//				return new Object[]{type,styleBean};
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return null;
	} 
}
