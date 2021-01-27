package xm.message.wx;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;

import xm.message.MsgUtils;

public class AsdUserLoginSrv extends AbsWXMsgSrv {
	
	

	@Override
	public String Process(String jsonMessage) {
		
		log.info("Received WX Message <--");
		log.info(jsonMessage+"\n");
		
		if(util==null)
		{
			try {
				 util=(MsgBaseUtil) context.getBean("msgbaseutil");
				
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}

		
		if(util==null)
		{
			log.error("get message unit faill ");
			return StringUtils.EMPTY;
		}
		
		
		
      String xml=   util.Json2Xml(jsonMessage);
      String env = util.getWXEnv(xml);
      String module= util.getWXModule(xml);
      String password=StringUtils.EMPTY;
      String userid= util.getWXUserId(xml);
      String msgname=util.getWXMsgName(xml);
      if(xml==StringUtils.EMPTY)
      {
    	  return StringUtils.EMPTY;
      }
      try {
    	  Document doc = DocumentHelper.parseText(xml);
          Element root=doc.getRootElement();
          Element pwdeElement = (Element) root.selectSingleNode("//PASSWORD | //Password | //password");
          if(pwdeElement!=null)
          {
        	  password = pwdeElement.getText();
        	  if(password.contains("huanghanjie1")||password.toUpperCase().contains("TEST"))
        	  {
        		  module="test";
        	  }
          }
          if(env==StringUtils.EMPTY||module==StringUtils.EMPTY||userid==StringUtils.EMPTY||msgname==StringUtils.EMPTY)
          {
        	  return StringUtils.EMPTY;
          }
          Map<String, String> map = util.getWXParamMap(xml);
         
         String mesmsg=  util.MakeMesMessage(msgname, map);
         if(mesmsg==StringUtils.EMPTY)
         {
        	 return StringUtils.EMPTY;
         }
         
        String replymsg=  tib.SendMesRequest(env, mesmsg);
        
        if(replymsg!=StringUtils.EMPTY)
        {
        	replymsg =attachWxSetting(replymsg);
            String replyjsonString=  util.makeJsonMsgForQueryReplyMsg(replymsg);
            return replyjsonString;
        }
        

          
		
	} catch (Exception e) {
		log.error(e.getMessage(),e);
		
	}
      

		
		return StringUtils.EMPTY;
	}
	
	
	private String attachWxSetting(String xml)
	{
		try {
			Document mesdoc= DocumentHelper.parseText(xml);
			Element mesroot= mesdoc.getRootElement();
			Document rtndoc= DocumentHelper.createDocument();
			rtndoc.add((Element)mesroot.clone());
			Element rtnroot= rtndoc.getRootElement();
			//ClassPathResource classPathResource = new ClassPathResource("wxsetting.xml");
			//File file = classPathResource.getFile();
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new File(util.getWxSettingFile()));
			Element root = document.getRootElement();
			Element global=(Element) root.selectSingleNode("global | Global | GLOBAL");
			if(global!=null)
			{
				rtnroot.add((Element)global.clone());
			}
			
			return (rtndoc.asXML());
			
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		
		return StringUtils.EMPTY;
	}




}
