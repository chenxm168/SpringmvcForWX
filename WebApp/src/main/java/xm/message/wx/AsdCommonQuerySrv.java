package xm.message.wx;


import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("asdcommonquerysrv")
@Scope(value="prototype")
public class AsdCommonQuerySrv extends AbsWXMsgSrv {

	@Override
	public String Process(String jsonMessage) {
		log.info("Received WX Message <--");
		log.info(jsonMessage + "\n");
		if (util == null) {
			try {
				util = new MsgBaseUtil();

			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		if(util==null)
		{
			log.error("get message unit faill ");
			return StringUtils.EMPTY;
		}
		String jxml = util.Json2Xml(jsonMessage);
		String env = util.getWXEnv(jxml);
		String userid = util.getWXUserId(jxml);
		Map<String, String> map = util.getWXParamMap(jxml);
		String pgname = map.get("PAGE_NAME") == null ? StringUtils.EMPTY
				: map.get("PAGE_NAME");
		String tbname = map.get("TABLE_NAME") == null ? StringUtils.EMPTY
				: map.get("TABLE_NAME");

		boolean needpagesetting = map.containsKey("PAGE_SETTING") ;
				
		boolean needtablesetting = map.containsKey("TABLE_SETTING");
				
		boolean needheadersetting = map.containsKey("HEADER_SETTING");
				
		boolean onlydata = (!needheadersetting&&!needpagesetting&&!needtablesetting);
		if (onlydata) {
			String qString = util.MakeToMesQueryMessage(jxml);
			String replyString = tib.QueryRequest(env, qString);
			if (replyString != StringUtils.EMPTY) {
				return util.makeJsonMsgForQueryReplyMsg(replyString);
			} else {
				return replyString;
			}

		} else {
			

			String qString = util.MakeToMesQueryMessage(jxml);
			String replyString = tib.QueryRequest(env, qString);
			if (replyString == StringUtils.EMPTY) {
				return replyString;
			}
			else {
				
				try {
					
					Document rtndoc= DocumentHelper.createDocument();
					rtndoc.add((Element) DocumentHelper.parseText(replyString).getRootElement().clone());
					if(needpagesetting&&pgname!=StringUtils.EMPTY)
					{
					  Element element=	util.getWxPageSetting(pgname);
					  if(element!=null)
					  {
						  util.AttachElement(rtndoc, "page", element);
					  }
					}
					if(needtablesetting&&tbname!=StringUtils.EMPTY&&pgname!=StringUtils.EMPTY)
					{
						Element element = util.getWxTableSetting(pgname, tbname);
						if(element!=null)
						{
							util.AttachElement(rtndoc, tbname,  element);
						}
					}
					
					if(needheadersetting&&tbname!=StringUtils.EMPTY&&pgname!=StringUtils.EMPTY)
					{
						Element element = util.getWxTableHeaders(pgname, tbname);
						if(element!=null)
						{
							util.AttachElement(rtndoc, tbname,  element);
						}
					}
					Element datalist = util.getElement(replyString, "DATALIST");
					if(datalist!=null)
					{
						util.AttachElement(rtndoc, tbname,  datalist);
					}
					
					return util.makeJsonMsgForQueryReplyMsg( util.DeleteBodyDataList(rtndoc).asXML());
					
					
				} catch (Exception e) {
				   log.error(e.getMessage(),e);
				   return  util.makeJsonMsgForQueryReplyMsg( replyString);
				}
				
				
				
				
			}

			

		}

	}

}
