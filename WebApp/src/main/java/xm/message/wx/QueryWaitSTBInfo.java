package xm.message.wx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("querywaitstbinfo")
@Scope(value="prototype")
public class QueryWaitSTBInfo extends AbsWXMsgSrv {

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

		Document rtndoc = util.MakeBaseReturnMessageDoc();
		Element rtnroot = rtndoc.getRootElement();
		Element rtnbody = (Element) rtnroot
				.selectSingleNode("//Body | //BODY | //body");
		Element rtnTFTWaitSTB = null;
		Element rtnCFWaitSTB = null;
		Element rtnTFTODFBalance=null;
		Element rtnBackITOBalance=null;
		
		List<String> pslist=new ArrayList<String>();
		//List<String> tftList=new ArrayList<String>();

		try {
			String jxml = util.Json2Xml(jsonMessage);
			String env = util.getWXEnv(jxml);
			String userid = util.getWXUserId(jxml);
			Map<String, String> map = util.getWXParamMap(jxml);
			String msgName = "getWaitSTBListForTFT";
			String qString = util.MakeQueryMessage(msgName, map);

			if (rtnbody != null) {
				rtnTFTWaitSTB = rtnbody.addElement("TFTWAITSTB");
			}
			String rString = tib.QueryRequest(env, qString);
			if (rString != StringUtils.EMPTY && rtnTFTWaitSTB != null) {
				Element dl = util.getElement(rString, "DATALIST");
				if (dl != null) {
					rtnTFTWaitSTB.add((Element) dl.clone());
					try {
						List<Element> elements=dl.elements();
						for (Element element : elements) {
							Element psElement =(Element) element.selectSingleNode("..//PRODUCTSPECNAME");
							if(psElement!=null)
							{
								String psString= psElement.getText();
								if(!pslist.contains(psString))
								{
									pslist.add(psString);
								}
							}
							
						}
						
					} catch (Exception e) {
						log.error(e.getMessage(),e);
					}

					

				}
				
			//	log.info(rtndoc.asXML());

			}

			msgName = "getWaitSTBListForCF";
			qString = util.MakeQueryMessage(msgName, map);

			if (rtnbody != null) {
				rtnCFWaitSTB = rtnbody.addElement("CFWAITSTB");
			}
			String rString2 = tib.QueryRequest(env, qString);
			if (rString2 != StringUtils.EMPTY &&rtnCFWaitSTB != null)
			{
				Element dl2 = util.getElement(rString2, "DATALIST");
				if (dl2 != null) {
					rtnCFWaitSTB.add((Element) dl2.clone());
					try {
						List<Element> elements=dl2.elements();
						for (Element element : elements) {
							Element psElement =(Element) element.selectSingleNode("..//PRODUCTSPECNAME");
							if(psElement!=null)
							{
								String psString= psElement.getText();
								if(!pslist.contains(psString))
								{
									pslist.add(psString);
								}
							}
							
						}
						
					} catch (Exception e) {
						log.error(e.getMessage(),e);
					}

				}

			}
			
			//log.info(rtndoc.asXML());
			
			
			
			//log.info(rtndoc.asXML());
			/*
			msgName="getBalanceListForAFODF";
			qString=MsgUtils.MakeQueryMessage(msgName, map);
			
			if (rtnbody != null) {
				rtnTFTODFBalance = rtnbody.addElement("TFTODFBALANCE");
			}
			String rString5 = tib.QueryRequest(env, qString);
			if (rString5 != StringUtils.EMPTY &&rtnTFTODFBalance != null)
			{
				Element dl2 = MsgUtils.getElement(rString5, "DATALIST");
				if (dl2 != null) {
					rtnTFTODFBalance.add((Element) dl2.clone());

				}

			} */
			
			//log.info(rtndoc.asXML());
			
			
			msgName="getBalanceListForBackITO";
			
			StringBuilder sb= new StringBuilder();
			sb.append("AND L.PRODUCTSPECNAME IN (");
			for(int i=0;i<pslist.size();i++)
			{
				if(i!=pslist.size()-1)
				{
					sb.append(" '"+pslist.get(i)+"', ");
					
				}else {
					sb.append(" '"+pslist.get(i)+"' ");
					
				}
				
			}
			sb.append(")");
			
			String condiString =sb.toString();
			
			Map<String, String> cMap= new HashMap<String, String>();
			cMap.put("CONDITION", condiString);
			
			
			qString=util.MakeQueryMessage(msgName, cMap);
			
			if (rtnbody != null) {
				rtnBackITOBalance = rtnbody.addElement("BACKITOBLANCE");
			}
			String rString6 = tib.QueryRequest(env, qString);
			if (rString6 != StringUtils.EMPTY &&rtnBackITOBalance != null)
			{
				Element dl2 = util.getElement(rString6, "DATALIST");
				if (dl2 != null) {
					rtnBackITOBalance.add((Element) dl2.clone());

				}

			}
			
			//log.info(rtndoc.asXML());
			
			   return util.Xml2Json(rtndoc.asXML());
			
			

		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}

		return StringUtils.EMPTY;
	}

}
