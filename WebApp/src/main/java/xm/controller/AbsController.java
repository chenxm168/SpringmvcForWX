package xm.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.apache.logging.log4j.*;

import xm.message.IWXMessageHandler;

public class AbsController implements ApplicationContextAware,IJsonDataGetable {

	 protected ApplicationContext contenxt = null;
	protected Logger logger =LogManager.getLogger (AbsController.class);
	protected IWXMessageHandler wxmessagehandler;
	

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.contenxt = context;

	}
	
	public IWXMessageHandler getWxmessagehandler() {
		return wxmessagehandler;
	}
      @Autowired
	public void setWxmessagehandler(IWXMessageHandler wxmessagehandler) {
		this.wxmessagehandler = wxmessagehandler;
	}

	public ApplicationContext getApplicationContext()
	{
		return this.contenxt;
	}

	public String getJsonData(HttpServletRequest request) {
		

		if (!request.getContentType().equalsIgnoreCase("application/json")  ) {
			return null;
		}

		
			String jsonString = "";

			try {
				BufferedReader streamReader = new BufferedReader(
						new InputStreamReader(request.getInputStream(), "UTF-8"));
				StringBuilder responseStrBuilder = new StringBuilder();
				String inputStr;
				while ((inputStr = streamReader.readLine()) != null) {
					responseStrBuilder.append(inputStr);
				}
				String value = responseStrBuilder.toString();

				jsonString = value;

			} catch (Exception e) {
				logger.error(e.getMessage());
			}

			return jsonString;

		

	}

}
