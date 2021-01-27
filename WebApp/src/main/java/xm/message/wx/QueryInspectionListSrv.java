package xm.message.wx;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;


public class QueryInspectionListSrv extends AbsWXMsgSrv {

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

		String jxml= util.Json2Xml(jsonMessage);
		String env= util.getWXEnv(jxml);
		Map<String, String> map = util.getWXParamMap(jxml);
		boolean queryByUnit =false;
		for (String key  : map.keySet()) {
			if(key.contains("UNITNAME"))
			{
				queryByUnit=true;
				break;
			}
			
		}
		String msgname=StringUtils.EMPTY;
		if(queryByUnit)
		{
			msgname="GetInspectionListByUnit";
		}else {
			msgname="GetInspectionListByMachine";
			
		}
		String qString= util.MakeQueryMessage(msgname, map);
		if(qString!=StringUtils.EMPTY)
		{
			String rString=tib.QueryRequest(env, qString);
			if(rString!=StringUtils.EMPTY)
			{
				return util.makeJsonMsgForQueryReplyMsg(rString);
			}else {
				return rString;
			}
			
		}
		
		return StringUtils.EMPTY;
	}

}
