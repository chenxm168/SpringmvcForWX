package xm.message.wx;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import xm.message.MsgUtils;

@Component("asdplantaskbaseinfosrv")
@Scope(value="prototype")
public class AsdPalnTaskBaseInfoSrv extends AbsWXMsgSrv {

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
		
		String jxml=util.Json2Xml(jsonMessage);
		String env=util.getWXEnv(jxml);
		Map<String, String> map = new HashMap<String, String>();
		map.put("ENUMNAME", "CleanMaintState");
		String qString =util.MakeQueryMessage("GetEnumDefValueList", map);
		String mesr1= tib.QueryRequest(env, qString);
		map.clear();
		map.put("ENUMNAME", "PMCleanType");
		qString=util.MakeQueryMessage("GetEnumDefValueList", map);
		String mesr2=tib.QueryRequest(env, qString);
		qString=util.MakeQueryMessage("GetMachineGroupListPMS", null);
		String mesr3=tib.QueryRequest(env, qString);
		
		
		Document rtndoc=util.MakeBaseReturnMessageDoc();
		Element e1=util.getElement(mesr1, "DATALIST");
		Element e2 =util.getElement(mesr2, "DATALIST");
		Element e3 = util.getElement(mesr3, "DATALIST");
		Element root = rtndoc.getRootElement();
		try {
			Element body=(Element) root.selectSingleNode("//Body | //body | BODY");
			if(body!=null)
			{
				Element dl1 = body.addElement("CLEANMAINSTATE");
				Element dl2 = body.addElement("PMCLEANTYPE");
				Element dl3 =body.addElement("MACHINEGROUP");
				if(e1!=null)
				{
					dl1.add((Element)e1.clone());
				}
				if(e2!=null)
				{
					dl2.add((Element)e2.clone());
				}
				if(e3!=null)
				{
				   dl3.add((Element)e3.clone());
				}
				
				
			}
			
		} catch (Exception e) {
			return StringUtils.EMPTY;
		}
		
		
		return MsgUtils.Xml2Json( rtndoc.asXML());
	}

}
