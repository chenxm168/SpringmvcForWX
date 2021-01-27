package xm.message.wx;

import org.apache.commons.lang3.StringUtils;

import xm.message.MsgUtils;

public class AsdPmsSrv extends AbsWXMsgSrv {

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
		
		
		String xml = util.Json2Xml(jsonMessage);
		String env = util.getWXEnv(xml);
		String mesmsg = util.MakeToMesEventMessage(xml);
		if (mesmsg == StringUtils.EMPTY) {
			return StringUtils.EMPTY;
		}
		String replymsg = tib.SendMesRequest(env,"pms", mesmsg);
		String replyjsonString=StringUtils.EMPTY;
		if(replymsg!=StringUtils.EMPTY)
		{
			replyjsonString = util.makeJsonMsgForEventReplyMsg(replymsg);
		}else {
			log.error("Sever Not Reply Message");
		}

		 
	
		return replyjsonString;
	}

}
