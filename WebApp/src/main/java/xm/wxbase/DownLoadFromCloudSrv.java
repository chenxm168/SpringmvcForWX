package xm.wxbase;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import xm.httprequest.HttpRequest;

public class DownLoadFromCloudSrv implements ApplicationContextAware {
	protected Logger log = LogManager.getLogger(this.getClass());
	private ApplicationContext context;
	private String tocken;

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.context = context;

	}

	private DownLoadFromCloudSrv(String token) {
		this.tocken = token;
	}

	public static DownLoadFromCloudSrv getInstance(String token) {
		return new DownLoadFromCloudSrv(token);
	}

	private JSONObject getDownLoadUrl(JSONObject jsb) {

		JSONArray jsa = jsb.getJSONArray(CONST.FILE_LIST_KEY);

		String url = CONST.DEFAULT_DOWNLOAD_HTTPS_URL + this.tocken;
		HttpRequest https = new HttpRequest(true);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(CONST.ENV_KEY, CONST.DEFAULT_ENV);
		JSONArray jsonArray = new JSONArray();

		for (Object object : jsa) {
			JSONObject jsb2 = (JSONObject) object;
			JSONObject jObject = new JSONObject();
			jObject.put(CONST.FILE_ID_KEY, jsb2.getString(CONST.FILE_ID_KEY));
			jObject.put(CONST.MAX_AGE_KEY, CONST.DEFAULT_MAX_AGE);
			jsonArray.add(jObject);

		}

		jsonObject.put(CONST.FILE_LIST_KEY, jsonArray);
		String msg = jsonObject.toJSONString();
		try {
			String rString = https.doPostJsonData(url, msg);
			log.info("get download url message!");
			log.info(rString);

			JSONObject jo = JSONObject.parseObject(rString);
			String errcode = jo.getString(CONST.ERR_CODE_KEY);
			if (Integer.valueOf(errcode) != 0) {
				log.error(String.format(
						"get download url error! errcode:%s;errmsg:%s",
						jo.getString(CONST.ERR_CODE_KEY),
						(String) jo.getString(CONST.ERR_MSG_KEY)));
				https.Destroy();
				return null;
			}
			JSONArray fja = jo.getJSONArray(CONST.FILE_LIST_KEY);

			for (Object object : fja) {
				JSONObject jo2 = (JSONObject) object;

				for (int i = 0; i < jsa.size(); i++) {
					if (((JSONObject) jsa.get(i)).getString(CONST.FILE_ID_KEY)
							.equals(jo2.getString(CONST.FILE_ID_KEY))) {
						((JSONObject) jsa.get(i)).put(CONST.DOWNLOAD_URL_KEY,
								jo2.getString(CONST.DOWNLOAD_URL_KEY));
						break;
					}
				}// end for jsa

				/*
				 * StringBuilder sb =new StringBuilder(); sb.append("$[0,");
				 * sb.append(String.valueOf(jsa.size()));
				 * sb.append("]["+CONST.FILE_ID_KEY+"='");
				 * sb.append(jo2.getString(CONST.FILE_ID_KEY)+"']"); String
				 * jPath=sb.toString(); log.debug(jPath); Object object2=
				 * JSONPath.eval(jsa,jPath);
				 * 
				 * List<Object> list =(List<Object>) object2;
				 * 
				 * for (Object object3 : list) { log.debug(object3.getClass());
				 * JSONObject jo3 =(JSONObject) object3;
				 * jo3.put(CONST.DOWNLOAD_URL_KEY,
				 * jo2.getString(CONST.DOWNLOAD_URL_KEY));
				 * 
				 * }
				 * 
				 * 
				 * 
				 * log.debug(object2.getClass()); if(object2 instanceof
				 * JSONArray) { log.debug(object2); JSONArray
				 * ja2=(JSONArray)object2; for (Object object3 : ja2) {
				 * JSONObject jo3= (JSONObject) object3;
				 * jo3.put(CONST.DOWNLOAD_URL_KEY,
				 * jo2.get(CONST.DOWNLOAD_URL_KEY));
				 * 
				 * } continue; } if(object2 instanceof String) {
				 * log.debug(object2); }
				 * 
				 * if(object2 instanceof JSONObject) { log.debug(object2); }
				 */

				// JSONPath.eval(jsa, path)

			}

			/*
			 * Map<String, String> map= new HashMap<String, String>(); for
			 * (Object object : fja) { JSONObject jo2= (JSONObject) object;
			 * String status= jo2.getString(CONST.STATUS_KEY);
			 * if(Integer.valueOf(status)!=0) { continue; }
			 * map.put(jo2.getString(CONST.FILE_ID_KEY),
			 * jo2.getString(CONST.DOWNLOAD_URL_KEY));
			 * 
			 * }
			 */
			// https.Destroy();
			https.Destroy();
			return jsb;

		} catch (Exception e) {
			https.Destroy();
			log.error(e.getMessage(), e);

		}

		return null;
	}

	public JSONObject downFile(JSONObject jsonObject) {
		JSONObject jso = this.getDownLoadUrl(jsonObject);
		HttpRequest https = new HttpRequest(true);
		JSONObject json = https.DownloadFile(jso);
		this.putInUploadTask(json);
		return json;
	}

	private void putInUploadTask(JSONObject json) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date nowDate = new Date(System.currentTimeMillis());
		String nowString = formatter.format(nowDate);
		String fPath = CONST.UPLOAD_TASK_ROOT_PATH + "\\" + nowString + ".txt";
		FileOutputStream fs = null;
		try {
			File file = new File(fPath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdir();
			}
			fs = new FileOutputStream(file);
			String wrsString = json.toJSONString();
			fs.write(wrsString.getBytes());

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (fs != null) {
				try {
					fs.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}

	}

}
