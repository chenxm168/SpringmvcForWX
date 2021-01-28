package xm.scheduler;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import xm.CONST;
import xm.httprequest.HttpRequest;
import xm.wxbase.CloudTokenManager;
import xm.wxbase.DownLoadFromCloudSrv;



	
	
	public class ScheduleJob1 implements IScheduleJob ,ApplicationContextAware{
		private ApplicationContext context;
		
		public void  doSchedule() {
			
			//WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
			
			Logger logger = LogManager.getLogger(ScheduleJob1.class);
			logger.debug("Start Schedule");

            String token= CloudTokenManager.getCloudToken();
            logger.info(token);
            DownLoadFromCloudSrv srv= DownLoadFromCloudSrv.getInstance(token);
            JSONObject jsonObject=new JSONObject();
            JSONArray  jsonArray = new JSONArray();
            
            
            
            List<String> list= new ArrayList<String>();
            list.add("cloud://asd-smart-cloud-k2u5e.6173-asd-smart-cloud-k2u5e-1259294007/AsdTempFile/fixbug.jpg");
            list.add("cloud://asd-smart-cloud-k2u5e.6173-asd-smart-cloud-k2u5e-1259294007/AsdTempFile/image0.jpg");
            for (String string : list) {
            	JSONObject jo= new JSONObject();
            	jo.put(CONST.FILE_ID_KEY, string);
            	jo.put(CONST.LOCAL_ROOT_PATH_KEY, CONST.LOCAL_ROOT_PATH);
            	jsonArray.add(jo);
				
			}
            jsonObject.put(CONST.FILE_LIST_KEY, jsonArray);
            
            srv.downFile(jsonObject);
			
			
			
		}

		@Override
		public void setApplicationContext(ApplicationContext context)
				throws BeansException {
			this.context=context;
			
		}

}
