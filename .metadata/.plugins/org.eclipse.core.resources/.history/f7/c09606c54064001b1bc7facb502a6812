package xm.message;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import xm.Tib.ITibInterface;
import xm.message.wx.MsgBaseUtil;

public class WXMessageSrv implements IWXMessageHandler ,ApplicationContextAware {
	
	   private ApplicationContext contex;
	   private Map<String, IWXMessageHandler> handlerMap=new HashMap<String, IWXMessageHandler>();
	   protected MsgBaseUtil util=null;

	public Map<String, IWXMessageHandler> getHandlerMap() {
		return handlerMap;
	}

	public void setHandlerMap(Map<String, IWXMessageHandler> handlerMap) {
		this.handlerMap = handlerMap;
	}

	@Override
	public String Process(String jsonMessage) {
        
		//String xml = MsgUtils.Json2Xml(jsonMessage);
		//String service = MsgUtils.getWXMsgServiceName(xml);
		try {
			MsgBaseUtil util =(MsgBaseUtil) contex.getBean("msgbaseutil");
			String xml = util.Json2Xml(jsonMessage);
			String service = util.getWXMsgServiceName(xml);
			
			IWXMessageHandler handler= (IWXMessageHandler) contex.getBean(service);
			
			if(handler.getTib()==null)
			{
				ITibInterface tib =(ITibInterface) contex.getBean("tib");
				handler.setTib(tib);
			}
			
			
			handler.setUtil(util);
			
			if(handler!=null)
			{
				return handler.Process(jsonMessage);
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		   

		return StringUtils.EMPTY;
	}

	@Override
	public void setApplicationContext(ApplicationContext contex)
			throws BeansException {
		this.contex =contex;
		
	}

	@Override
	public void setUtil(MsgBaseUtil util) {
		this.util=util;
		
	}

	@Override
	public void setTib(ITibInterface tib) {
		
		
	}

	public ITibInterface getTib() {
		// TODO Auto-generated method stub
		return null;
	}
	

	
	

}
