package xm.httprequest;

import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;

import com.alibaba.fastjson.*;

public interface IHttpRequest {
	
	public String doGet(String httpUrl, Map<String, String> params) throws Exception;
	public String doPost(String httpUrl, String message) throws Exception;
	
	public String doPost(String httpUrl, Map<String, String> params) throws Exception;
	
	public JSONObject doPostData(String httpUrl, Object obj) throws Exception;
	
	public String doPostJsonData(String httpUrl,String jsonstring) throws Exception;
	
	public String doPostJsonData(String httpUrl, JSONObject json) throws Exception;
	
	public void setClient(CloseableHttpClient client);
	public void setUrlString(String url);
	
}

