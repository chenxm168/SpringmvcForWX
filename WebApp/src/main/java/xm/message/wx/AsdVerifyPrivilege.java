package xm.message.wx;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("asdverifyprivilege")
@Scope(value="prototype")
public class AsdVerifyPrivilege extends AbsWXMsgSrv {

	@Override
	public String Process(String jsonMessage) {
		log.info("Received WX Message <--");
		log.info(jsonMessage+"\n");
		
		if(util==null)
		{
			try {
				 util=new MsgBaseUtil();
				
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}

		
		if(util==null)
		{
			log.error("get message unit faill ");
			return StringUtils.EMPTY;
		}
		String jxml= util.Json2Xml(jsonMessage);
		String userid=util.getWXUserId(jxml);
		String password =util.getXmlElementText(jxml, "PASSWORD");
		String env=util.getWXEnv(jxml);
		String keyString=util.getXmlElementText(jxml, "PRIVILEGEKEY");
		Document rtndoc=DocumentHelper.createDocument();
		Element rtnroot=rtndoc.addElement("Message");
		Element rtnReturn=rtnroot.addElement("Return");
		Element rtnRETURNCODE=	rtnReturn.addElement("RETURNCODE");
		Element rtnRETURNMESSAGE=	rtnReturn.addElement("RETURNMESSAGE");
		
		if(!this.userLoginCheck(userid, password, env))
		{
			rtnRETURNCODE.setText("LOGINERR");
			
			rtnRETURNMESSAGE.setText("USERID AND PASSWORD  ERROR ");
			return util.Xml2Json(rtndoc.asXML()) ;
		} 
		
		if(keyString==StringUtils.EMPTY)
		{
		
		rtnRETURNCODE.setText("WXMSGERR");
		
		rtnRETURNMESSAGE.setText("Not FOUND KEY From WX ");
		return util.Xml2Json(rtndoc.asXML()) ;
		
		}
		
		String qString = util.MakeToMesQueryMessage(jxml);
		if(qString==StringUtils.EMPTY)
		{
			
			rtnRETURNCODE.setText("WXMSGERR");
			
			rtnRETURNMESSAGE.setText("Not FOUND QUERY MESSAGE ON SERVER ");
			return util.Xml2Json(rtndoc.asXML()) ;
		}
		
		String rString=tib.QueryRequest(env, qString);
		if(rString==StringUtils.EMPTY)
		{
			rtnRETURNCODE.setText("NOPRIVILEG");
			
			rtnRETURNMESSAGE.setText("YOU NO PRIVILEGE");
			return util.Xml2Json(rtndoc.asXML()) ;
		}
		
		try {
			//rString=MsgUtils.Json2Xml(MsgUtils.Xml2Json(rString));
			Document mesdoc= DocumentHelper.parseText(rString);
			Element mesroot=mesdoc.getRootElement();
			//Element foudElement=(Element)mesroot.selectSingleNode("//*["+keyString+"]");
			String xpathString="//*[FUNCTIONNAME='"+keyString+"']";
			Element foudElement=(Element)mesroot.selectSingleNode(xpathString);
			if(foudElement ==null)
			{
				rtnRETURNCODE.setText("NOPRIVILEG");
				
				rtnRETURNMESSAGE.setText("YOU NO PRIVILEGE");
				return util.Xml2Json(rtndoc.asXML()) ;
			}
			
			rtnRETURNCODE.setText("0");
			
			
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			rtnRETURNCODE.setText("MSGERR");
			
			rtnRETURNMESSAGE.setText("SERVER XML MESSAGE ERROR ");
			return util.Xml2Json(rtndoc.asXML()) ;
		}
		
		
		
		
		return util.Xml2Json(rtndoc.asXML()) ;
	}
	
	private  boolean  userLoginCheck(String userid,String password,String env) {
		String msgname ="UserLogin";
		Map<String, String> map =new HashMap<String, String>();
		map.put("USERID", userid);
		map.put("PASSWORD", password);
		String mesmsg=  util.MakeMesMessage(msgname, map); 
		if(mesmsg==StringUtils.EMPTY)
		{
			return false;
		}
		String mesRtn= tib.SendMesRequest(env, mesmsg);
		String returncode= util.getXmlElementText(mesRtn, "RETURNCODE");
		if(!returncode.equals("0"))
		{
			return false;
		}
		
		
		
		return true;
		
	}

}
