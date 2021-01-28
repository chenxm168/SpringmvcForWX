package xm.wxbase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.alibaba.fastjson.JSONObject;
import xm.httprequest.HttpRequest;

public class CloudTokenManager {
	private static ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();

	protected static Logger log = LogManager.getLogger(CloudTokenManager.class);
	private static SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	// private static String

	public static synchronized String getCloudToken() {

		if (map.containsKey(CONST.TOKEN_KEY)) {
			if (verifyExprires()) {
				return (String) map.get(CONST.TOKEN_KEY);
			}

		}

		if (requestToken()) {
			return (String) map.get(CONST.TOKEN_KEY);
		}

		return StringUtils.EMPTY;
	}

	private static boolean requestToken() {

		HttpRequest https = new HttpRequest(true);
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("grant_type", "client_credential");
		map2.put("appid",CONST. APP_ID);
		map2.put("secret", CONST.APP_SECRET);
		try {
			String rString = https.doGet(
					"https://api.weixin.qq.com/cgi-bin/token", map2);
			log.info(rString);
			JSONObject jsonObject = JSONObject.parseObject(rString);
			String errCode = jsonObject.getString(CONST.ERR_CODE_KEY);
			if (errCode != null) {
				log.error("request cloud tocken error!error message:"
						+ jsonObject.getString(CONST.ERR_MSG_KEY));
				log.error(String
						.format("request cloud tocken error!error!errcode:%s;errmsg:%s",
								jsonObject.getString(CONST.ERR_CODE_KEY),
								jsonObject.getString(CONST.ERR_MSG_KEY)));
				https.Destroy();
				
				return false;
			}
			Date date = new Date(System.currentTimeMillis());
			String timestamp = formatter.format(date);
			map.put(CONST.TOKEN_KEY, jsonObject.getString(CONST.TOKEN_KEY));
			map.put(CONST.GET_TIME_KEY, timestamp);
			map.put(CONST.EXPRIRES_KEY, jsonObject.getString(CONST.EXPRIRES_KEY));
			https.Destroy();
			return true;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			https.Destroy();
		}

		return false;
	}

	private static boolean verifyExprires() {
		String epsString = (String) map.get(CONST.EXPRIRES_KEY);
		String timestamp = (String) map.get(CONST.GET_TIME_KEY);

		try {
			long epsLong = Long.valueOf(epsString);
			epsLong = epsLong -600;
			Date getDate = formatter.parse(timestamp);
			long l1 = getDate.getTime() + (epsLong * 1000);
			Calendar getCalendar = Calendar.getInstance();
			getCalendar.setTimeInMillis(l1);
			Calendar nowCalendar = Calendar.getInstance();
			log.info(formatter.format(getCalendar.getTime()));
			log.info(formatter.format(nowCalendar.getTime()));
			if (getCalendar.after(nowCalendar)) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return false;
	}

}
